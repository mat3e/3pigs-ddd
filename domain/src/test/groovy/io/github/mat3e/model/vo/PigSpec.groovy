package io.github.mat3e.model.vo

import spock.lang.Specification
import spock.lang.Unroll

import static io.github.mat3e.model.vo.Pig.LAZY
import static io.github.mat3e.model.vo.Pig.NOT_LAZY
import static io.github.mat3e.model.vo.Pig.NOT_LAZY_ANYMORE
import static io.github.mat3e.model.vo.Pig.VERY_LAZY

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
