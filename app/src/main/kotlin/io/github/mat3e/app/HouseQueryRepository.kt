package io.github.mat3e.app

import io.github.mat3e.model.vo.HouseId
import io.github.mat3e.model.vo.Material
import io.github.mat3e.model.vo.Pig
import java.util.Optional

interface HouseQueryRepository {
    fun findClosestTo(houseId: HouseId): Optional<HouseId>

    fun findDirect(id: HouseId): Optional<HouseReadModel>
}

data class HouseReadModel(val id: Int, val material: Material, val pigs: List<Pig>) {
    fun isDestroyed(): Boolean = pigs.isEmpty()
}
