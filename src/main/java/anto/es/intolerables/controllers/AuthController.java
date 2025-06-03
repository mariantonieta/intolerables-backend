package anto.es.intolerables.controllers;

import anto.es.intolerables.dto.UsuarioDTO;
import anto.es.intolerables.entities.Usuario;
import anto.es.intolerables.entities.UsuarioIntolerancia;
import anto.es.intolerables.repositories.IntoleranciaRepository;
import anto.es.intolerables.security.CustomUserDetailsService;
import anto.es.intolerables.security.jwt.JwtTokenProvider;

import anto.es.intolerables.services.UsuarioService;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UsuarioService usuarioService;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;
    private final IntoleranciaRepository intoleranciaRepo;

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
                    .body(Map.of("error", "Ya existe un usuario con tu nombre, pon tu apodo"));
        }

        Usuario usuario = new Usuario(
                null,
                usuarioDto.getNombre(),
                passwordEncoder.encode(usuarioDto.getContrasena()),
                LocalDate.now(),
                usuarioDto.getPaisUsuario(),
                usuarioDto.getCiudad(),
                null,
                null,
                null
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

    @Transactional
    @GetMapping("/usuario/{id}/perfil")
    public ResponseEntity<?> obtenerPerfil(@PathVariable Integer id) {
        return usuarioService.findById(id)
                .map(usuario -> {
                    usuario.getIntolerancias().size(); // Forzar la carga para evitar LazyInitializationException

                    // Construir la lista con nombres de las intolerancias correctamente
                    List<Map<String, Object>> intolerancias = usuario.getIntolerancias().stream()
                            .map(usuarioIntolerancia -> {
                                Map<String, Object> intoleranciaMap = new HashMap<>();
                                intoleranciaMap.put("id", usuarioIntolerancia.getIntolerancia().getId());
                                intoleranciaMap.put("nombre", usuarioIntolerancia.getIntolerancia().getNombre());
                                return intoleranciaMap;
                            })
                            .toList(); // O usa .collect(Collectors.toList()) en Java 8

                    return ResponseEntity.ok(Map.of(
                            "id", usuario.getId(),
                            "nombre", usuario.getNombre(),
                            "paisUsuario", usuario.getPaisUsuario(),
                            "ciudadUsuario", usuario.getCiudadUsuario(),
                            "intolerancias", intolerancias
                    ));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Usuario no encontrado")));
    }



    @PutMapping("/usuario/{id}/perfil")
    public ResponseEntity<?> actualizarPerfil(@PathVariable Integer id, @RequestBody Map<String, Object> request) {
        return usuarioService.findById(id).map(usuario -> {
            usuario.setPaisUsuario((String) request.get("paisUsuario"));
            usuario.setCiudadUsuario((String) request.get("ciudadUsuario"));

            // Actualizar intolerancias si se proporciona una lista
            if (request.containsKey("intoleranciasIds")) {
                List<Integer> intoleranciasIds = (List<Integer>) request.get("intoleranciasIds");

                // Convertir cada Intolerancia en UsuarioIntolerancia
                List<UsuarioIntolerancia> usuarioIntolerancias = intoleranciaRepo.findByIdIn(intoleranciasIds).stream()
                        .map(intolerancia -> new UsuarioIntolerancia(usuario, intolerancia))
                        .toList();

                usuario.setIntolerancias(usuarioIntolerancias);
            }

            usuarioService.save(usuario);
            return ResponseEntity.ok(Map.of("mensaje", "Perfil actualizado correctamente"));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Usuario no encontrado")));
    }




}
