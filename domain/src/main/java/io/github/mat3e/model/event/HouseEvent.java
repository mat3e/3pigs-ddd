package io.github.mat3e.model.event;

import io.github.mat3e.ddd.event.DomainEvent;
import io.github.mat3e.model.vo.HouseId;

// TODO: sealed doesn't work with Groovy 3.0.7
public /*sealed*/ interface HouseEvent extends DomainEvent /*permits HouseAbandoned, WolfResignedFromAttacking*/ {
    HouseId house();
}
