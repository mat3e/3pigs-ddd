package io.github.mat3e.model.event;

import io.github.mat3e.ddd.event.DomainEvent;
import io.github.mat3e.model.vo.HouseId;

public sealed interface HouseEvent extends DomainEvent permits HouseAbandoned, WolfResignedFromAttacking {
    HouseId house();
}
