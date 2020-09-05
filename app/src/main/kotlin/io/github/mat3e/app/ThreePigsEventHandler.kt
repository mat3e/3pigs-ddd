package io.github.mat3e.app

import io.github.mat3e.app.command.Enter
import io.github.mat3e.app.command.ShareKnowledge
import io.github.mat3e.model.event.HouseAbandoned
import io.github.mat3e.model.event.HouseEvent
import io.github.mat3e.model.event.WolfResignedFromAttacking

class ThreePigsEventHandler(
        private val queryRepository: HouseQueryRepository,
        private val commandHandler: ThreePigsCommandHandler
) {
    fun handle(event: HouseEvent) {
        when (event) {
            is HouseAbandoned -> event.run {
                queryRepository.findClosestTo(house()).ifPresent { closestHouseId ->
                    refugees().forEach { pig ->
                        commandHandler handle Enter(pig, closestHouseId)
                    }
                }
                // doesn't enter any house ~ eaten
            }
            is WolfResignedFromAttacking -> commandHandler handle ShareKnowledge(event.house())
        }
    }
}
