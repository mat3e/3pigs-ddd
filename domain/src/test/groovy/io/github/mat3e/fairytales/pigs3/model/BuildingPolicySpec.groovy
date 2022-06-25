package io.github.mat3e.fairytales.pigs3.model

import spock.lang.Specification
import spock.lang.Unroll

import static io.github.mat3e.fairytales.pigs3.model.vo.Material.*
import static io.github.mat3e.fairytales.pigs3.model.vo.Pig.*

class BuildingPolicySpec extends Specification {
    @Unroll('#material for a #motivation pig')
    def 'should choose a proper material for a given pig'() {
        given:
        def policy = BuildingPolicy.chooseFor motivation

        expect:
        policy.chooseMaterial() == material

        where:
        material | motivation
        STRAW    | VERY_LAZY
        WOOD     | LAZY
        BRICKS   | NOT_LAZY
        BRICKS   | NOT_LAZY_ANYMORE
    }
}
