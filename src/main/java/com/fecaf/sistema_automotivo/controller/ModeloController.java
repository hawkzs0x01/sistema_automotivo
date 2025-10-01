package com.fecaf.sistema_automotivo.controller;

import com.fecaf.sistema_automotivo.dto.ModeloRequestDTO;
import com.fecaf.sistema_automotivo.model.Modelo;
import com.fecaf.sistema_automotivo.service.ModeloService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/modelos")
public class ModeloController {

    @Autowired
    private ModeloService modeloService;

    @GetMapping
    public ResponseEntity<List<Modelo>> listarModelos() {
        return ResponseEntity.ok(modeloService.listarModelos());
    }

    @PostMapping
    public ResponseEntity<Modelo> criarModelo(@Valid @RequestBody ModeloRequestDTO dto) {
        return ResponseEntity.ok(modeloService.criarModelo(dto));
    }


}