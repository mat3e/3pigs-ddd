package io.github.mat3e.model.event;

import io.github.mat3e.model.vo.HouseId;
import io.github.mat3e.model.vo.Pig;

import java.time.Instant;
import java.util.List;

public record HouseAbandoned(HouseId house, List<Pig> refugees, Instant occurredOn) implements HouseEvent {
    public HouseAbandoned(HouseId house, List<Pig> refugees) {
        this(house, refugees, Instant.now());
    }
}
