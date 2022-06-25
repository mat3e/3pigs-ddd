package io.github.mat3e.fairytales.pigs3.model.vo

import spock.lang.Specification
import spock.lang.Unroll

import static io.github.mat3e.fairytales.pigs3.model.vo.Pig.*

class PigSpec extends Specification {
    @Unroll('from #start to #end')
    def 'should change their mind'() {
        expect:
        Pig.learnFromMistakes(start) == end

        where:
        start     | end
        VERY_LAZY | NOT_LAZY_ANYMORE
        LAZY      | NOT_LAZY_ANYMORE
    }

    @Unroll('#pig')
    def 'should remain unchanged'() {
        expect:
        Pig.learnFromMistakes(pig) == pig

        where:
        pig << [NOT_LAZY, NOT_LAZY_ANYMORE, null]
    }
}
