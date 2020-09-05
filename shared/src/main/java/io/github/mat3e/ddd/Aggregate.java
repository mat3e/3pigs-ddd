package io.github.mat3e.ddd;

import io.github.mat3e.ddd.vo.EntitySnapshot;

public interface Aggregate<ID, T extends EntitySnapshot<ID>> extends DomainEntity<ID, T> {
}
