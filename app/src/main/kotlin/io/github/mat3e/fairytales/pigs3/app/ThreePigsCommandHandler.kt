package io.github.mat3e.fairytales.pigs3.app

import io.github.mat3e.fairytales.pigs3.app.command.*
import io.github.mat3e.fairytales.pigs3.model.BigBadWolfService
import io.github.mat3e.fairytales.pigs3.model.House
import io.github.mat3e.fairytales.pigs3.model.HouseFactory
import io.github.mat3e.fairytales.pigs3.model.HouseRepository
import io.github.mat3e.fairytales.pigs3.model.vo.HouseId
import io.github.mat3e.fairytales.pigs3.model.vo.HouseSnapshot
import io.github.mat3e.fairytales.pigs3.model.vo.Pig

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
            pigs.forEach { pig -> pig enter house }
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
