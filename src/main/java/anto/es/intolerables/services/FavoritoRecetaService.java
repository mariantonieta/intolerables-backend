package anto.es.intolerables.services;

import anto.es.intolerables.entities.FavoritoReceta;
import anto.es.intolerables.repositories.FavoritoRecetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class FavoritoRecetaService {
    private final FavoritoRecetaRepository repositorio;
    public List<FavoritoReceta> findAll(){
        return repositorio.findAll();
    }
    public Optional<FavoritoReceta> findById(Integer id){
        return repositorio.findById(id);
    }
    public FavoritoReceta save(FavoritoReceta favoritoReceta){
        return repositorio.save(favoritoReceta);
    }
    public void deleteById(Integer id) {
        repositorio.deleteById(id);
    }
}
