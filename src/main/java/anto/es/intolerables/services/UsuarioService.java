package anto.es.intolerables.services;
import anto.es.intolerables.entities.Usuario;
import anto.es.intolerables.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repositorio;

    public Optional<Usuario> findByNombre(String nombre) {
    return repositorio.findByNombre(nombre);
}

@Transactional
    public Optional<Usuario> findById(Integer id) {
        return repositorio.findById(id);
    }

    public Usuario save(Usuario usuario) {
        return repositorio.save(usuario);
    }


}