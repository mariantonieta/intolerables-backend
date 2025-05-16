package anto.es.intolerables.services;

import anto.es.intolerables.entities.Intolerancia;
import anto.es.intolerables.entities.Usuario;
import anto.es.intolerables.entities.UsuarioIntolerancia;
import anto.es.intolerables.repositories.IntoleranciaRepository;
import anto.es.intolerables.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IntoleranciaService {
    private final IntoleranciaRepository repositorio;
    private  final UsuarioRepository usuarioRepo;

    @Transactional
    public List<Intolerancia> findAll(){
        return repositorio.findAll();
    }

    public Optional<Intolerancia> findById(Integer id){
        return repositorio.findById(id);
    }

    public Intolerancia save(Intolerancia intolerancia){
        return repositorio.save(intolerancia);
    }
    //se guarda el usuario y la relacion
    @Transactional
    public boolean asociarAUsuario(String nombreIntolerancia, String nombre) {
        Optional<Usuario> usuarioOpt = usuarioRepo.findByNombre(nombre);
        Optional<Intolerancia> intoleOpt = repositorio.findByNombreContainingIgnoreCase(nombreIntolerancia);

        if (usuarioOpt.isPresent() && intoleOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Intolerancia intolerancia = intoleOpt.get();

            UsuarioIntolerancia relacion = new UsuarioIntolerancia();
            relacion.setUsuario(usuario);
            relacion.setIntolerancia(intolerancia);

            usuario.getIntolerancias().add(relacion);

            usuarioRepo.save(usuario);

            return true;
        }
        return false;
    }

}
