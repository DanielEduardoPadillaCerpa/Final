package com.parcial.app.controlador;

import com.parcial.app.entidades.*;
import java.util.List;
import com.parcial.app.repositorio.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


@Controller
@RequestMapping("/coordinacion")
public class CoordinacionController {

    @Autowired private EstudianteRepository         estudianteRepo;
    @Autowired private ProgramaAcademicoRepository  programaRepo;
    @Autowired private ResultadoTytRepository       tytRepo;
    @Autowired private ResultadoSaberProRepository  proRepo;
    @Autowired private BeneficioResolucionRepository beneficioRepo;
    @Autowired private PagoSaberProRepository       pagoRepo;
    @Autowired private UsuarioRepository            usuarioRepo;
    @Autowired private FacultadRepository facultadRepo;
    private boolean esCoord(HttpSession s) {
        Object rol = s.getAttribute("rol");
        return rol != null && (rol.equals("COORDINACION") || rol.equals("ADMINISTRADOR"));
    }

    // ── DASHBOARD ────────────────────────────────────────────────────────
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!esCoord(session)) return "redirect:/login";
        model.addAttribute("totalEstudiantes", estudianteRepo.count());
        model.addAttribute("pendientesPago",   pagoRepo.findByEstado(PagoSaberPro.Estado.PENDIENTE).size());
        model.addAttribute("ultimosEstudiantes", estudianteRepo.findAll()
            .stream().limit(5).toList());
        return "coordinacion/dashboard";
    }

    @GetMapping("/estudiantes")
    public String listar(HttpSession session, Model model) {
        if (!esCoord(session)) return "redirect:/login";

        Usuario coord = (Usuario) session.getAttribute("usuario");
        if (coord == null) return "redirect:/login";

        // Solo estudiantes de la facultad del coordinador
        model.addAttribute("estudiantes",
            estudianteRepo.findByProgramaFacultadId(coord.getFacultad().getId()));

        return "coordinacion/estudiante/lista";
    }

    @GetMapping("/estudiantes/nuevo")
    public String nuevo(HttpSession session, Model model, RedirectAttributes flash) {
        if (!esCoord(session)) return "redirect:/login";

        Usuario coord = (Usuario) session.getAttribute("usuario");
        if (coord == null || coord.getRol() != Usuario.Rol.COORDINACION || coord.getFacultad() == null) {
            flash.addFlashAttribute("error", "Debe iniciar sesión como coordinador con facultad asignada.");
            return "redirect:/login";
        }


        List<ProgramaAcademico> programas = programaRepo.findByFacultadId(coord.getFacultad().getId());

        model.addAttribute("estudiante", new Estudiante());
        model.addAttribute("programas", programas);
        model.addAttribute("tiposDoc", Estudiante.TipoDocumento.values());
        model.addAttribute("tiposExamen", Estudiante.TipoExamen.values());
        return "coordinacion/estudiante/form";
    }




    @PostMapping("/estudiantes/guardar")
    public String guardar(@Valid @ModelAttribute("estudiante") Estudiante est,
                          BindingResult result,
                          HttpSession session, Model model,
                          RedirectAttributes flash) {
        if (!esCoord(session)) return "redirect:/login";

        if (result.hasErrors()) {
            Usuario coord = (Usuario) session.getAttribute("usuario");
            List<ProgramaAcademico> programas = programaRepo.findByFacultadId(coord.getFacultad().getId());
            model.addAttribute("programas", programas);
            model.addAttribute("tiposDoc", Estudiante.TipoDocumento.values());
            model.addAttribute("tiposExamen", Estudiante.TipoExamen.values());
            return "coordinacion/estudiante/form";
        }

        Usuario coord = (Usuario) session.getAttribute("usuario");
        if (coord == null) {
            flash.addFlashAttribute("error", "No hay usuario en sesión, no se puede registrar el estudiante.");
            return "redirect:/login";
        }

        // Asignar facultad y usuario creador
        est.setFacultad(coord.getFacultad());
        est.setCreadoPor(coord);

     // Contraseña universal en texto plano
        String UNIVERSAL = "UTS2026"; 
        est.setContrasena(UNIVERSAL);


        estudianteRepo.save(est);
        flash.addFlashAttribute("exito", "Estudiante registrado. Debe subir comprobante de pago.");
        return "redirect:/coordinacion/estudiantes";
    }





    @GetMapping("/estudiantes/editar/{id}")
    public String editar(@PathVariable Integer id, HttpSession session, Model model) {
        if (!esCoord(session)) return "redirect:/login";
        model.addAttribute("estudiante", estudianteRepo.findById(id).orElseThrow());
        model.addAttribute("programas",   programaRepo.findAll());
        model.addAttribute("tiposDoc",    Estudiante.TipoDocumento.values());
        model.addAttribute("tiposExamen", Estudiante.TipoExamen.values());
        return "coordinacion/estudiante/form";
    }

    @GetMapping("/estudiantes/detalle/{id}")
    public String detalle(@PathVariable Integer id, HttpSession session, Model model) {
        if (!esCoord(session)) return "redirect:/login";
        Estudiante est = estudianteRepo.findById(id).orElseThrow();
        PagoSaberPro pago = pagoRepo.findByEstudianteId(est.getId()).orElse(null);
        model.addAttribute("estudiante", est);
        model.addAttribute("pago", pago); //comprobante
        tytRepo.findByEstudianteId(id).ifPresent(r -> model.addAttribute("resultadoTyt", r));
        proRepo.findByEstudianteId(id).ifPresent(r -> model.addAttribute("resultadoPro", r));
        pagoRepo.findByEstudianteId(id).ifPresent(p -> model.addAttribute("pago", p));
        model.addAttribute("beneficios", beneficioRepo.findByTipoExamen(
            BeneficioResolucion.TipoExamen.valueOf(est.getTipoExamen().name())));
        return "coordinacion/estudiante/detalle";
    }

    @GetMapping("/estudiantes/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, HttpSession session, RedirectAttributes flash) {
        if (!esCoord(session)) return "redirect:/login";
        try {
            estudianteRepo.deleteById(id);
            flash.addFlashAttribute("exito", "Estudiante eliminado.");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Aquí capturamos el error de integridad referencial
            flash.addFlashAttribute("error", "No se puede eliminar el estudiante porque tiene notas asociadas.");
        }
        return "redirect:/coordinacion/estudiantes";
    }


    // ── APROBAR SABER PRO ─────────────────────────────────────────────────
    @GetMapping("/estudiantes/aprobar/{id}")
    public String aprobarSaberPro(@PathVariable Integer id, HttpSession session, RedirectAttributes flash) {
        if (!esCoord(session)) return "redirect:/login";
        Estudiante est = estudianteRepo.findById(id).orElseThrow();
        est.setAprobadoSaberpro(true);
        estudianteRepo.save(est);
        flash.addFlashAttribute("exito", "Estudiante aprobado para Saber Pro.");
        return "redirect:/coordinacion/estudiantes/detalle/" + id;
    }

    // ── NOTAS TYT ─────────────────────────────────────────────────────────
    @GetMapping("/notas/tyt/{estudianteId}")
    public String notaTytForm(@PathVariable Integer estudianteId, HttpSession session, Model model) {
        if (!esCoord(session)) return "redirect:/login";
        Estudiante est = estudianteRepo.findById(estudianteId).orElseThrow();
        ResultadoTyt resultado = tytRepo.findByEstudianteId(estudianteId).orElse(new ResultadoTyt());
        resultado.setEstudiante(est);
        model.addAttribute("resultado", resultado);
        model.addAttribute("estudiante", est);
        return "coordinacion/notas/form-tyt";
    }

    @PostMapping("/notas/tyt/guardar")
    public String notaTytGuardar(@ModelAttribute("resultado") ResultadoTyt resultado,
                                 HttpSession session, RedirectAttributes flash) {
        if (!esCoord(session)) return "redirect:/login";

        Usuario coord = (Usuario) session.getAttribute("usuario");
        resultado.setSubidoPor(coord);

        // Lógica de anulación
        if (Boolean.TRUE.equals(resultado.getAnulado())) {
            resultado.setPuntajeGlobal(null);
            resultado.setNivelGlobal(null);
            resultado.setComunicacionEscrita(null);
            resultado.setComunicacionEscritaNivel(null);
            resultado.setRazonamientoCuantitativo(null);
            resultado.setRazonCuantNivel(null);
            resultado.setLecturaCritica(null);
            resultado.setLecturaCriticaNivel(null);
            resultado.setCompetenciasCiudadanas(null);
            resultado.setCompCiudadanasNivel(null);
            resultado.setIngles(null);
            resultado.setInglesNivel(null);
            resultado.setNivelInglesCertificado(null);
        }

        tytRepo.save(resultado);
        flash.addFlashAttribute("exito", "Resultados TyT guardados.");
        return "redirect:/coordinacion/estudiantes/detalle/" + resultado.getEstudiante().getId();
    }

    // ── NOTAS SABER PRO ───────────────────────────────────────────────────
    @GetMapping("/notas/pro/{estudianteId}")
    public String notaProForm(@PathVariable Integer estudianteId, HttpSession session, Model model) {
        if (!esCoord(session)) return "redirect:/login";
        Estudiante est = estudianteRepo.findById(estudianteId).orElseThrow();
        ResultadoSaberPro resultado = proRepo.findByEstudianteId(estudianteId).orElse(new ResultadoSaberPro());
        resultado.setEstudiante(est);
        model.addAttribute("resultado", resultado);
        model.addAttribute("estudiante", est);
        return "coordinacion/notas/form-saberpro";
    }

    @PostMapping("/notas/pro/guardar")
    public String notaProGuardar(@ModelAttribute("resultado") ResultadoSaberPro resultado,
                                 HttpSession session, RedirectAttributes flash) {
        if (!esCoord(session)) return "redirect:/login";
        Usuario coord = (Usuario) session.getAttribute("usuario");
        resultado.setSubidoPor(coord);
        proRepo.save(resultado);
        flash.addFlashAttribute("exito", "Resultados Saber Pro guardados.");
        return "redirect:/coordinacion/estudiantes/detalle/" + resultado.getEstudiante().getId();
    }

 // ── VERIFICAR PAGO ────────────────────────────────────────────────────
    @GetMapping("/pagos")
    public String pagosListar(HttpSession session, Model model) {
        if (!esCoord(session)) return "redirect:/login";

        Usuario coord = (Usuario) session.getAttribute("usuario");
        if (coord == null || coord.getFacultad() == null) {
            return "redirect:/login";
        }

        // Solo pagos de estudiantes de la facultad del coordinador
        model.addAttribute("pagos",
            pagoRepo.findByEstudianteFacultadId(coord.getFacultad().getId()));

        return "coordinacion/pagos/lista";
    }


    @GetMapping("/pagos/verificar/{id}")
    public String verificarPago(@PathVariable Integer id,
                                @RequestParam String estado,
                                HttpSession session,
                                RedirectAttributes flash) {
        if (!esCoord(session)) return "redirect:/login";

        PagoSaberPro pago = pagoRepo.findById(id).orElseThrow();
        pago.setEstado(PagoSaberPro.Estado.valueOf(estado));
        pago.setVerificadoPor((Usuario) session.getAttribute("usuario"));
        pago.setFechaVerificacion(java.time.LocalDate.now());
        pagoRepo.save(pago);

        if (estado.equals("VERIFICADO")) {
            flash.addFlashAttribute("exito", "Pago aprobado. Ahora puede registrar notas.");
        } else if (estado.equals("RECHAZADO")) {
            flash.addFlashAttribute("error", "Pago rechazado.");
        } else {
            flash.addFlashAttribute("info", "Estado de pago actualizado a " + estado + ".");
        }

        return "redirect:/coordinacion/pagos";
    }
    @GetMapping("/programas/{facultadId}")
    @ResponseBody
    public List<ProgramaAcademico> programasPorFacultad(@PathVariable Integer facultadId) {
        return programaRepo.findByFacultadId(facultadId);
    }
    
    @GetMapping("/estudiante/pago/ver/{nombreArchivo}")
    public ResponseEntity<Resource> verComprobante(@PathVariable String nombreArchivo) {
        // Ruta donde guardas los comprobantes
        Path path = Paths.get("C:/Parcial/comprobantes").resolve(nombreArchivo);

        if (!Files.exists(path)) {
            // Si no existe, devolvemos 404
            return ResponseEntity.notFound().build();
        }

        Resource recurso = new FileSystemResource(path);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + nombreArchivo + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(recurso);
    }


}
