package io.github.mat3e.model

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static io.github.mat3e.model.vo.Material.BRICKS
import static io.github.mat3e.model.vo.Material.STRAW
import static io.github.mat3e.model.vo.Material.WOOD
import static io.github.mat3e.model.vo.Pig.LAZY
import static io.github.mat3e.model.vo.Pig.NOT_LAZY
import static io.github.mat3e.model.vo.Pig.NOT_LAZY_ANYMORE
import static io.github.mat3e.model.vo.Pig.VERY_LAZY

class HouseFactorySpec extends Specification {
    @Subject
    def factory = new HouseFactory()

    @Unroll('#material house for a #motivation pig')
    def 'should build a proper, new house for a given pig'() {
        given:
        House house = factory.buildFor motivation

        expect:
        with(house.snapshot) {
            it.id().value() == 0
            it.material() == material
            it.pigs() == [motivation]
            it.events() == []
        }

        where:
        material | motivation
        STRAW    | VERY_LAZY
        WOOD     | LAZY
        BRICKS   | NOT_LAZY
        BRICKS   | NOT_LAZY_ANYMORE
    }
}
