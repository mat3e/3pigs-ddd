package io.github.mat3e.model.vo;

import io.github.mat3e.ddd.event.SnapshotWithEvents;
import io.github.mat3e.model.event.HouseEvent;

import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;

public record HouseSnapshot(HouseId id, Material material, List<Pig> pigs, List<HouseEvent> events) implements SnapshotWithEvents<HouseId> {
    public HouseSnapshot {
        Objects.requireNonNull(id, "HouseId cannot be null");
    }

    public HouseSnapshot(final HouseId id, final Material material, final List<Pig> pigs) {
        this(id, material, pigs, emptyList());
    }
}
