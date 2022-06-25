package io.github.mat3e.fairytales.pigs3.model.event;

import io.github.mat3e.ddd.event.DomainEvent;
import io.github.mat3e.fairytales.pigs3.model.vo.HouseId;

public sealed interface HouseEvent extends DomainEvent permits HouseAbandoned, WolfResignedFromAttacking {
    HouseId house();
}
