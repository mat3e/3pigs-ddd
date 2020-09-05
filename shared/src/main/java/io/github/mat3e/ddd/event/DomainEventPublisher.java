package io.github.mat3e.ddd.event;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
