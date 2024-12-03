package com.github.LukeCesar7.msclientes;

import com.github.LukeCesar7.msclientes.infra.repository.application.ClienteService;
import com.github.LukeCesar7.msclientes.infra.repository.domain.Cliente;
import com.github.LukeCesar7.msclientes.infra.repository.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void setup() {
        cliente = new Cliente("12345678901", "Clemente De Souza", 21);
    }

    @Test
    void save_DeveSalvarClienteComSucesso() {

        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente clienteSalvo = clienteService.save(cliente);

        assertNotNull(clienteSalvo);
        assertEquals("12345678901", clienteSalvo.getCpf());
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void getByCPF_DeveRetornarClienteQuandoEncontrado() {

        String cpf = "12345678901";
        when(clienteRepository.findByCpf(cpf)).thenReturn(Optional.of(cliente));


        Optional<Cliente> clienteOptional = clienteService.getByCPF(cpf);


        assertTrue(clienteOptional.isPresent());
        assertEquals("12345678901", clienteOptional.get().getCpf());
        verify(clienteRepository, times(1)).findByCpf(cpf);
    }

    @Test
    void getByCPF_DeveRetornarOptionalVazioQuandoClienteNaoEncontrado() {

        String cpf = "00000000000";
        when(clienteRepository.findByCpf(cpf)).thenReturn(Optional.empty());


        Optional<Cliente> clienteOptional = clienteService.getByCPF(cpf);


        assertFalse(clienteOptional.isPresent());
        verify(clienteRepository, times(1)).findByCpf(cpf);
    }
}

