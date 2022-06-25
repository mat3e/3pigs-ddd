package io.github.mat3e.fairytales.model;

import io.github.mat3e.fairytales.model.vo.HouseId;
import io.github.mat3e.fairytales.model.vo.HouseSnapshot;
import io.github.mat3e.fairytales.model.vo.Material;
import io.github.mat3e.fairytales.model.vo.Pig;

import java.util.List;

public class HouseFactory {
    public House buildFor(Pig pig) {
        Material resource = BuildingPolicy.chooseFor(pig).chooseMaterial();
        return House.from(
                new HouseSnapshot(
                        HouseId.empty(),
                        resource,
                        List.of(pig)
                )
        );
    }
}
