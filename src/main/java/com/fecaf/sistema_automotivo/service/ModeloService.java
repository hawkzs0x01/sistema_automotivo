package com.fecaf.sistema_automotivo.service;

import com.fecaf.sistema_automotivo.dto.ModeloRequestDTO;
import com.fecaf.sistema_automotivo.model.Marca;
import com.fecaf.sistema_automotivo.model.Modelo;
import com.fecaf.sistema_automotivo.repository.MarcaRepository;
import com.fecaf.sistema_automotivo.repository.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ModeloService {

    @Autowired
    private ModeloRepository modeloRepository;
    @Autowired
    private MarcaRepository marcaRepository;

    public List<Modelo> listarModelos() {
        return modeloRepository.findAll();
    }

    @Transactional
    public Modelo criarModelo(ModeloRequestDTO dto) {
        Marca marca = marcaRepository.findById(dto.getMarcaId())
                .orElseThrow(() -> new RuntimeException("Marca não encontrada com o id: " + dto.getMarcaId()));

        Modelo novoModelo = new Modelo();
        novoModelo.setNome(dto.getNome());
        novoModelo.setMarca(marca);

        return modeloRepository.save(novoModelo);
    }
}