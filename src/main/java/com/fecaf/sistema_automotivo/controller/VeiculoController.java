package com.fecaf.sistema_automotivo.controller;

import com.fecaf.sistema_automotivo.dto.VeiculoRequestDTO;
import com.fecaf.sistema_automotivo.model.Veiculo;
import com.fecaf.sistema_automotivo.service.VeiculoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoService veiculoService;

    @GetMapping
    public ResponseEntity<List<Veiculo>> listarVeiculos(
            @RequestParam Optional<Long> marcaId, @RequestParam Optional<Long> modeloId,
            @RequestParam Optional<Integer> ano, @RequestParam Optional<Double> precoMax) {
        List<Veiculo> veiculos = veiculoService.listarVeiculos(marcaId, modeloId, ano, precoMax);
        return ResponseEntity.ok(veiculos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> buscarVeiculoPorId(@PathVariable Long id) {
        return veiculoService.buscarVeiculoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Veiculo> criarVeiculo(@Valid @RequestBody VeiculoRequestDTO dto) {
        Veiculo novoVeiculo = veiculoService.criarVeiculo(dto);
        return ResponseEntity.ok(novoVeiculo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Veiculo> atualizarVeiculo(@PathVariable Long id, @Valid @RequestBody VeiculoRequestDTO dto) {
        try {
            Veiculo veiculoAtualizado = veiculoService.atualizarVeiculo(id, dto);
            return ResponseEntity.ok(veiculoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVeiculo(@PathVariable Long id) {
        try {
            veiculoService.deletarVeiculo(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/vender")
    public ResponseEntity<Veiculo> venderVeiculo(@PathVariable Long id) {
        try {
            Veiculo veiculoVendido = veiculoService.venderVeiculo(id);
            return ResponseEntity.ok(veiculoVendido);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}