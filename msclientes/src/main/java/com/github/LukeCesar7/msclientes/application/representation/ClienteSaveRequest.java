package com.github.LukeCesar7.msclientes.application.representation;


import com.github.LukeCesar7.msclientes.domian.Cliente;
import lombok.Data;

@Data
public class ClienteSaveRequest {
    private String cpf;
    private String nome;
    private Integer idade;

    public Cliente toModel(){
        return new Cliente(cpf, nome, idade);
    }
}

