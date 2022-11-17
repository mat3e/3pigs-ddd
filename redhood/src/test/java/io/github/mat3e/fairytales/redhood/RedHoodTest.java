package io.github.mat3e.fairytales.redhood;

import io.github.mat3e.fairytales.redhood.query.InMemoryWolfQueryRepository;
import io.github.mat3e.fairytales.redhood.query.RedHoodQuery;
import org.junit.jupiter.api.Test;

import static io.github.mat3e.fairytales.redhood.Person.GRANDMA;
import static io.github.mat3e.fairytales.redhood.Person.RED_HOOD;
import static io.github.mat3e.fairytales.redhood.WolfAssert.then;

class RedHoodTest {
    private final RedHoodCommandHandler command;
    private final RedHoodQuery query;

    RedHoodTest() {
        var repo = new InMemoryWolfRepository(wolf -> wolf.getSnapshot().id());
        command = new RedHoodConfiguration(repo).commandHandler();
        query = new RedHoodQuery(new InMemoryWolfQueryRepository(repo));
    }

    @Test
    void newWolf_notEatenAnyoneYet() {
        // when
        int wolfId = command.startInteracting();

        then(query.findById(wolfId)).hasNotEatenAnyone();
    }

    @Test
    void wolfWithoutPlan_doesNotEat() {
        // given
        int wolfId = command.startInteracting();

        // when
        command.meetWolf(RED_HOOD, wolfId);

        then(query.findById(wolfId)).hasNotEatenAnyone();
    }

    @Test
    void wolfWithPlan_eatsPlannedPerson() {
        // given
        int wolfId = command.startInteracting(RED_HOOD);

        // when
        command.meetWolf(RED_HOOD, wolfId);

        then(query.findById(wolfId)).hasEaten(RED_HOOD);
    }

    @Test
    void wolfWithPlan_eatsAllPlannedPeople() {
        // given
        int wolfId = command.startInteracting(RED_HOOD, GRANDMA, GRANDMA);

        // when
        command.meetWolf(RED_HOOD, wolfId);
        // and
        command.meetWolf(GRANDMA, wolfId);
        // and
        command.meetWolf(GRANDMA, wolfId);

        then(query.findById(wolfId)).hasEaten(RED_HOOD, GRANDMA, GRANDMA);
    }
}
