package com.parcial.app.controlador;

import com.parcial.app.entidades.*;
import com.parcial.app.repositorio.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/docente")
public class DocenteController {

    @Autowired private EstudianteRepository          estudianteRepo;
    @Autowired private ResultadoTytRepository        tytRepo;
    @Autowired private ResultadoSaberProRepository   proRepo;
    @Autowired private BeneficioResolucionRepository beneficioRepo;
    @Autowired private FacultadRepository            facultadRepo;

    private boolean esDocente(HttpSession s) {
        Object rol = s.getAttribute("rol");
        return rol != null && (rol.equals("DOCENTE") || rol.equals("COORDINACION") || rol.equals("ADMINISTRADOR"));
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!esDocente(session)) return "redirect:/login";

        Usuario docente = (Usuario) session.getAttribute("usuario");
        if (docente == null) return "redirect:/login";

        // Contar todos los estudiantes, sin importar facultad
        long totalEstudiantes = estudianteRepo.count();

        model.addAttribute("totalEstudiantes", totalEstudiantes);
        model.addAttribute("beneficios", beneficioRepo.findAll());
        return "docente/dashboard";
    }

    // ── DETALLE (solo lectura) ────────────────────────────────────────────
    @GetMapping("/consulta/detalle/{id}")
    public String detalle(@PathVariable Integer id, HttpSession session, Model model) {
        if (!esDocente(session)) return "redirect:/login";

        Estudiante est = estudianteRepo.findById(id).orElseThrow();

        // Ya no validamos la facultad, el docente puede ver cualquier estudiante
        model.addAttribute("estudiante", est);
        tytRepo.findByEstudianteId(id).ifPresent(r -> model.addAttribute("resultadoTyt", r));
        proRepo.findByEstudianteId(id).ifPresent(r -> model.addAttribute("resultadoPro", r));
        model.addAttribute("beneficios", beneficioRepo.findByTipoExamen(
            BeneficioResolucion.TipoExamen.valueOf(est.getTipoExamen().name())));
        return "docente/consulta/detalle";
    }



    // ── BUSCAR POR CÉDULA ─────────────────────────────────────────────────
    @GetMapping("/consulta/por-cedula")
    public String porCedulaForm(HttpSession session, Model model) {
        if (!esDocente(session)) return "redirect:/login";
        return "docente/consulta/por-cedula";
    }

    @GetMapping("/consulta/por-cedula/buscar")
    public String porCedulaBuscar(@RequestParam String cedula,
                                  HttpSession session, Model model) {
        if (!esDocente(session)) return "redirect:/login";
        model.addAttribute("cedula", cedula);
        estudianteRepo.findByCedula(cedula).ifPresentOrElse(
            est -> model.addAttribute("estudiante", est),
            ()  -> model.addAttribute("noEncontrado", true)
        );
        return "docente/consulta/por-cedula";
    }

    // ── BUSCAR POR FACULTAD ───────────────────────────────────────────────
    @GetMapping("/consulta/por-facultad")
    public String porFacultadForm(HttpSession session, Model model) {
        if (!esDocente(session)) return "redirect:/login";
        model.addAttribute("facultades", facultadRepo.findAll());
        return "docente/consulta/por-facultad";
    }

    @GetMapping("/consulta/por-facultad/buscar")
    public String porFacultadBuscar(@RequestParam Integer facultadId,
                                    HttpSession session, Model model) {
        if (!esDocente(session)) return "redirect:/login";
        model.addAttribute("facultades",   facultadRepo.findAll());
        model.addAttribute("facultadId",   facultadId);
        model.addAttribute("estudiantes",  estudianteRepo.findByProgramaFacultadId(facultadId));
        return "docente/consulta/por-facultad";
    }

    

}