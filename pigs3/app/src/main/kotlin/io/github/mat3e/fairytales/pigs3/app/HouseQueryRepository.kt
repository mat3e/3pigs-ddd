package io.github.mat3e.fairytales.pigs3.app

import io.github.mat3e.fairytales.pigs3.model.vo.HouseId
import io.github.mat3e.fairytales.pigs3.model.vo.Material
import io.github.mat3e.fairytales.pigs3.model.vo.Pig
import java.util.*

interface HouseQueryRepository {
    fun findClosestTo(houseId: HouseId): Optional<HouseId>

    fun findDirect(id: HouseId): Optional<HouseReadModel>
}

data class HouseReadModel(val id: Int, val material: Material, val pigs: List<Pig>) {
    fun isDestroyed(): Boolean = pigs.isEmpty()
}
