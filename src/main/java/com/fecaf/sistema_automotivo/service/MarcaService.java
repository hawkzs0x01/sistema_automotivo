package com.fecaf.sistema_automotivo.service;

import com.fecaf.sistema_automotivo.model.Marca;
import com.fecaf.sistema_automotivo.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MarcaService {

    @Autowired
    private MarcaRepository marcaRepository;

    public List<Marca> listarMarcas() {
        return marcaRepository.findAll();
    }

    public Optional<Marca> buscarMarcaPorId(Long id) {
        return marcaRepository.findById(id);
    }

    @Transactional
    public Marca criarMarca(Marca marca) {

        if (marca.getNome() == null || marca.getNome().trim().isEmpty()) {
            throw new RuntimeException("O nome da marca não pode ser vazio.");
        }
        return marcaRepository.save(marca);
    }

    @Transactional
    public Marca atualizarMarca(Long id, Marca marcaDetalhes) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca não encontrada com o id: " + id));

        marca.setNome(marcaDetalhes.getNome());
        return marcaRepository.save(marca);
    }

    @Transactional
    public void deletarMarca(Long id) {

        if (!marcaRepository.existsById(id)) {
            throw new RuntimeException("Marca não encontrada com o id: " + id);
        }
        marcaRepository.deleteById(id);
    }
}