package io.github.mat3e.fairytales.pigs3.model

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static io.github.mat3e.fairytales.pigs3.model.vo.Material.*

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
