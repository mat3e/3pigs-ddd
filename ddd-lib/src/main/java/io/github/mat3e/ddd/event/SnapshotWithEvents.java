package io.github.mat3e.ddd.event;

import io.github.mat3e.ddd.vo.EntitySnapshot;

import java.util.List;

public interface SnapshotWithEvents<ID> extends EntitySnapshot<ID> {
    List<? extends DomainEvent> events();
}
