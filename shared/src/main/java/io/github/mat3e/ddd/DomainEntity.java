package io.github.mat3e.ddd;

import io.github.mat3e.ddd.vo.EntitySnapshot;

public interface DomainEntity<ID, T extends EntitySnapshot<ID>> {
    T getSnapshot();
}
