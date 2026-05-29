package com.parcial.app.controlador;

import com.parcial.app.entidades.Facultad;
import com.parcial.app.entidades.Usuario;
import com.parcial.app.repositorio.FacultadRepository;
import com.parcial.app.repositorio.UsuarioRepository;
import com.parcial.app.repositorio.ProgramaAcademicoRepository;
import com.parcial.app.repositorio.BeneficioResolucionRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private FacultadRepository facultadRepo;
    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private ProgramaAcademicoRepository programaRepo;
    @Autowired private BeneficioResolucionRepository beneficioRepo;

    private boolean esAdmin(HttpSession s) {
        Object rol = s.getAttribute("rol");
        return rol != null && rol.equals("ADMINISTRADOR");
    }

    // ── DASHBOARD ───────────────────────────────────────────────
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!esAdmin(session)) return "redirect:/login";
        model.addAttribute("totalFacultades", facultadRepo.count());
        model.addAttribute("totalUsuarios",   usuarioRepo.count());
        model.addAttribute("totalDocentes",   usuarioRepo.findByRol(Usuario.Rol.DOCENTE).size());
        model.addAttribute("totalCoords",     usuarioRepo.findByRol(Usuario.Rol.COORDINACION).size());
        model.addAttribute("beneficios",      beneficioRepo.findAll());
        return "admin/dashboard";
    }

 // ── COORDINADORES ───────────────────────────────────────────
    @GetMapping("/coordinadores")
    public String coordinadoresListar(HttpSession session, Model model) {
        if (!esAdmin(session)) return "redirect:/login";
        model.addAttribute("coordinadores", usuarioRepo.findByRol(Usuario.Rol.COORDINACION));
        return "admin/coordinador/lista";
    }

    @GetMapping("/coordinadores/nuevo")
    public String coordinadorNuevo(HttpSession session, Model model) {
        if (!esAdmin(session)) return "redirect:/login";
        Usuario nuevoCoord = new Usuario();
        nuevoCoord.setRol(Usuario.Rol.COORDINACION);
        model.addAttribute("usuario", nuevoCoord);
        model.addAttribute("facultades", facultadRepo.findAll());
        return "admin/coordinador/form";
    }

   

    @GetMapping("/coordinadores/editar/{id}")
    public String coordinadorEditar(@PathVariable Integer id, HttpSession session, Model model) {
        if (!esAdmin(session)) return "redirect:/login";
        Usuario coordinador = usuarioRepo.findById(id).orElseThrow();
        model.addAttribute("usuario", coordinador);
        model.addAttribute("facultades", facultadRepo.findAll());
        return "admin/coordinador/form";
    }

    @GetMapping("/coordinadores/eliminar/{id}")
    public String coordinadorEliminar(@PathVariable Integer id, HttpSession session, RedirectAttributes flash) {
        if (!esAdmin(session)) return "redirect:/login";
        usuarioRepo.deleteById(id);
        flash.addFlashAttribute("exito", "Coordinador eliminado.");
        return "redirect:/admin/coordinadores";
    }
 // ── DOCENTES ────────────────────────────────────────────────
    @GetMapping("/docentes")
    public String docentesListar(HttpSession session, Model model) {
        if (!esAdmin(session)) return "redirect:/login";
        model.addAttribute("docentes", usuarioRepo.findByRol(Usuario.Rol.DOCENTE));
        return "admin/docente/lista";
    }

    @GetMapping("/docentes/nuevo")
    public String docenteNuevo(HttpSession session, Model model) {
        if (!esAdmin(session)) return "redirect:/login";
        Usuario nuevoDoc = new Usuario();
        nuevoDoc.setRol(Usuario.Rol.DOCENTE);
        model.addAttribute("usuario", nuevoDoc);
        model.addAttribute("facultades", facultadRepo.findAll());
        return "admin/docente/form";
    }

    @PostMapping("/coordinadores/guardar")
    public String coordinadorGuardar(@Valid @ModelAttribute("usuario") Usuario u,
                                     BindingResult result,
                                     HttpSession session, Model model,
                                     RedirectAttributes flash) {
        if (!esAdmin(session)) return "redirect:/login";

        // Validar contraseña solo al crear
        if (u.getId() == null && (u.getContrasena() == null || u.getContrasena().isBlank())) {
            result.rejectValue("contrasena", "error.contrasena", "La contraseña es obligatoria");
        }

        if (result.hasErrors()) {
            model.addAttribute("facultades", facultadRepo.findAll());
            return "admin/coordinador/form";
        }

        Usuario existente = (u.getId() != null)
            ? usuarioRepo.findById(u.getId()).orElse(new Usuario())
            : new Usuario();

        existente.setCedula(u.getCedula());
        existente.setNombre(u.getNombre());
        existente.setApellido(u.getApellido());
        existente.setCorreo(u.getCorreo());
        existente.setTelefono(u.getTelefono());
        existente.setActivo(u.getActivo());
        existente.setRol(Usuario.Rol.COORDINACION);

        if (u.getFacultad() != null && u.getFacultad().getId() != null) {
            Facultad fac = facultadRepo.findById(u.getFacultad().getId()).orElseThrow();
            existente.setFacultad(fac);
        }

        // Solo setear contraseña al crear
        if (existente.getId() == null) {
            existente.setContrasena(u.getContrasena());
        }

        usuarioRepo.save(existente);
        flash.addFlashAttribute("exito", u.getId() != null ? "Coordinador actualizado." : "Coordinador creado.");
        return "redirect:/admin/coordinadores";
    }

    @PostMapping("/docentes/guardar")
    public String docenteGuardar(@Valid @ModelAttribute("usuario") Usuario u,
                                 BindingResult result,
                                 HttpSession session, Model model,
                                 RedirectAttributes flash) {
        if (!esAdmin(session)) return "redirect:/login";

        // Validar contraseña solo al crear
        if (u.getId() == null && (u.getContrasena() == null || u.getContrasena().isBlank())) {
            result.rejectValue("contrasena", "error.contrasena", "La contraseña es obligatoria");
        }

        if (result.hasErrors()) {
            model.addAttribute("facultades", facultadRepo.findAll());
            return "admin/docente/form";
        }

        Usuario existente = (u.getId() != null)
            ? usuarioRepo.findById(u.getId()).orElse(new Usuario())
            : new Usuario();

        existente.setCedula(u.getCedula());
        existente.setNombre(u.getNombre());
        existente.setApellido(u.getApellido());
        existente.setCorreo(u.getCorreo());
        existente.setTelefono(u.getTelefono());
        existente.setActivo(u.getActivo());
        existente.setRol(Usuario.Rol.DOCENTE);

        if (u.getFacultad() != null && u.getFacultad().getId() != null) {
            Facultad fac = facultadRepo.findById(u.getFacultad().getId()).orElseThrow();
            existente.setFacultad(fac);
        }

        // Solo setear contraseña al crear
        if (existente.getId() == null) {
            existente.setContrasena(u.getContrasena());
        }

        usuarioRepo.save(existente);
        flash.addFlashAttribute("exito", u.getId() != null ? "Docente actualizado." : "Docente creado.");
        return "redirect:/admin/docentes";
    }

    @GetMapping("/docentes/editar/{id}")
    public String docenteEditar(@PathVariable Integer id, HttpSession session, Model model) {
        if (!esAdmin(session)) return "redirect:/login";
        Usuario docente = usuarioRepo.findById(id).orElseThrow();
        model.addAttribute("usuario", docente);
        model.addAttribute("facultades", facultadRepo.findAll());
        return "admin/docente/form";
    }

    @GetMapping("/docentes/eliminar/{id}")
    public String docenteEliminar(@PathVariable Integer id, HttpSession session, RedirectAttributes flash) {
        if (!esAdmin(session)) return "redirect:/login";
        usuarioRepo.deleteById(id);
        flash.addFlashAttribute("exito", "Docente eliminado.");
        return "redirect:/admin/docentes";
    }


    // ── USUARIOS GENERALES ──────────────────────────────────────
    @GetMapping("/usuarios")
    public String usuariosListar(HttpSession session, Model model) {
        if (!esAdmin(session)) return "redirect:/login";
        model.addAttribute("usuarios", usuarioRepo.findAll());
        return "admin/usuario/lista";
    }

    @GetMapping("/usuarios/nuevo")
    public String usuarioNuevo(HttpSession session, Model model) {
        if (!esAdmin(session)) return "redirect:/login";
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("facultades", facultadRepo.findAll());
        model.addAttribute("roles", Usuario.Rol.values());
        return "admin/usuario/form";
    }

    @PostMapping("/usuarios/guardar")
    public String usuarioGuardar(@Valid @ModelAttribute("usuario") Usuario u,
                                 BindingResult result,
                                 HttpSession session, Model model,
                                 RedirectAttributes flash) {
        if (!esAdmin(session)) return "redirect:/login";
        if (result.hasErrors()) {
            model.addAttribute("facultades", facultadRepo.findAll());
            model.addAttribute("roles", Usuario.Rol.values());
            return "admin/usuario/form";
        }
        usuarioRepo.save(u);
        flash.addFlashAttribute("exito", "Usuario guardado.");
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String usuarioEditar(@PathVariable Integer id, HttpSession session, Model model) {
        if (!esAdmin(session)) return "redirect:/login";
        model.addAttribute("usuario", usuarioRepo.findById(id).orElseThrow());
        model.addAttribute("facultades", facultadRepo.findAll());
        model.addAttribute("roles", Usuario.Rol.values());
        return "admin/usuario/form";
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String usuarioEliminar(@PathVariable Integer id, HttpSession session, RedirectAttributes flash) {
        if (!esAdmin(session)) return "redirect:/login";
        usuarioRepo.deleteById(id);
        flash.addFlashAttribute("exito", "Usuario eliminado.");
        return "redirect:/admin/usuarios";
    }
}
