package madstodolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import madstodolist.authentication.ManagerUserSession;

import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;

@Controller
public class HomeController {
    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ManagerUserSession managerUserSession;
    // Ejemplo de método para /about
    @GetMapping("/about")
    public String about(Model model) {
        Long idUsuario = managerUserSession.usuarioLogeado();
        if (idUsuario != null) {
            UsuarioData usuario = usuarioService.findById(idUsuario);
            model.addAttribute("usuario", usuario);
        }
        return "about";
    }

}

