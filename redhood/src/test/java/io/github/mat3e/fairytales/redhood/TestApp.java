package io.github.mat3e.fairytales.redhood;

import io.github.mat3e.ddd.event.DomainEventPublisher;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
class TestApp {
    @Bean
    DomainEventPublisher testPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return applicationEventPublisher::publishEvent;
    }
}
