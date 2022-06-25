package io.github.mat3e.fairytales.pigs3.model.vo;

import io.github.mat3e.ddd.event.SnapshotWithEvents;
import io.github.mat3e.fairytales.pigs3.model.event.HouseEvent;

import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;

public record HouseSnapshot(
        HouseId id,
        Material material,
        List<Pig> pigs,
        List<HouseEvent> events
) implements SnapshotWithEvents<HouseId> {
    public HouseSnapshot(final HouseId id, final Material material, final List<Pig> pigs, final List<HouseEvent> events) {
        this.id = Objects.requireNonNull(id, "HouseId cannot be null");
        this.material = material;
        this.pigs = List.copyOf(pigs);
        this.events = List.copyOf(events);
    }

    public HouseSnapshot(final HouseId id, final Material material, final List<Pig> pigs) {
        this(id, material, pigs, emptyList());
    }
}
