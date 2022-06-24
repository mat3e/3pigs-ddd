package io.github.mat3e.model

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static io.github.mat3e.model.vo.Material.BRICKS
import static io.github.mat3e.model.vo.Material.STRAW
import static io.github.mat3e.model.vo.Material.WOOD

class DefaultBlowingDownSpecificationSpec extends Specification implements HouseHelpers {
    @Subject
    def specification = BlowingDownSpecification.defaultSpec()

    @Unroll('#inputMaterial')
    def 'should pass for'() {
        expect:
        specification.isSatisfiedBy houseFrom(inputMaterial)

        where:
        inputMaterial << [STRAW, WOOD]
    }

    def 'should fail for BRICKS'() {
        expect:
        !specification.isSatisfiedBy(houseFrom(BRICKS))
    }
}
