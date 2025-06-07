package madstodolist.controller;

import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @BeforeEach
    public void setup() {
        usuarioService.deleteAll(); // <-- Añade esta línea para limpiar usuarios antes de cada test

        UsuarioData usuarioData = new UsuarioData();
        usuarioData.setEmail("usuario1@ua.es");
        usuarioData.setPassword("1234");
        usuarioData.setNombre("Usuario1");
        usuarioService.registrar(usuarioData);
    }

    /**
     * Test: La URL /registrados devuelve la página correctamente
     * y contiene el correo del usuario registrado.
     */
    @Test
    public void registradosDevuelveListadoUsuarios() throws Exception {
        mockMvc.perform(get("/registrados"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("usuario1@ua.es")));
    }

    /**
     * Test: La tabla de usuarios contiene las cabeceras "ID" y "Correo electrónico".
     */
    @Test
    public void registradosMuestraTablaConCabeceras() throws Exception {
        mockMvc.perform(get("/registrados"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ID")))
                .andExpect(content().string(containsString("Correo electrónico")));
    }

    /**
     * Test: La descripción del usuario muestra los datos correctos.
     */
    @Test
    public void descripcionUsuarioMuestraDatos() throws Exception {
        Long id = usuarioService.findByEmail("usuario1@ua.es").getId();
        mockMvc.perform(get("/registrados/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("usuario1@ua.es")))
                .andExpect(content().string(containsString("Usuario1")))
                .andExpect(content().string(containsString("ID")));
    }
}
