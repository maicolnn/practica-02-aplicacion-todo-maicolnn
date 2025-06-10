# Documentación - Aplicación ToDoList
### Autor: Maicol Nasimba

## Introducción

Este documento describe la evolución técnica de la aplicación ToDoList durante la implementación de las funcionalidades de barra de menú, gestión de usuarios y administración. 

---

## 1. Nuevas Clases y Métodos Implementados

### Controladores

- **UsuarioController**
  - `@GetMapping("/registrados")`: Devuelve el listado de usuarios registrados.
  - `@GetMapping("/registrados/{id}")`: Muestra la descripción detallada de un usuario.
- **LoginController**
  - Métodos para login, logout y registro, incluyendo la lógica para permitir solo un administrador.
- **HomeController**
  - `@GetMapping("/about")`: Muestra la página "Acerca de", adaptando la barra de menú según el estado de sesión.

### Servicios

- **UsuarioService**
  - `findAll()`: Devuelve todos los usuarios registrados.
  - `findById(Long id)`: Busca un usuario por su ID.
  - `findByEmail(String email)`: Busca un usuario por su email.
  - `registrar(UsuarioData usuario)`: Registra un nuevo usuario, permitiendo marcarlo como administrador si no existe otro.
  - `existeAdministrador()`: Comprueba si ya existe un usuario administrador.
  - `deleteAll()`: Elimina todos los usuarios y tareas (usado en tests).

### DTO

- **UsuarioData**
  - Añadido el atributo `admin` (Boolean) y sus getters/setters para distinguir usuarios administradores.

---

## 2. Plantillas Thymeleaf Añadidas o Modificadas

- **fragments.html**: Fragmento de navbar común, con lógica para mostrar diferentes enlaces según el estado de sesión.
- **about.html**: Página "Acerca de", con navbar dinámica (común si logueado, login/registro si no).
- **listaUsuarios.html**: Listado de usuarios registrados, con enlace a la descripción de cada uno.
- **descripcionUsuario.html**: Muestra todos los datos del usuario seleccionado, excepto la contraseña, e indica si es administrador o usuario estándar.
- **formRegistro.html**: Formulario de registro con checkbox para administrador, deshabilitado si ya existe uno.

---

## 3. Explicación de los Tests Implementados

- **UsuarioControllerWebTest**
  - Verifica que `/registrados` muestra la tabla de usuarios con sus respectivas cabeceras y que la descripción de usuario muestra los datos correctos.
- **NavbarWebTest**
  - Comprueba que la barra de menú se muestra correctamente según el estado de sesión y que no aparece en login/registro.
- **UsuarioServiceTest**
  - Testea el registro de usuarios, la restricción de un solo administrador y la consulta de usuarios.
- **UsuarioWebTest**  
  - Se han añadido tests que moquean el servicio de usuario para verificar que el checkbox de administrador está deshabilitado si ya existe un administrador y también para comprobar que un administrador al hacer login es redirigido al listado de usuarios .


Los tests usan MockMvc para simular peticiones HTTP y comprobar el contenido de las vistas, así como la lógica de negocio en los servicios.

---

## 4. Explicación de Parte del código implementado

### Ejemplo 1: Navbar dinámica en Thymeleaf

```html
<!-- fragments.html -->
<nav th:fragment="navbar (usuario)">
    <nav class="navbar navbar-expand-lg navbar-dark" style="background-color: #1857a4;" th:if="${usuario != null}">
        <a class="navbar-brand text-white" href="/about">ToDoList</a>
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link text-white" th:href="@{/usuarios/{id}/tareas(id=${usuario.id})}">Tareas</a>
            </li>
        </ul>
        <ul class="navbar-nav ml-auto">
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle text-white" href="#" id="navbarDropdown" role="button"
                   data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"
                   th:text="${usuario.nombre}">Usuario</a>
                <div class="dropdown-menu dropdown-menu-right">
                    <a class="dropdown-item" href="#">Cuenta</a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" th:href="@{/logout}">Cerrar sesión <span th:text="'(' + ${usuario.nombre} + ')'"></span></a>
                </div>
            </li>
        </ul>
    </nav>
</nav>
```

Este fragmento define la barra de menú común, mostrando enlaces y el nombre del usuario logueado. Si no hay usuario, la barra no se muestra.

---

### Ejemplo 2: Restricción de un solo administrador en el registro

```java
// UsuarioService.java
@Transactional(readOnly = true)
public boolean existeAdministrador() {
    return usuarioRepository.existsByAdminTrue();
}
```
  
Este método permite comprobar si ya existe un usuario administrador, lo que se utiliza para habilitar o deshabilitar el checkbox de administrador en el formulario de registro.

---

### Ejemplo 3: Mostrar el rol del usuario en la descripción

```html
<!-- descripcionUsuario.html -->
<tr>
    <th>Rol</th>
    <td th:text="${usuarioDescripcion.admin} ? 'Administrador' : 'Usuario estándar'"></td>
</tr>
```
  
Esta línea en la plantilla muestra "Administrador" si el usuario tiene el atributo `admin` a `true`, y "Usuario estándar" en caso contrario.

---

## 5. Consideraciones de las nuevas funcionalidades implementadas


- **Navbar dinámica:** Se utiliza un fragmento Thymeleaf para la barra de menú, que cambia según si el usuario está logueado o no.
- **Restricción de administrador único:** El formulario de registro deshabilita la opción de administrador si ya existe uno, tanto en la lógica de backend como en la interfaz.
- **Separación de usuario logueado y usuario descrito:** En la descripción de usuario, se diferencian claramente los datos del usuario logueado (para la navbar) y del usuario cuya información se está mostrando.
- **Tests integrados:** Los tests cubren tanto la lógica de negocio como la integración web, asegurando la robustez de las nuevas funcionalidades.

---

## 6. Resumen

La aplicación ha evolucionado para soportar una gestión básica de usuarios, incluyendo la distinción de roles, una barra de navegación contextual y la restricción de un solo administrador. El diseño modular y los tests aseguran la mantenibilidad y extensibilidad del sistema.