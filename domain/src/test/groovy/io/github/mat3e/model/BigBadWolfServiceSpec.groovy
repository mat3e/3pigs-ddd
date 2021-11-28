package io.github.mat3e.model

import io.github.mat3e.ddd.event.DomainEventPublisher
import io.github.mat3e.model.event.WolfResignedFromAttacking
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static io.github.mat3e.model.vo.Material.BRICKS
import static io.github.mat3e.model.vo.Material.STRAW
import static io.github.mat3e.model.vo.Material.WOOD
import static io.github.mat3e.model.vo.Pig.LAZY

class BigBadWolfServiceSpec extends Specification implements HouseHelpers {
    def publisher = Mock(DomainEventPublisher)

    @Subject
    def wolf = new BigBadWolfService(publisher)

    @Unroll('#input')
    def 'should blow down house from'() {
        given:
        House house = houseFrom input
        house.letIn LAZY

        expect:
        house.snapshot.pigs().size() != 0

        when:
        wolf.blowDown house

        then:
        house.snapshot.pigs().size() == 0

        where:
        input << [STRAW, WOOD]
    }

    def 'should fail with house from BRICKS'() {
        when:
        wolf.blowDown houseFrom(BRICKS)

        then:
        1 * publisher.publish(_ as WolfResignedFromAttacking)
    }
}
