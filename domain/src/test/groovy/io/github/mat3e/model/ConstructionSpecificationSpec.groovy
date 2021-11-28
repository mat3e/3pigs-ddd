package io.github.mat3e.model

import spock.lang.Specification
import spock.lang.Unroll

import static io.github.mat3e.model.vo.Material.BRICKS
import static io.github.mat3e.model.vo.Material.STRAW
import static io.github.mat3e.model.vo.Material.WOOD

class ConstructionSpecificationSpec extends Specification implements HouseHelpers {
    @Unroll('#inputMaterial')
    def 'should pass for material as in specification'() {
        given:
        House house = houseFrom inputMaterial

        expect:
        new ConstructionSpecification(inputMaterial).isSatisfiedBy(house)

        where:
        inputMaterial << [BRICKS, STRAW, WOOD]
    }

    @Unroll('house from #inputMaterial vs. #resource')
    def 'should fail for material different than in specification'() {
        given:
        House house = houseFrom inputMaterial

        expect:
        !new ConstructionSpecification(resource).isSatisfiedBy(house)

        where:
        resource | inputMaterial
        STRAW    | BRICKS
        WOOD     | STRAW
        STRAW    | WOOD
    }
}
