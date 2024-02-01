package io.github.mat3e.fairytales.redhood;

import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static io.github.mat3e.fairytales.redhood.Person.HUNTSMAN;
import static java.util.Objects.requireNonNull;

// @FunctionalInterface
sealed interface MeetingPolicy {
    Logger logger = LoggerFactory.getLogger(MeetingPolicy.class);

    Optional<Person> run();

    default boolean hasFatalConsequencesForWolf() {
        return false;
    }
}

class MeetingPolicyFactory {
    static MeetingPolicy policyFor(Person expectedPerson, @Nonnull Person actualPerson) {
        if (requireNonNull(actualPerson) == HUNTSMAN) {
            return new LoosingWolfPolicy();
        }
        if (expectedPerson != actualPerson) {
            return new UnplannedMeetingPolicy();
        }
        return new AsPlannedMeetingPolicy(actualPerson);
    }

    private MeetingPolicyFactory() {
    }

    private static final class LoosingWolfPolicy extends UnplannedMeetingPolicy {
        @Override
        public boolean hasFatalConsequencesForWolf() {
            logger.info("That one was totally unexpected by wolf. He was about being killed!");
            return true;
        }
    }

    private static sealed class UnplannedMeetingPolicy implements MeetingPolicy {
        @Override
        public Optional<Person> run() {
            logger.info("This meeting was not planned by wolf, he didn't manage to eat anyone!");
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
            logger.info("That was what he planned.");
            return Optional.of(metPerson);
        }
    }
}
