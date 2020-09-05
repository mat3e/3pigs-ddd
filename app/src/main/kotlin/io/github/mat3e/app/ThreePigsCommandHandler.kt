package io.github.mat3e.app

import io.github.mat3e.app.command.BlowDown
import io.github.mat3e.app.command.BuildHouse
import io.github.mat3e.app.command.Enter
import io.github.mat3e.app.command.ShareKnowledge
import io.github.mat3e.app.command.ThreePigsCommand
import io.github.mat3e.app.command.UpdateCommand
import io.github.mat3e.model.BigBadWolfService
import io.github.mat3e.model.House
import io.github.mat3e.model.HouseFactory
import io.github.mat3e.model.HouseRepository
import io.github.mat3e.model.vo.HouseId
import io.github.mat3e.model.vo.HouseSnapshot
import io.github.mat3e.model.vo.Pig

// seems kotlin-allopen has no effect when annotations are added in the other module
open class ThreePigsCommandHandler(
        private val repository: HouseRepository,
        private val factory: HouseFactory,
        private val wolf: BigBadWolfService
) {
    open infix fun handle(command: ThreePigsCommand): HouseId {
        val house: House = when (command) {
            is BuildHouse -> command.run {
                val house: House = factory.buildFor(owner)
                repository.save(house)
            }
            is UpdateCommand -> command.run {
                repository.findById(id).map { house ->
                    val before: HouseSnapshot = house.snapshot
                    val changedHouse: House = handleUpdateCommand(house, command)
                    compareAndSave(before, changedHouse)
                    changedHouse
                }.orElseThrow { HouseNotFoundException(id) }
            }
        }
        return house.snapshot.id()
    }

    private fun handleUpdateCommand(house: House, command: UpdateCommand): House = when (command) {
        is BlowDown -> command.run {
            wolf.blowDown(house)
            house
        }
        is Enter -> command.run {
            pig enter house
            house
        }
        is ShareKnowledge -> command.run {
            house.runBrainstorming()
            house
        }
    }

    private fun compareAndSave(before: HouseSnapshot, after: House) {
        if (before != after.snapshot) {
            repository.save(after)
        }
    }
}

class HouseNotFoundException(houseId: HouseId) : RuntimeException("No house found with id: ${houseId.value()}")

private infix fun Pig.enter(house: House) = house.letIn(this)
