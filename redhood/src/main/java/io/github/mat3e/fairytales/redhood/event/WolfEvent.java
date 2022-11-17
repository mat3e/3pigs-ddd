package io.github.mat3e.fairytales.redhood.event;

import io.github.mat3e.ddd.event.DomainEvent;

public sealed interface WolfEvent extends DomainEvent permits WolfKilled {
    int wolfId();
}
