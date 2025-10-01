package com.fecaf.sistema_automotivo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer anoFabricacao;

    private String cor;

    @Column(nullable = false)
    private Double preco;

    private Double quilometragem;

    private boolean disponivel;

    private String urlCapa; // O campo da URL da imagem

    @ManyToOne
    @JoinColumn(name = "modelo_id", nullable = false)
    @JsonIgnoreProperties("veiculos")
    private Modelo modelo;
}