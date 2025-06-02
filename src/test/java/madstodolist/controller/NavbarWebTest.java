package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.service.UsuarioService;
import madstodolist.dto.UsuarioData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class NavbarWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ManagerUserSession managerUserSession;

    /**
     * Test: La navbar aparece en la lista de tareas si el usuario está logueado.
     * Debe mostrar los enlaces y el nombre del usuario.
     */
    @Test
    public void navbarApareceEnListaTareasSiUsuarioLogeado() throws Exception {
        // Crear y registrar usuario de prueba
        UsuarioData usuarioData = new UsuarioData();
        usuarioData.setEmail("test@ua.es");
        usuarioData.setPassword("1234");
        usuarioData.setNombre("TestUser");
        UsuarioData usuario = usuarioService.registrar(usuarioData);

        // Simular login
        managerUserSession.logearUsuario(usuario.getId());

        // Comprobar que la navbar contiene los elementos esperados
        mockMvc.perform(get("/usuarios/" + usuario.getId() + "/tareas").sessionAttr("idUsuarioLogeado", usuario.getId()))
            .andExpect(content().string(containsString("ToDoList")))
            .andExpect(content().string(containsString("Tareas")))
            .andExpect(content().string(containsString("TestUser")))
            .andExpect(content().string(containsString("Cerrar sesión")));
    }

    /**
     * Test: La navbar NO aparece en las páginas de login y registro.
     */
    @Test
    public void navbarNoApareceEnLoginYRegistro() throws Exception {
        mockMvc.perform(get("/login"))
            .andExpect(content().string(not(containsString("Cerrar sesión"))))
            .andExpect(content().string(not(containsString("Tareas"))));

        mockMvc.perform(get("/registro"))
            .andExpect(content().string(not(containsString("Cerrar sesión"))))
            .andExpect(content().string(not(containsString("Tareas"))));
    }

    /**
     * Test: En la página 'about', si el usuario está logueado, se muestra la barra común.
     */
    @Test
    public void aboutMuestraBarraComunSiLogeado() throws Exception {
        // Crear y registrar usuario de prueba
        UsuarioData usuarioData = new UsuarioData();
        usuarioData.setEmail("about@ua.es");
        usuarioData.setPassword("1234");
        usuarioData.setNombre("AboutUser");
        UsuarioData usuario = usuarioService.registrar(usuarioData);

        // Simular login
        managerUserSession.logearUsuario(usuario.getId());

        // Comprobar que la navbar común aparece en /about
        mockMvc.perform(get("/about").sessionAttr("idUsuarioLogeado", usuario.getId()))
            .andExpect(content().string(containsString("Tareas")))
            .andExpect(content().string(containsString("Cerrar sesión")))
            .andExpect(content().string(containsString("AboutUser")));
    }

    /**
     * Test: En la página 'about', si NO hay usuario logueado, se muestran solo Login y Registro.
     */
    @Test
    public void aboutMuestraBarraLoginRegistroSiNoLogeado() throws Exception {
        mockMvc.perform(get("/about"))
            .andExpect(content().string(containsString("Login")))
            .andExpect(content().string(containsString("Registro")))
            .andExpect(content().string(not(containsString("Cerrar sesión"))));
    }
}