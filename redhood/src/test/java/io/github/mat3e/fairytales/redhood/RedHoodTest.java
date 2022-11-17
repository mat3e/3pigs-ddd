package io.github.mat3e.fairytales.redhood;

import io.github.mat3e.ddd.event.DomainEvent;
import io.github.mat3e.ddd.event.DomainEventPublisher;
import io.github.mat3e.fairytales.redhood.query.InMemoryWolfQueryRepository;
import io.github.mat3e.fairytales.redhood.query.RedHoodQuery;
import org.junit.jupiter.api.Test;

import static io.github.mat3e.fairytales.redhood.Person.*;
import static io.github.mat3e.fairytales.redhood.WolfAssert.*;

class RedHoodTest {
    private final RedHoodService command;
    private final RedHoodQuery query;

    RedHoodTest() {
        var publisher = new DomainEventPublisher() {
            @Override
            public void publish(DomainEvent event) {
                command.onEvent(event);
            }
        };
        var repo = new InMemoryWolfRepository(wolf -> wolf.getSnapshot().id());
        command = new RedHoodConfiguration(repo, publisher).redHoodService();
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

    @Test
    void wolfWithPlan_differentMeetings_eatsJustSome() {
        // given
        int wolfId = command.startInteracting(RED_HOOD, GRANDMA, GRANDMA, RED_HOOD);

        // when mismatching
        command.meetWolf(GRANDMA, wolfId);
        // and
        command.meetWolf(RED_HOOD, wolfId);
        // and matching
        command.meetWolf(GRANDMA, wolfId);
        // and
        command.meetWolf(RED_HOOD, wolfId);

        then(query.findById(wolfId)).hasEaten(GRANDMA, RED_HOOD);
    }

    @Test
    void wolfMeetingHuntsman_stopsExisting() {
        // given
        int wolfId = command.startInteracting(RED_HOOD, GRANDMA, RED_HOOD, GRANDMA);
        // and
        command.meetWolf(RED_HOOD, wolfId);
        // and
        command.meetWolf(GRANDMA, wolfId);

        expect(query.findById(wolfId)).hasEaten(RED_HOOD, GRANDMA);

        // when
        command.meetWolf(HUNTSMAN, wolfId);

        thenShouldNotExist(query.findById(wolfId));
    }
}
