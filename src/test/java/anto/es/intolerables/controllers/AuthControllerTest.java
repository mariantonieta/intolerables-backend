package anto.es.intolerables.controllers;



import anto.es.intolerables.dto.UsuarioDTO;
import anto.es.intolerables.entities.Usuario;
import anto.es.intolerables.entities.UsuarioIntolerancia;
import anto.es.intolerables.repositories.IntoleranciaRepository;
import anto.es.intolerables.security.CustomUserDetailsService;
import anto.es.intolerables.security.jwt.JwtTokenProvider;
import anto.es.intolerables.services.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private IntoleranciaRepository intoleranciaRepo;

    @InjectMocks
    private AuthController authController;

    private UsuarioDTO usuarioDTO;
    private Usuario usuario;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("testuser");
        usuarioDTO.setContrasena("password");
        usuarioDTO.setPaisUsuario("España");
        usuarioDTO.setCiudad("Madrid");

        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("testuser");
        usuario.setContrasena("encodedPassword");
        usuario.setFechaRegistro(LocalDate.now());
        usuario.setPaisUsuario("España");
        usuario.setCiudadUsuario("Madrid");

        userDetails = User.withUsername("testuser")
                .password("encodedPassword")
                .authorities("USER")
                .build();
    }

    @Test
    void login_Successful() {
        // Arrange
        when(usuarioService.findByNombre(anyString())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(tokenProvider.generateToken(any(UserDetails.class))).thenReturn("mockedToken");

        // Act
        ResponseEntity<?> response = authController.login(usuarioDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("mockedToken", responseBody.get("token"));

        @SuppressWarnings("unchecked")
        Map<String, Object> usuarioMap = (Map<String, Object>) responseBody.get("usuario");
        assertEquals(1, usuarioMap.get("id"));
        assertEquals("testuser", usuarioMap.get("nombre"));
    }

    @Test
    void login_WrongPassword() {
        // Arrange
        when(usuarioService.findByNombre(anyString())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act
        ResponseEntity<?> response = authController.login(usuarioDTO);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Contraseña incorrecta", responseBody.get("error"));
    }

    @Test
    void login_UserNotFound() {
        // Arrange
        when(usuarioService.findByNombre(anyString())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = authController.login(usuarioDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Usuario no encontrado", responseBody.get("error"));
    }

    @Test
    void registerApi_Successful() {
        // Arrange
        when(usuarioService.findByNombre(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioService.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        ResponseEntity<?> response = authController.registerApi(usuarioDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Usuario registrado con éxito", responseBody.get("mensaje"));
    }

    @Test
    void registerApi_UserAlreadyExists() {
        // Arrange
        when(usuarioService.findByNombre(anyString())).thenReturn(Optional.of(usuario));

        // Act
        ResponseEntity<?> response = authController.registerApi(usuarioDTO);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Ya existe un usuario con tu nombre, pon tu apodo", responseBody.get("error"));
    }

    @Test
    void obtenerUsuarioPorId_Successful() {
        // Arrange
        when(usuarioService.findById(anyInt())).thenReturn(Optional.of(usuario));

        // Act
        ResponseEntity<?> response = authController.obtenerUsuarioPorId(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals(1, responseBody.get("id"));
        assertEquals("testuser", responseBody.get("nombre"));
        assertEquals("España", responseBody.get("paisUsuario"));
        assertEquals("Madrid", responseBody.get("ciudadUsuario"));
        assertNotNull(responseBody.get("fechaRegistro"));
    }

    @Test
    void obtenerUsuarioPorId_NotFound() {
        // Arrange
        when(usuarioService.findById(anyInt())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = authController.obtenerUsuarioPorId(1);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Usuario no encontrado", responseBody.get("error"));
    }

  @Test
    void obtenerPerfil_NotFound() {
        // Arrange
        when(usuarioService.findById(anyInt())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = authController.obtenerPerfil(1);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Usuario no encontrado", responseBody.get("error"));
    }

    @Test
    void actualizarPerfil_Successful() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("paisUsuario", "Francia");
        request.put("ciudadUsuario", "París");
        request.put("intoleranciasIds", Collections.singletonList(1));

        when(usuarioService.findById(anyInt())).thenReturn(Optional.of(usuario));
        when(intoleranciaRepo.findByIdIn(anyList())).thenReturn(Collections.emptyList());
        when(usuarioService.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        ResponseEntity<?> response = authController.actualizarPerfil(1, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Perfil actualizado correctamente", responseBody.get("mensaje"));
    }

    @Test
    void actualizarPerfil_NotFound() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        when(usuarioService.findById(anyInt())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = authController.actualizarPerfil(1, request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Usuario no encontrado", responseBody.get("error"));
    }
}