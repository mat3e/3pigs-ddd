package io.github.mat3e.fairytales.pigs3.app.command

import io.github.mat3e.fairytales.pigs3.model.vo.HouseId
import io.github.mat3e.fairytales.pigs3.model.vo.Pig

sealed class ThreePigsCommand {
    override fun toString(): String = javaClass.simpleName.toString()
}

data class BuildHouse(val owner: Pig) : ThreePigsCommand()

sealed class UpdateCommand(val id: HouseId) : ThreePigsCommand() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as UpdateCommand
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int = id.hashCode()
}

class BlowDown(id: HouseId) : UpdateCommand(id)

// just for internal events

internal class Enter(val pigs: Collection<Pig>, id: HouseId) : UpdateCommand(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Enter) return false
        if (!super.equals(other)) return false
        if (pigs != other.pigs) return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + pigs.hashCode()
        return result
    }
}

internal class ShareKnowledge(id: HouseId) : UpdateCommand(id)
