package com.parcial.app.controlador;

import com.parcial.app.entidades.Facultad;
import com.parcial.app.entidades.ProgramaAcademico;
import com.parcial.app.repositorio.FacultadRepository;
import com.parcial.app.repositorio.ProgramaAcademicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/facultades")
public class FacultadController {

    @Autowired
    private FacultadRepository facultadRepo;

    @Autowired
    private ProgramaAcademicoRepository programaRepo;

    // ── LISTAR FACULTADES ───────────────────────────────
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("facultades", facultadRepo.findAll());
        return "admin/facultad/lista";
    }

    // ── NUEVA FACULTAD ──────────────────────────────────
    @GetMapping("/nuevo")
    public String nuevaFacultad(Model model) {
        model.addAttribute("facultad", new Facultad());
        return "admin/facultad/form";
    }

    // ── GUARDAR FACULTAD ────────────────────────────────
    @PostMapping("/guardar")
    public String guardarFacultad(@ModelAttribute Facultad facultad) {
        Facultad existente = (facultad.getId() != null)
            ? facultadRepo.findById(facultad.getId()).orElse(new Facultad())
            : new Facultad();

        // Actualizar solo los campos básicos
        existente.setNombre(facultad.getNombre());
        existente.setDescripcion(facultad.getDescripcion());
        existente.setActivo(facultad.getActivo());

        facultadRepo.save(existente);
        return "redirect:/admin/facultades";
    }


    // ── EDITAR FACULTAD ─────────────────────────────────
    @GetMapping("/editar/{id}")
    public String editarFacultad(@PathVariable Integer id, Model model) {
        Facultad fac = facultadRepo.findById(id).orElseThrow();
        model.addAttribute("facultad", fac);
        return "admin/facultad/form";
    }

    // ── ELIMINAR FACULTAD ───────────────────────────────
    @GetMapping("/eliminar/{id}")
    public String eliminarFacultad(@PathVariable Integer id, RedirectAttributes flash) {
        Facultad fac = facultadRepo.findById(id).orElseThrow();

        // Verificar si la facultad tiene programas asociados
        if (!fac.getProgramas().isEmpty()) {
            flash.addFlashAttribute("error", "No se puede eliminar la facultad porque tiene programas asociados.");
            return "redirect:/admin/facultades";
        }

        facultadRepo.deleteById(id);
        flash.addFlashAttribute("exito", "Facultad eliminada correctamente.");
        return "redirect:/admin/facultades";
    }


    // ── DETALLE FACULTAD ────────────────────────────────
    @GetMapping("/{id}")
    public String detalleFacultad(@PathVariable Integer id, Model model) {
        Facultad fac = facultadRepo.findById(id).orElseThrow();
        model.addAttribute("facultad", fac);
        return "admin/facultad/detalle";
    }

    // ── NUEVO PROGRAMA ──────────────────────────────────
    @GetMapping("/{id}/programas/nuevo")
    public String nuevoPrograma(@PathVariable Integer id, Model model) {
        Facultad fac = facultadRepo.findById(id).orElseThrow();
        model.addAttribute("facultad", fac);
        model.addAttribute("programa", new ProgramaAcademico());
        return "admin/facultad/programa-form";
    }

    // ── GUARDAR PROGRAMA ────────────────────────────────
    @PostMapping("/{id}/programas/guardar")
    public String guardarPrograma(@PathVariable Integer id,
                                  @ModelAttribute ProgramaAcademico programa) {
        Facultad fac = facultadRepo.findById(id).orElseThrow();
        programa.setFacultad(fac);
        programaRepo.save(programa);
        return "redirect:/admin/facultades/" + id;
    }
}
