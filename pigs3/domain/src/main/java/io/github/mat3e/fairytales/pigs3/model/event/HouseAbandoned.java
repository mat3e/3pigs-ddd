package io.github.mat3e.fairytales.pigs3.model.event;

import io.github.mat3e.fairytales.pigs3.model.vo.HouseId;
import io.github.mat3e.fairytales.pigs3.model.vo.Pig;

import java.time.Instant;
import java.util.List;

public record HouseAbandoned(HouseId house, List<Pig> refugees, Instant occurredOn) implements HouseEvent {
    public HouseAbandoned(final HouseId house, final List<Pig> refugees, final Instant occurredOn) {
        this.house = house;
        this.refugees = List.copyOf(refugees);
        this.occurredOn = occurredOn;
    }

    public HouseAbandoned(HouseId house, List<Pig> refugees) {
        this(house, refugees, Instant.now());
    }
}
