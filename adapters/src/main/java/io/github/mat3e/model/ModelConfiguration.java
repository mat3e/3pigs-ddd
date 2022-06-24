package io.github.mat3e.model;

import io.github.mat3e.ddd.event.DomainEvent;
import io.github.mat3e.ddd.event.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ModelConfiguration {
    @Bean
    DomainEventPublisher eventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        return new SpringDomainEventPublisher(applicationEventPublisher);
    }

    private static class SpringDomainEventPublisher implements DomainEventPublisher {
        private final ApplicationEventPublisher applicationEventPublisher;

        SpringDomainEventPublisher(final ApplicationEventPublisher publisher) {
            applicationEventPublisher = publisher;
        }

        @Override
        public void publish(final DomainEvent event) {
            applicationEventPublisher.publishEvent(event);
        }
    }
}
