package io.github.mat3e.fairytales.model;

import io.github.mat3e.ddd.event.DomainEventPublisher;
import io.github.mat3e.fairytales.model.event.WolfResignedFromAttacking;

public class BigBadWolfService {
    private final DomainEventPublisher eventPublisher;
    private final BlowingDownSpecification canBeBlownDown;

    public BigBadWolfService(final DomainEventPublisher eventPublisher) {
        this(eventPublisher, BlowingDownSpecification.defaultSpec());
    }

    BigBadWolfService(final DomainEventPublisher eventPublisher, final BlowingDownSpecification canBeBlownDown) {
        this.eventPublisher = eventPublisher;
        this.canBeBlownDown = canBeBlownDown;
    }

    public void blowDown(final House target) {
        try {
            target.handleHurricane(canBeBlownDown);
        } catch (House.IndestructibleHouseException e) {
            retryBlowing(target);
        }
    }

    private void retryBlowing(final House target) {
        try {
            target.handleHurricane(canBeBlownDown);
        } catch (House.IndestructibleHouseException e) {
            // app layer should handle/sync it carefully; can result in parallel house update
            eventPublisher.publish(new WolfResignedFromAttacking(target.getSnapshot().id()));
        }
    }
}
