package io.github.mat3e.fairytales.app

import io.github.mat3e.fairytales.app.command.Enter
import io.github.mat3e.fairytales.app.command.ShareKnowledge
import io.github.mat3e.fairytales.model.event.HouseAbandoned
import io.github.mat3e.fairytales.model.event.HouseEvent
import io.github.mat3e.fairytales.model.event.WolfResignedFromAttacking

class ThreePigsEventHandler(
    private val queryRepository: HouseQueryRepository,
    private val commandHandler: ThreePigsCommandHandler
) {
    fun handle(event: HouseEvent) {
        when (event) {
            is HouseAbandoned -> event.run {
                queryRepository.findClosestTo(house).ifPresent { commandHandler handle Enter(refugees, it) }
                // don't enter any house ~ eaten
            }
            is WolfResignedFromAttacking -> event.run { commandHandler handle ShareKnowledge(house) }
        }
    }
}
