package anto.es.intolerables.security;

import anto.es.intolerables.entities.Usuario;
import anto.es.intolerables.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
//para buscar al user en la base de datos al iniciar sesion
@Service("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


private final UsuarioRepository usuarioRepository;

@Override
public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {
    Usuario usuario = usuarioRepository.findByNombre(nombre)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + nombre));

    return User.builder()
            .username(usuario.getNombre())
            .password(usuario.getContrasena())
            .authorities(Collections.emptyList())
            .build();
}
}