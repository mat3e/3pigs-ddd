package io.github.mat3e.app

import io.github.mat3e.model.vo.HouseId
import java.util.Optional

interface HouseQueryRepository {
    fun findClosestTo(houseId: HouseId): Optional<HouseId>
}