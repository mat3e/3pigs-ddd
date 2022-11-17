package io.github.mat3e.fairytales.redhood;

import io.github.mat3e.fairytales.redhood.query.Wolf;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.Optional;

class WolfAssert extends AbstractAssert<WolfAssert, Wolf> {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static void thenShouldNotExist(Optional<Wolf> potentialWolf) {
        Assertions.assertThat(potentialWolf).isEmpty();
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static WolfAssert expect(Optional<Wolf> potentialWolf) {
        return then(potentialWolf);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static WolfAssert then(Optional<Wolf> potentialWolf) {
        Assertions.assertThat(potentialWolf).isPresent();
        return new WolfAssert(potentialWolf.get());
    }

    static WolfAssert then(io.github.mat3e.fairytales.redhood.Wolf wolf) {
        return new WolfAssert(() -> wolf.getSnapshot().alreadyEatenPeople().stream().map(Enum::name).toList());
    }

    protected WolfAssert(Wolf wolf) {
        super(wolf, WolfAssert.class);

    }

    WolfAssert hasNotEatenAnyone() {
        Assertions.assertThat(actual.getEatenPeople()).isEmpty();
        return this;
    }

    WolfAssert hasEaten(Person... people) {
        Assertions.assertThat(actual.getEatenPeople()).map(Person::valueOf).containsExactly(people);
        return this;
    }
}
