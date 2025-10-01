package com.fecaf.sistema_automotivo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModeloRequestDTO {

    @NotBlank(message = "O nome do modelo não pode estar em branco.")
    @Size(min = 2, max = 50, message = "O nome do modelo deve ter entre 2 e 50 caracteres.")
    private String nome;

    @NotNull(message = "O ID da marca é obrigatório.")
    private Long marcaId;
}