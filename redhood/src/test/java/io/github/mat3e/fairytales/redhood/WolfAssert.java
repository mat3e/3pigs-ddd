package io.github.mat3e.fairytales.redhood;

import io.github.mat3e.fairytales.redhood.query.Wolf;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.Optional;

class WolfAssert extends AbstractAssert<WolfAssert, Wolf> {
    protected WolfAssert(Wolf wolf) {
        super(wolf, WolfAssert.class);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static WolfAssert then(Optional<Wolf> potentialWolf) {
        Assertions.assertThat(potentialWolf).isPresent();
        return new WolfAssert(potentialWolf.get());
    }

    WolfAssert hasNotEatenAnyone() {
        Assertions.assertThat(actual.getEatenPeople()).isEmpty();
        return this;
    }

    WolfAssert hasEaten(Person... people) {
        Assertions.assertThat(actual.getEatenPeople()).containsExactly(people);
        return this;
    }
}
