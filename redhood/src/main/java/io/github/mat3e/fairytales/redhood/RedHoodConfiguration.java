package io.github.mat3e.fairytales.redhood;

import io.github.mat3e.ddd.event.DomainEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RedHoodConfiguration {
    private final WolfRepository wolfRepository;
    private final DomainEventPublisher eventPublisher;

    RedHoodConfiguration(WolfRepository wolfRepository, DomainEventPublisher publisher) {
        this.wolfRepository = wolfRepository;
        this.eventPublisher = publisher;
    }

    @Bean
    RedHoodService commandHandler() {
        return new RedHoodService(wolfRepository, eventPublisher);
    }
}
