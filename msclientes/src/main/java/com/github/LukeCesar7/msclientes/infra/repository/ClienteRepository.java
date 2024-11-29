package com.github.LukeCesar7.msclientes.infra.repository;


import com.github.LukeCesar7.msclientes.domian.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCpf(String cpf);
}
