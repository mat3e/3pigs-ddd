package io.github.mat3e.model

import io.github.mat3e.model.event.HouseAbandoned
import io.github.mat3e.model.vo.HouseSnapshot
import io.github.mat3e.model.vo.Pig
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Instant

import static io.github.mat3e.model.vo.Material.BRICKS
import static io.github.mat3e.model.vo.Material.STRAW
import static io.github.mat3e.model.vo.Material.WOOD
import static io.github.mat3e.model.vo.Pig.LAZY
import static io.github.mat3e.model.vo.Pig.NOT_LAZY
import static io.github.mat3e.model.vo.Pig.NOT_LAZY_ANYMORE
import static io.github.mat3e.model.vo.Pig.VERY_LAZY

class HouseSpec extends Specification implements HouseHelpers {
    def 'should restore House from snapshot'() {
        given:
        def material = BRICKS
        def pigs = [VERY_LAZY, LAZY]
        def snapshot = new HouseSnapshot(randomId(), material, pigs)

        when:
        House result = House.from snapshot

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
        house.snapshot.pigs().contains(LAZY)

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

    @Unroll('#material')
    def 'should get blown down when from'() {
        given:
        def id = randomId()
        def house = House.from(new HouseSnapshot(id, material, [LAZY]))
        def before = Instant.now()

        when:
        house.handleHurricane()

        then:
        with(house.snapshot) {
            it.pigs().size() == 0
            with(it.events()) {
                size() == 1
                it[0].occurredOn().isAfter(before)
                it[0].occurredOn().isBefore(Instant.now())
                it[0] instanceof HouseAbandoned
                (it[0] as HouseAbandoned).house() == id
                (it[0] as HouseAbandoned).refugees() == [LAZY]
            }
        }

        where:
        material << [STRAW, WOOD]
    }

    def 'should NOT get blown down when from BRICKS'() {
        given:
        House house = House.from(new HouseSnapshot(randomId(), BRICKS, [NOT_LAZY]))

        when:
        house.handleHurricane()

        then:
        thrown House.IndestructibleHouseException
    }

    private static House houseWith(List<Pig> pigs) {
        House.from(new HouseSnapshot(randomId(), BRICKS, pigs))
    }
}
