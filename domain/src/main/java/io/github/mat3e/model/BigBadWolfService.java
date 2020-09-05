package io.github.mat3e.model;

import io.github.mat3e.ddd.event.DomainEventPublisher;
import io.github.mat3e.model.event.WolfResignedFromAttacking;

public class BigBadWolfService {
    private final DomainEventPublisher eventPublisher;

    BigBadWolfService(final DomainEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void blowDown(final House target) {
        try {
            target.handleHurricane();
        } catch (House.IndestructibleHouseException e) {
            retryBlowing(target);
        }
    }

    private void retryBlowing(final House target) {
        try {
            target.handleHurricane();
        } catch (House.IndestructibleHouseException e) {
            // app layer should handle/sync it carefully; can result in parallel house update
            eventPublisher.publish(new WolfResignedFromAttacking(target.getSnapshot().id()));
        }
    }
}
