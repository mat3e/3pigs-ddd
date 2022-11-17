package io.github.mat3e.fairytales.redhood;

import jakarta.annotation.Nonnull;

import java.util.Optional;

import static io.github.mat3e.fairytales.redhood.Person.HUNTSMAN;
import static java.util.Objects.requireNonNull;

// @FunctionalInterface
sealed interface MeetingPolicy {
    Optional<Person> run();
}

class MeetingPolicyFactory {
    // todo: fluent api?
    //  Meetings.with(actualPerson).plannedFor(expectedPerson)
    //  Meetings.plannedFor(expectedPerson).with(actualPerson)
    static MeetingPolicy policyFor(Person expectedPerson, @Nonnull Person actualPerson) {
        if (requireNonNull(actualPerson) == HUNTSMAN) {
            return new LoosingWolfPolicy();
        }
        if (expectedPerson == null || expectedPerson != actualPerson) {
            return new UnplannedMeetingPolicy();
        }
        return new AsPlannedMeetingPolicy(actualPerson);
    }

    private MeetingPolicyFactory() {
    }

    private static final class LoosingWolfPolicy extends UnplannedMeetingPolicy {
        // wolf dies
    }

    private static sealed class UnplannedMeetingPolicy implements MeetingPolicy {
        @Override
        public Optional<Person> run() {
            // no one eaten, person run away!
            return Optional.empty();
        }
    }

    private static final class AsPlannedMeetingPolicy implements MeetingPolicy {
        private final Person metPerson;

        AsPlannedMeetingPolicy(Person metPerson) {
            this.metPerson = metPerson;
        }

        @Override
        public Optional<Person> run() {
            // todo: logging here that according to plan, wolf dressed, bla, bla
            return Optional.of(metPerson);
        }
    }
}
