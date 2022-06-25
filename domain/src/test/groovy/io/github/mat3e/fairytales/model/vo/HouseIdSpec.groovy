package io.github.mat3e.fairytales.model.vo

import spock.lang.Specification

class HouseIdSpec extends Specification {
    def 'should wrap null if created as an empty HouseId'() {
        when:
        def result = HouseId.empty()

        then:
        result.value() == 0
    }
}
