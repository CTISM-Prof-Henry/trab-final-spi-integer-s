package br.ufsm.csi.salas.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sala {

    private Integer id;

    @NotBlank(message = "O tipo da sala é obrigatório")
    private String tipo;

    @NotNull(message = "A capacidade é obrigatória")
    @Min(value = 1, message = "A capacidade deve ser no mínimo 1")
    private Integer capacidade;

    @NotBlank(message = "O bloco é obrigatório")
    private String bloco;
}