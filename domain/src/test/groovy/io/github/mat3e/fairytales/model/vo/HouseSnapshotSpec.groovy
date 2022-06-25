package io.github.mat3e.fairytales.model.vo

import spock.lang.Specification

class HouseSnapshotSpec extends Specification {
    def 'should throw for null HouseId'() {
        when:
        new HouseSnapshot(null, Material.BRICKS, [])

        then:
        def e = thrown NullPointerException
        e.message.contains 'HouseId'
    }
}
