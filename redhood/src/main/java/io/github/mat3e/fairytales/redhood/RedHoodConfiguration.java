package io.github.mat3e.fairytales.redhood;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RedHoodConfiguration {
    private final WolfRepository wolfRepository;

    RedHoodConfiguration(WolfRepository wolfRepository) {
        this.wolfRepository = wolfRepository;
    }

    @Bean
    RedHoodCommandHandler commandHandler() {
        return new RedHoodCommandHandler(wolfRepository);
    }
}
