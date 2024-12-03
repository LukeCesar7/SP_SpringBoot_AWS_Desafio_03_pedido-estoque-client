package com.github.LukeCesar7.msestoque.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EstoqueConfig {
    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
