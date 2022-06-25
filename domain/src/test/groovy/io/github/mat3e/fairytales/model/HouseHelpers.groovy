package io.github.mat3e.fairytales.model

import groovy.transform.TypeChecked

@TypeChecked
trait HouseHelpers {
    static House houseFrom(io.github.mat3e.fairytales.model.vo.Material material) {
        House.from new io.github.mat3e.fairytales.model.vo.HouseSnapshot(randomId(), material, [], [])
    }

    static io.github.mat3e.fairytales.model.vo.HouseId randomId() {
        io.github.mat3e.fairytales.model.vo.HouseId.of(new Random().nextInt(100) + 1)
    }
}
