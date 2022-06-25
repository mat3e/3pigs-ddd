package io.github.mat3e.fairytales.pigs3.model

import io.github.mat3e.fairytales.pigs3.model.vo.HouseSnapshot
import io.github.mat3e.fairytales.pigs3.model.vo.Pig
import spock.lang.Specification
import spock.lang.Unroll

import static io.github.mat3e.fairytales.pigs3.model.vo.Material.BRICKS
import static io.github.mat3e.fairytales.pigs3.model.vo.Pig.*

class HouseSpec extends Specification implements HouseHelpers {
    def 'should restore House from snapshot'() {
        given:
        def material = BRICKS
        def pigs = [VERY_LAZY, LAZY]
        def snapshot = new HouseSnapshot(randomId(), material, pigs)

        when:
        def result = House.from snapshot

        then:
        with(result.snapshot) {
            it.id().value() != 0
            it.material() == material
            it.pigs() == pigs
            it.events() == []
        }
    }

    def 'should NOT restore when more than 3 pigs'() {
        given:
        def snapshot = new HouseSnapshot(randomId(), BRICKS, [LAZY, LAZY, LAZY, LAZY])

        when:
        House.from snapshot

        then:
        thrown House.TooManyPigsException
    }

    @Unroll('already with #initialTenants.size() pigs')
    def 'should let the pig in'() {
        given:
        House house = houseWith initialTenants

        when:
        house.letIn LAZY

        then:
        house.snapshot.pigs() contains LAZY

        where:
        // noinspection GroovyAssignabilityCheck
        initialTenants << [[], [NOT_LAZY_ANYMORE], [NOT_LAZY_ANYMORE, NOT_LAZY_ANYMORE]]
    }

    def 'should NOT let the pig in if already has 3 pigs'() {
        given:
        House house = houseWith([NOT_LAZY, NOT_LAZY_ANYMORE, NOT_LAZY_ANYMORE])

        when:
        house.letIn LAZY

        then:
        def e = thrown House.TooManyPigsException
        e.message.contains '3'
    }

    @Unroll('#pigs -> #smarterPigs')
    def 'should have smart pigs after a brainstorming session'() {
        given:
        House house = houseWith pigs

        when:
        house.runBrainstorming()

        then:
        house.snapshot.pigs() == smarterPigs

        where:
        pigs                               | smarterPigs
        [LAZY, VERY_LAZY, LAZY]            | [NOT_LAZY_ANYMORE, NOT_LAZY_ANYMORE, NOT_LAZY_ANYMORE]
        [NOT_LAZY_ANYMORE, LAZY, NOT_LAZY] | [NOT_LAZY_ANYMORE, NOT_LAZY_ANYMORE, NOT_LAZY]
    }

    def 'should throw when handling hurricane and from BRICKS'() {
        given:
        def house = House.from new HouseSnapshot(randomId(), BRICKS, [NOT_LAZY])

        when:
        house.handleHurricane BlowingDownSpecification.defaultSpec()

        then:
        thrown House.IndestructibleHouseException
    }

    private static House houseWith(List<Pig> pigs) {
        House.from new HouseSnapshot(randomId(), BRICKS, pigs)
    }
}
