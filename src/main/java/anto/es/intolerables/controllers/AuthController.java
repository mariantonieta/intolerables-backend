package anto.es.intolerables.controllers;

import anto.es.intolerables.dto.UsuarioDTO;
import anto.es.intolerables.entities.Usuario;
import anto.es.intolerables.security.CustomUserDetailsService;
import anto.es.intolerables.security.jwt.JwtTokenProvider;
import anto.es.intolerables.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UsuarioService usuarioService;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioDTO loginDto) {
        log.info("Intento de login para usuario: {}", loginDto.getNombre());

        return usuarioService.findByNombre(loginDto.getNombre())
                .map(usuario -> {
                    if (passwordEncoder.matches(loginDto.getContrasena(), usuario.getContrasena())) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getNombre());
                        String token = tokenProvider.generateToken(userDetails);
                     //   log.info("Login exitoso para el usuario: {}", usuario.getNombre());
                        return ResponseEntity.ok(Map.of(
                                "token", token,
                                "usuario", Map.of("id", usuario.getId(), "nombre", usuario.getNombre())
                        ));
                    } else {
                       // log.warn("Contraseña incorrecta para usuario: {}", loginDto.getNombre());
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(Map.of("error", "Contraseña incorrecta"));
                    }
                })
                .orElseGet(() -> {
                    // log.warn("Usuario no encontrado: {}", loginDto.getNombre());
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of("error", "Usuario no encontrado"));
                });
    }
    @PostMapping("/register-api")
    public ResponseEntity<?> registerApi(@RequestBody UsuarioDTO usuarioDto) {
        log.info("Datos recibidos: {}", usuarioDto);
        Optional<Usuario> usuarioBD = usuarioService.findByNombre(usuarioDto.getNombre());

        if (usuarioBD.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ya existe un usuario con ese nombre"));
        }

        Usuario usuario = new Usuario(
                null, // ID será generado automáticamente
                usuarioDto.getNombre(),
                passwordEncoder.encode(usuarioDto.getContrasena()),
                LocalDate.now(),
                usuarioDto.getPaisUsuario(),
                usuarioDto.getCiudad(),
                null, // Favoritos inicializados como null o lista vacía
                null, // Favorito de recetas
                null  // Intolerancias
        );;

        usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Usuario registrado con éxito"));
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> obtenerUsuarioPorId(@PathVariable Integer id) {
        log.info("Buscando usuario con ID: {}", id);

        return usuarioService.findById(id)
                .map(usuario -> ResponseEntity.ok(Map.of(
                        "id", usuario.getId(),
                        "nombre", usuario.getNombre(),
                        "paisUsuario", usuario.getPaisUsuario(),
                        "ciudadUsuario", usuario.getCiudadUsuario(),
                        "fechaRegistro", usuario.getFechaRegistro()
                )))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Usuario no encontrado")));
    }
}
