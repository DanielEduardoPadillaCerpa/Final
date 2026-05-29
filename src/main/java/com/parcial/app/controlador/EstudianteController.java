package com.parcial.app.controlador;

import com.parcial.app.entidades.*;
import com.parcial.app.repositorio.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {

    @Autowired private EstudianteRepository estudianteRepo;
    @Autowired private ResultadoTytRepository tytRepo;
    @Autowired private ResultadoSaberProRepository proRepo;
    @Autowired private BeneficioResolucionRepository beneficioRepo;
    @Autowired private PagoSaberProRepository pagoRepo;

    @Value("${app.upload.dir}")
    private String uploadDir;

    private Estudiante getEstudianteSession(HttpSession s) {
        return (Estudiante) s.getAttribute("estudiante");
    }

    // ── LOGIN ───────────────────────────────────────────────
    @GetMapping("/login")
    public String loginForm(Model model) {
        var estudiantes = estudianteRepo.findAll(
            org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "id")
        );
        model.addAttribute("estudiantes", estudiantes);
        return "estudiante/login";
    }

    @PostMapping("/login")
    public String loginProcesar(@RequestParam String cedula,
                                @RequestParam String contrasena,
                                HttpSession session,
                                RedirectAttributes flash) {
        Optional<Estudiante> opt = estudianteRepo.findByCedula(cedula);
        if (opt.isEmpty() || !opt.get().getContrasena().equals(contrasena)) {
            flash.addFlashAttribute("error", "Cédula o contraseña incorrecta.");
            return "redirect:/estudiante/login";
        }
        session.setAttribute("estudiante", opt.get());
        return "redirect:/estudiante/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("estudiante");
        return "redirect:/estudiante/login";
    }

    // ── DASHBOARD ───────────────────────────────────────────
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Estudiante est = getEstudianteSession(session);
        if (est == null) return "redirect:/estudiante/login";

        est = estudianteRepo.findById(est.getId()).orElseThrow();
        session.setAttribute("estudiante", est);
        model.addAttribute("estudiante", est);

        if (est.getTipoExamen() == Estudiante.TipoExamen.TYT) {
            tytRepo.findByEstudianteId(est.getId()).ifPresent(r -> model.addAttribute("resultado", r));
        } else {
            proRepo.findByEstudianteId(est.getId()).ifPresent(r -> model.addAttribute("resultado", r));
        }

        pagoRepo.findByEstudianteId(est.getId()).ifPresent(p -> model.addAttribute("pago", p));
        model.addAttribute("beneficios", beneficioRepo.findByTipoExamen(
            BeneficioResolucion.TipoExamen.valueOf(est.getTipoExamen().name())));
        return "estudiante/dashboard";
    }

    // ── RESULTADO ───────────────────────────────────────────
    @GetMapping("/resultado")
    public String resultado(HttpSession session, Model model) {
        Estudiante est = getEstudianteSession(session);
        if (est == null) return "redirect:/estudiante/login";
        model.addAttribute("estudiante", est);

        if (est.getTipoExamen() == Estudiante.TipoExamen.TYT) {
            tytRepo.findByEstudianteId(est.getId()).ifPresent(r -> model.addAttribute("resultado", r));
        } else {
            proRepo.findByEstudianteId(est.getId()).ifPresent(r -> model.addAttribute("resultado", r));
        }
        model.addAttribute("beneficios", beneficioRepo.findByTipoExamen(
            BeneficioResolucion.TipoExamen.valueOf(est.getTipoExamen().name())));
        return "estudiante/resultado";
    }

    // ── SUBIR PAGO ──────────────────────────────────────────
    @GetMapping("/pago")
    public String pagoForm(HttpSession session, Model model) {
        Estudiante est = getEstudianteSession(session);
        if (est == null) return "redirect:/estudiante/login";
        model.addAttribute("estudiante", est);
        pagoRepo.findByEstudianteId(est.getId()).ifPresent(p -> model.addAttribute("pago", p));
        return "estudiante/pago";
    }

    @PostMapping("/pago/subir")
    public String subirPago(@RequestParam("archivo") MultipartFile archivo,
                            HttpSession session,
                            RedirectAttributes flash) {
        Estudiante est = (Estudiante) session.getAttribute("estudiante");
        if (est == null) return "redirect:/estudiante/login";

        if (archivo.isEmpty()) {
            flash.addFlashAttribute("error", "Debes seleccionar un archivo.");
            return "redirect:/estudiante/pago";
        }

        try {
            Path carpeta = Paths.get(uploadDir).toAbsolutePath();
            Files.createDirectories(carpeta);

            String nombreArchivo = est.getCedula() + "_" + archivo.getOriginalFilename();
            Path destino = carpeta.resolve(nombreArchivo);
            archivo.transferTo(destino.toFile());

            PagoSaberPro pago = pagoRepo.findByEstudianteId(est.getId()).orElse(new PagoSaberPro());
            pago.setEstudiante(est);
            pago.setArchivoPath(nombreArchivo); // guardamos solo el nombre
            pago.setNombreArchivo(nombreArchivo);
            pago.setEstado(PagoSaberPro.Estado.PENDIENTE);
            pagoRepo.save(pago);

            flash.addFlashAttribute("exito", "Comprobante subido correctamente. En espera de verificación.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al subir el archivo: " + e.getMessage());
        }

        return "redirect:/estudiante/dashboard";
    }

 // ── VER ARCHIVO ─────────────────────────────────────────
    @GetMapping("/pago/ver/{nombreArchivo}")
    public ResponseEntity<Resource> verArchivo(@PathVariable String nombreArchivo) throws IOException {
        Path archivo = Paths.get(uploadDir).toAbsolutePath().resolve(nombreArchivo).normalize();
        Resource recurso = new UrlResource(archivo.toUri());

        if (!recurso.exists()) {
            return ResponseEntity.notFound().build();
        }

        // Detectar tipo de contenido (PDF, JPG, PNG, etc.)
        String contentType = Files.probeContentType(archivo);
        if (contentType == null) {
            contentType = "application/octet-stream"; // genérico si no se detecta
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }
}
