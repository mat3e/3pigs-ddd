package io.github.mat3e.model

import io.github.mat3e.ddd.event.DomainEventPublisher
import io.github.mat3e.model.event.HouseAbandoned
import io.github.mat3e.model.event.WolfResignedFromAttacking
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.time.Instant

import static io.github.mat3e.model.vo.Material.*
import static io.github.mat3e.model.vo.Pig.LAZY

class BigBadWolfServiceSpec extends Specification implements HouseHelpers {
    def publisher = Mock(DomainEventPublisher)

    @Subject
    def wolf = new BigBadWolfService(publisher)

    @Unroll('#input')
    def 'should blow down house from'() {
        given:
        def before = Instant.now()
        House house = houseFrom input
        house.letIn LAZY

        expect:
        house.snapshot.pigs().size() != 0

        when:
        wolf.blowDown house

        then:
        with(house.snapshot) {
            it.pigs().size() == 0
            with(it.events()) {
                size() == 1
                it[0].occurredOn() isAfter before
                it[0].occurredOn() isBefore Instant.now()
                it[0] instanceof HouseAbandoned
                (it[0] as HouseAbandoned).refugees() == [LAZY]
            }
        }

        where:
        input << [STRAW, WOOD]
    }

    def 'should fail with house from BRICKS'() {
        when:
        wolf.blowDown houseFrom(BRICKS)

        then:
        1 * publisher.publish(_ as WolfResignedFromAttacking)
    }

    def 'should blow down house from BRICKS with an overridden specification'() {
        given:
        def before = Instant.now()
        House house = houseFrom BRICKS
        house.letIn LAZY
        and:
        wolf = new BigBadWolfService(publisher, { houseToTest -> true })

        when:
        wolf.blowDown(house)

        then:
        with(house.snapshot) {
            it.pigs().size() == 0
            with(it.events()) {
                size() == 1
                it[0].occurredOn() isAfter before
                it[0].occurredOn() isBefore Instant.now()
                it[0] instanceof HouseAbandoned
                (it[0] as HouseAbandoned).refugees() == [LAZY]
            }
        }

        cleanup:
        wolf = new BigBadWolfService(publisher)
    }
}
