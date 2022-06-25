package io.github.mat3e.fairytales.model.event;

import io.github.mat3e.fairytales.model.vo.HouseId;

import java.time.Instant;

public record WolfResignedFromAttacking(HouseId house, Instant occurredOn) implements HouseEvent {
    public WolfResignedFromAttacking(HouseId house) {
        this(house, Instant.now());
    }
}
