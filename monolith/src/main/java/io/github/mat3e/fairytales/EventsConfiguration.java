package io.github.mat3e.fairytales;

import io.github.mat3e.ddd.event.DomainEvent;
import io.github.mat3e.ddd.event.DomainEventPublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
class EventsConfiguration {
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

    @EnableAsync
    @Configuration
    @ConditionalOnProperty(value = "spring.main.web-application-type", havingValue = "servlet")
    static class AsyncConfiguration {
    }
}
