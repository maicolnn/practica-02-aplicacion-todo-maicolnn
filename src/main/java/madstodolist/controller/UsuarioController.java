package madstodolist.controller;

import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/registrados")
    public String listadoUsuarios(Model model, HttpSession session) {
        List<UsuarioData> usuarios = usuarioService.findAll();
        model.addAttribute("usuarios", usuarios);

        // Añadir usuario logueado al modelo para la navbar
        Long idUsuario = (Long) session.getAttribute("idUsuarioLogeado");
        if (idUsuario != null) {
            UsuarioData usuario = usuarioService.findById(idUsuario);
            model.addAttribute("usuario", usuario);
        }

        return "listaUsuarios";
    }

    @GetMapping("/registrados/{id}")
    public String descripcionUsuarioDetalle(@PathVariable Long id, Model model, HttpSession session) {
        UsuarioData usuarioDescripcion = usuarioService.findById(id);
        if (usuarioDescripcion == null) {
            return "redirect:/registrados";
        }
        model.addAttribute("usuarioDescripcion", usuarioDescripcion);

        // Añadir usuario logueado al modelo para la navbar
        Long idUsuario = (Long) session.getAttribute("idUsuarioLogeado");
        if (idUsuario != null) {
            UsuarioData usuario = usuarioService.findById(idUsuario);
            model.addAttribute("usuario", usuario);
        }

        return "descripcionUsuario";
    }
}
