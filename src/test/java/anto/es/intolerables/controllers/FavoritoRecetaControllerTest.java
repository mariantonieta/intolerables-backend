package anto.es.intolerables.controllers;

import anto.es.intolerables.dto.FavoritoRecetaDTO;
import anto.es.intolerables.entities.FavoritoReceta;
import anto.es.intolerables.entities.Receta;
import anto.es.intolerables.entities.Usuario;
import anto.es.intolerables.repositories.FavoritoRecetaRepository;
import anto.es.intolerables.repositories.RecetaRepository;
import anto.es.intolerables.services.FavoritoRecetaService;
import anto.es.intolerables.services.RecetaService;
import anto.es.intolerables.services.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoritoRecetaControllerTest {

    @Mock
    private FavoritoRecetaService favoritoRecetaService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private RecetaRepository recetaRepository;

    @Mock
    private FavoritoRecetaRepository favoritoRepo;

    @Mock
    private RecetaService recetaService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private FavoritoRecetaController favoritoRecetaController;

    private Usuario usuario;
    private Receta receta;
    private FavoritoReceta favoritoReceta;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setNombre("testUser");

        receta = new Receta();
        receta.setId(1);
        receta.setTitle("Test Recipe");

        favoritoReceta = new FavoritoReceta();
        favoritoReceta.setId(1);
        favoritoReceta.setUsuario(usuario);
        favoritoReceta.setReceta(receta);

        // Configuración básica de seguridad
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("testUser");
    }

    @Test
    void getFavoritos_WhenUserExists_ReturnsFavoritosList() {
        // Arrange
        when(usuarioService.findByNombre("testUser")).thenReturn(Optional.of(usuario));
        when(favoritoRepo.findByUsuario(usuario)).thenReturn(Collections.singletonList(favoritoReceta));

        // Act
        ResponseEntity<List<FavoritoRecetaDTO>> response = favoritoRecetaController.getFavoritos();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(favoritoReceta.getId(), response.getBody().get(0).getId());
    }

    @Test
    void getFavoritos_WhenUserNotFound_ThrowsRuntimeException() {
        // Arrange
        when(usuarioService.findByNombre("testUser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> favoritoRecetaController.getFavoritos());
    }

    @Test
    void crearFavorito_WithValidFavorito_ReturnsCreatedResponse() {
        // Arrange
        when(usuarioService.findByNombre("testUser")).thenReturn(Optional.of(usuario));
        when(recetaRepository.findById(any())).thenReturn(Optional.of(receta));
        when(favoritoRecetaService.save(any(FavoritoReceta.class))).thenReturn(favoritoReceta);

        // Act
        ResponseEntity<Map<String, Object>> response = favoritoRecetaController.crearFavorito(favoritoReceta);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(favoritoReceta.getId(), response.getBody().get("id"));
    }

    @Test
    void crearFavorito_WithNullReceta_ReturnsErrorResponse() {
        // Arrange
        FavoritoReceta favoritoSinReceta = new FavoritoReceta();
        favoritoSinReceta.setUsuario(usuario);
        favoritoSinReceta.setReceta(null);

        when(usuarioService.findByNombre("testUser")).thenReturn(Optional.of(usuario));

        // Act
        ResponseEntity<Map<String, Object>> response = favoritoRecetaController.crearFavorito(favoritoSinReceta);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("error"));
    }



    @Test
    void crearFavoritoDesdeSpoonacular_WithValidData_ReturnsCreatedResponse() {
        // Arrange
        Map<String, Object> datosReceta = Map.of("title", "Test Recipe", "id", 123);

        when(usuarioService.findByNombre("testUser")).thenReturn(Optional.of(usuario));
        when(recetaService.convertirDesdeSpoonacular(datosReceta)).thenReturn(receta);
        when(recetaRepository.findByTitle("Test Recipe")).thenReturn(Optional.empty());
        when(recetaRepository.save(receta)).thenReturn(receta);
        when(favoritoRecetaService.save(any(FavoritoReceta.class))).thenReturn(favoritoReceta);

        // Act
        ResponseEntity<?> response = favoritoRecetaController.crearFavoritoDesdeSpoonacular(datosReceta);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals(favoritoReceta.getId(), responseBody.get("id"));
    }

    @Test
    void crearFavoritoDesdeSpoonacular_WhenUserNotFound_ReturnsErrorResponse() {
        // Arrange
        Map<String, Object> datosReceta = Map.of("title", "Test Recipe", "id", 123);
        when(usuarioService.findByNombre("testUser")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = favoritoRecetaController.crearFavoritoDesdeSpoonacular(datosReceta);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertTrue(responseBody.containsKey("error"));
    }
}