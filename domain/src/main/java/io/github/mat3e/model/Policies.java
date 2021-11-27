package io.github.mat3e.model;

import io.github.mat3e.model.vo.Material;
import io.github.mat3e.model.vo.Pig;

sealed interface BuildingPolicy {
    static BuildingPolicy chooseFor(Pig pig) {
        return switch (pig) {
            case VERY_LAZY -> new VeryLazyBuildingPolicy();
            case LAZY -> new LazyBuildingPolicy();
            case NOT_LAZY, NOT_LAZY_ANYMORE -> new SolidBuildingPolicy();
        };
    }

    Material chooseMaterial();
}

final class VeryLazyBuildingPolicy implements BuildingPolicy {
    @Override
    public Material chooseMaterial() {
        return Material.STRAW;
    }
}

final class LazyBuildingPolicy implements BuildingPolicy {
    @Override
    public Material chooseMaterial() {
        return Material.WOOD;
    }
}

final class SolidBuildingPolicy implements BuildingPolicy {
    @Override
    public Material chooseMaterial() {
        return Material.BRICKS;
    }
}
