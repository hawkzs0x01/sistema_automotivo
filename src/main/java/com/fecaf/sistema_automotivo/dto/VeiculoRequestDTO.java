package com.fecaf.sistema_automotivo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VeiculoRequestDTO {

    @NotNull(message = "O ano de fabricação é obrigatório.")
    @Positive(message = "O ano de fabricação deve ser um número positivo.")
    private Integer anoFabricacao;

    private String cor;

    @NotNull(message = "O preço é obrigatório.")
    @Positive(message = "O preço deve ser maior que zero.")
    private Double preco;

    @NotNull(message = "A quilometragem é obrigatória.")
    @PositiveOrZero(message = "A quilometragem não pode ser negativa.")
    private Double quilometragem;

    private boolean disponivel;

    @NotNull(message = "O ID do modelo é obrigatório.")
    private Long modeloId;

    // CAMPO QUE FALTAVA ADICIONADO
    private String urlCapa;
}