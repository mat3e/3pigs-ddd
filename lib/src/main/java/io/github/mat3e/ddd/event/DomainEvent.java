package io.github.mat3e.ddd.event;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredOn();
}
