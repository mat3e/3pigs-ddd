package io.github.mat3e.fairytales.pigs3.model.vo

import spock.lang.Specification

class HouseIdSpec extends Specification {
    def 'should wrap 0 if created as an empty HouseId'() {
        when:
        def result = HouseId.empty()

        then:
        result.value() == 0
    }
}
