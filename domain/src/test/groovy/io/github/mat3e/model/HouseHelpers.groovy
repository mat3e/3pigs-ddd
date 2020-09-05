package io.github.mat3e.model

import groovy.transform.PackageScope
import groovy.transform.TypeChecked
import io.github.mat3e.model.vo.HouseId
import io.github.mat3e.model.vo.HouseSnapshot
import io.github.mat3e.model.vo.Material

@TypeChecked
@PackageScope
trait HouseHelpers {
    static House houseFrom(Material material) {
        return House.from(new HouseSnapshot(randomId(), material, [], []))
    }

    static HouseId randomId() {
        HouseId.of(new Random().nextInt(100) + 1)
    }
}