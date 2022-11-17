package io.github.mat3e.fairytales.redhood;

import org.junit.jupiter.api.Test;

import java.util.List;

import static io.github.mat3e.fairytales.redhood.Person.GRANDMA;
import static io.github.mat3e.fairytales.redhood.Person.HUNTSMAN;
import static io.github.mat3e.fairytales.redhood.WolfAssert.then;

class WolfTest {
    @Test
    void wolfWithoutPlan_doesNotEat() {
        // given
        var wolf = new Wolf(List.of());

        // when
        wolf.meet(GRANDMA);

        then(wolf).hasNotEatenAnyone();
    }

    @Test
    void wolfWithPlan_eatsPlannedPerson() {
        // given
        var wolf = new Wolf(List.of(GRANDMA));

        // when
        wolf.meet(GRANDMA);

        then(wolf).hasEaten(GRANDMA);
    }

    @Test
    void wolfMeetingHuntsman_dies_cannotMeetAnyoneElse() {
        // given
        var wolf = new Wolf(List.of(GRANDMA, GRANDMA, GRANDMA));

        // when
        wolf.meet(GRANDMA);

        then(wolf).hasEaten(GRANDMA);

        // when
        wolf.meet(HUNTSMAN);

        // belly cut and people escaped
        then(wolf).hasNotEatenAnyone();

        // when
        wolf.meet(GRANDMA);

        then(wolf).hasNotEatenAnyone();
    }
}
