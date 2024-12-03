package com.github.LukeCesar7.msclientes.infra.repository.application;

import com.github.LukeCesar7.msclientes.infra.repository.application.representation.ClienteSaveRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("cliente-service/api/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClientesResource {

    private final ClienteService service;

    @GetMapping
    public String status(){
        log.info("Obtendo o status do microservice de clientes");
        return "ok";
    }

    @PostMapping
    public ResponseEntity save(@Valid @RequestBody ClienteSaveRequest request){
        var cliente = request.toModel();
        service.save(cliente);
        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("cpf={cpf}")
                .buildAndExpand(cliente.getCpf())
                .toUri();
        return ResponseEntity.created(headerLocation).build();
    }



    @GetMapping(params = "cpf")
    public ResponseEntity getByCpf(@RequestParam("cpf") String cpf){
        log.info("Obtendo cliente via cpf");
        var cliente = service.getByCPF(cpf);

        if(cliente.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }
}