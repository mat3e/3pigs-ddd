package io.github.mat3e.model;

import io.github.mat3e.ddd.specification.Specification;
import io.github.mat3e.model.vo.Material;

import static io.github.mat3e.model.vo.Material.STRAW;
import static io.github.mat3e.model.vo.Material.WOOD;

class BlowingDownPossibility implements Specification<House> {
    private final ConstructionSpecification strawConstruction = new ConstructionSpecification(STRAW);
    private final ConstructionSpecification woodenConstruction = new ConstructionSpecification(WOOD);

    @Override
    public boolean test(final House houseToTest) {
        return strawConstruction.or(woodenConstruction).test(houseToTest);
    }
}

class ConstructionSpecification implements Specification<House> {
    private final Material originalResource;

    ConstructionSpecification(final Material material) {
        originalResource = material;
    }

    @Override
    public boolean test(final House houseToTest) {
        return originalResource == houseToTest.getSnapshot().material();
    }
}
