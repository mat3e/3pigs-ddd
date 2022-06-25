package io.github.mat3e.fairytales.model

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static io.github.mat3e.fairytales.model.vo.Material.*
import static io.github.mat3e.fairytales.model.vo.Pig.*

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
