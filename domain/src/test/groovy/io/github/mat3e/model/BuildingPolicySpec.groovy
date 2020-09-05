package io.github.mat3e.model

import spock.lang.Specification
import spock.lang.Unroll

import static io.github.mat3e.model.vo.Material.BRICKS
import static io.github.mat3e.model.vo.Material.STRAW
import static io.github.mat3e.model.vo.Material.WOOD
import static io.github.mat3e.model.vo.Pig.LAZY
import static io.github.mat3e.model.vo.Pig.NOT_LAZY
import static io.github.mat3e.model.vo.Pig.NOT_LAZY_ANYMORE
import static io.github.mat3e.model.vo.Pig.VERY_LAZY

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
