package io.github.mat3e.fairytales.pigs3.app

import io.github.mat3e.ddd.event.DomainEventPublisher
import io.github.mat3e.fairytales.pigs3.model.BigBadWolfService
import io.github.mat3e.fairytales.pigs3.model.HouseFactory
import io.github.mat3e.fairytales.pigs3.model.HouseRepository

// accepting just IO adapters
class ThreePigsApp(
    commandRepository: HouseRepository,
    eventPublisher: DomainEventPublisher,
    queryRepository: HouseQueryRepository
) {
    val commandHandler =
        ThreePigsCommandHandler(
            commandRepository,
            HouseFactory(),
            BigBadWolfService(eventPublisher)
        )

    val eventHandler = ThreePigsEventHandler(queryRepository, commandHandler)
}
