package com.parcial.app.controlador;

import com.parcial.app.entidades.Usuario;
import com.parcial.app.repositorio.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @GetMapping("/")
    public String raiz() { 
        return "redirect:/login"; 
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        // Trae todos los usuarios ordenados por id descendente (últimos creados primero)
        var usuarios = usuarioRepo.findAll(
            org.springframework.data.domain.Sort.by(
                org.springframework.data.domain.Sort.Direction.DESC, "id"
            )
        );
        model.addAttribute("usuarios", usuarios);
        return "login";
    }

    @PostMapping("/login")
    public String loginProcesar(@RequestParam String correo,
                                @RequestParam String contrasena,
                                HttpSession session,
                                RedirectAttributes flash) {
        var usuarioOpt = usuarioRepo.findByCorreo(correo);

        if (usuarioOpt.isEmpty()) {
            flash.addFlashAttribute("error", "Correo no encontrado.");
            return "redirect:/login";
        }

        Usuario usuario = usuarioOpt.get();

        // Contraseña universal
        String UNIVERSAL = "UTS2026";

        if (!(usuario.getContrasena().equals(contrasena) || contrasena.equals(UNIVERSAL))) {
            flash.addFlashAttribute("error", "Cédula o contraseña incorrecta.");
            return "redirect:/login";
        }


        // Comparación normal o universal
        if (!(usuario.getContrasena().equals(contrasena) || contrasena.equals(UNIVERSAL))) {
            flash.addFlashAttribute("error", "Cédula o contraseña incorrecta.");
            return "redirect:/login";
        }

        if (!usuario.getActivo()) {
            flash.addFlashAttribute("error", "Usuario inactivo.");
            return "redirect:/login";
        }

        // Guardar usuario en sesión
        session.setAttribute("usuario", usuario);
        session.setAttribute("rol", usuario.getRol().name());

        // Redirigir según rol
        return switch (usuario.getRol()) {
            case ADMINISTRADOR -> "redirect:/admin/dashboard";
            case COORDINACION  -> "redirect:/coordinacion/dashboard";
            case DOCENTE       -> "redirect:/docente/dashboard";
        };
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
