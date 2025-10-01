package com.fecaf.sistema_automotivo.service;

import com.fecaf.sistema_automotivo.dto.VeiculoRequestDTO;
import com.fecaf.sistema_automotivo.model.Modelo;
import com.fecaf.sistema_automotivo.model.Veiculo;
import com.fecaf.sistema_automotivo.repository.ModeloRepository;
import com.fecaf.sistema_automotivo.repository.VeiculoRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository veiculoRepository;
    @Autowired
    private ModeloRepository modeloRepository;

    public List<Veiculo> listarVeiculos(Optional<Long> marcaId, Optional<Long> modeloId, Optional<Integer> ano, Optional<Double> precoMax) {
        return veiculoRepository.findAll((Specification<Veiculo>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            marcaId.ifPresent(id -> predicates.add(cb.equal(root.get("modelo").get("marca").get("id"), id)));
            modeloId.ifPresent(id -> predicates.add(cb.equal(root.get("modelo").get("id"), id)));
            ano.ifPresent(a -> predicates.add(cb.equal(root.get("anoFabricacao"), a)));
            precoMax.ifPresent(p -> predicates.add(cb.lessThanOrEqualTo(root.get("preco"), p)));
            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

    public Optional<Veiculo> buscarVeiculoPorId(Long id) {
        return veiculoRepository.findById(id);
    }

    @Transactional
    public Veiculo criarVeiculo(VeiculoRequestDTO dto) {
        Modelo modelo = modeloRepository.findById(dto.getModeloId())
                .orElseThrow(() -> new RuntimeException("Modelo não encontrado com o id: " + dto.getModeloId()));
        Veiculo novoVeiculo = new Veiculo();
        novoVeiculo.setModelo(modelo);
        novoVeiculo.setAnoFabricacao(dto.getAnoFabricacao());
        novoVeiculo.setCor(dto.getCor());
        novoVeiculo.setPreco(dto.getPreco());
        novoVeiculo.setQuilometragem(dto.getQuilometragem());
        novoVeiculo.setDisponivel(dto.isDisponivel());
        novoVeiculo.setUrlCapa(dto.getUrlCapa());
        return veiculoRepository.save(novoVeiculo);
    }

    @Transactional
    public Veiculo atualizarVeiculo(Long id, VeiculoRequestDTO dto) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com o id: " + id));
        Modelo modelo = modeloRepository.findById(dto.getModeloId())
                .orElseThrow(() -> new RuntimeException("Modelo não encontrado com o id: " + dto.getModeloId()));
        veiculo.setModelo(modelo);
        veiculo.setAnoFabricacao(dto.getAnoFabricacao());
        veiculo.setCor(dto.getCor());
        veiculo.setPreco(dto.getPreco());
        veiculo.setQuilometragem(dto.getQuilometragem());
        veiculo.setDisponivel(dto.isDisponivel());
        veiculo.setUrlCapa(dto.getUrlCapa());
        return veiculoRepository.save(veiculo);
    }

    @Transactional
    public void deletarVeiculo(Long id) {
        if (!veiculoRepository.existsById(id)) {
            throw new RuntimeException("Veículo não encontrado com o id: " + id);
        }
        veiculoRepository.deleteById(id);
    }

    @Transactional
    public Veiculo venderVeiculo(Long id) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com o id: " + id));
        if (!veiculo.isDisponivel()) {
            throw new RuntimeException("Este veículo já foi vendido.");
        }
        veiculo.setDisponivel(false);
        return veiculoRepository.save(veiculo);
    }
}