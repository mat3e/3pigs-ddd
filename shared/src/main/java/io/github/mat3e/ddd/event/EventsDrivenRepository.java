package io.github.mat3e.ddd.event;

import io.github.mat3e.ddd.Aggregate;
import io.github.mat3e.ddd.DomainRepository;

import java.util.List;

public interface EventsDrivenRepository<ID, T extends Aggregate<ID, ? extends SnapshotWithEvents<ID>>> extends DomainRepository<ID, T> {
    @Override
    default T save(T aggregate) {
        return append(aggregate.getSnapshot().events());
    }

    T append(List<? extends DomainEvent> events);
}
