package com.fecaf.sistema_automotivo.controller;

import com.fecaf.sistema_automotivo.model.Marca;
import com.fecaf.sistema_automotivo.service.MarcaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/marcas")
public class MarcaController {

    @Autowired
    private MarcaService marcaService;

    @GetMapping
    public ResponseEntity<List<Marca>> listarMarcas() {
        return ResponseEntity.ok(marcaService.listarMarcas());
    }

    @PostMapping
    public ResponseEntity<Marca> criarMarca(@Valid @RequestBody Marca marca) {
        return ResponseEntity.ok(marcaService.criarMarca(marca));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Marca> atualizarMarca(@PathVariable Long id, @Valid @RequestBody Marca marca) {
        try {
            return ResponseEntity.ok(marcaService.atualizarMarca(id, marca));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMarca(@PathVariable Long id) {
        try {
            marcaService.deletarMarca(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}