package io.github.mat3e.app.command

import io.github.mat3e.model.vo.HouseId
import io.github.mat3e.model.vo.Pig

sealed class ThreePigsCommand {
    override fun toString(): String = javaClass.simpleName.toString()
}

data class BuildHouse(val owner: Pig) : ThreePigsCommand()

sealed class UpdateCommand(val id: HouseId) : ThreePigsCommand() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UpdateCommand) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int = id.hashCode()
}

class BlowDown(id: HouseId) : UpdateCommand(id)

class Enter(val pig: Pig, id: HouseId) : UpdateCommand(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Enter) return false
        if (!super.equals(other)) return false
        if (pig != other.pig) return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + pig.hashCode()
        return result
    }
}

class ShareKnowledge(id: HouseId) : UpdateCommand(id)
