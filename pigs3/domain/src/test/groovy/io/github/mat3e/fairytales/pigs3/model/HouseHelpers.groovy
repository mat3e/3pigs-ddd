package io.github.mat3e.fairytales.pigs3.model

import groovy.transform.TypeChecked
import io.github.mat3e.fairytales.pigs3.model.vo.HouseId
import io.github.mat3e.fairytales.pigs3.model.vo.HouseSnapshot
import io.github.mat3e.fairytales.pigs3.model.vo.Material

@TypeChecked
trait HouseHelpers {
    static House houseFrom(Material material) {
        House.from new HouseSnapshot(randomId(), material, [], [])
    }

    static HouseId randomId() {
        HouseId.of(new Random().nextInt(100) + 1)
    }
}
