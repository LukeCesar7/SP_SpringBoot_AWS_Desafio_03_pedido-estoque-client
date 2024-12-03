package com.github.LukeCesar7.msclientes.infra.repository.application.representation;

import com.github.LukeCesar7.msclientes.infra.repository.domain.Cliente;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ClienteSaveRequest {

    @NotBlank(message = "O CPF não pode ser vazio")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter 11 dígitos numéricos")
    private String cpf;

    @NotBlank(message = "O nome não pode ser vazio")
    @Size(min = 3, max = 45, message = "O nome deve ter entre 3 e 45 caracteres")
    private String nome;

    @NotNull(message = "A idade é entre 18 e 60 anos")
    @Min(value = 18, message = "A idade mínima permitida é 18 anos")
    @Max(value = 60, message = "A idade máxima permitida é 60 anos")
    private Integer idade;

    public Cliente toModel() {
        return new Cliente(cpf, nome, idade);
    }
}


