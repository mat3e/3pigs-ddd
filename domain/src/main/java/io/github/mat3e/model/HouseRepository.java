package io.github.mat3e.model;

import io.github.mat3e.ddd.DomainRepository;
import io.github.mat3e.model.vo.HouseId;

/**
 * Needs to publish house events.
 */
public interface HouseRepository extends DomainRepository<HouseId, House> {
}
