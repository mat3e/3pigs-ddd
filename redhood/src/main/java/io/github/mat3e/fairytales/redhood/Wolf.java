package io.github.mat3e.fairytales.redhood;

import ch.qos.logback.classic.Level;
import io.github.mat3e.ddd.Aggregate;
import io.github.mat3e.ddd.event.DomainEvent;
import io.github.mat3e.ddd.event.SnapshotWithEvents;
import io.github.mat3e.fairytales.redhood.event.WolfEvent;
import io.github.mat3e.fairytales.redhood.event.WolfKilled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Supplier;

class Wolf implements Aggregate<Integer, Wolf.Snapshot> {
    private static final Logger logger = LoggerFactory.getLogger(Wolf.class);
    private Integer id;
    private final ActionPlan masterPlan;
    private final List<Person> eatenPeople = new ArrayList<>();
    private boolean killed;
    private final List<WolfEvent> events = new ArrayList<>();

    static Wolf fromSnapshot(Snapshot snapshot) {
        return withInfoLogLevel(() -> {
            var result = new Wolf(snapshot.plannedEatingOrder());
            result.id = snapshot.id();
            result.eatenPeople.addAll(snapshot.alreadyEatenPeople());
            return result;
        });
    }

    private static <T> T withInfoLogLevel(Supplier<T> instruction) {
        var log = ((ch.qos.logback.classic.Logger) logger);
        var originalLevel = log.getLevel();
        log.setLevel(Level.INFO);
        var result = instruction.get();
        log.setLevel(originalLevel);
        return result;
    }

    Wolf(List<Person> plannedEatingOrder) {
        logCreation(plannedEatingOrder);
        this.masterPlan = new ActionPlan(inReversedOrder(plannedEatingOrder));
    }

    private void logCreation(List<Person> plannedEatingOrder) {
        if (plannedEatingOrder.isEmpty()) {
            logger.debug("He didn't have plans to eat anyone.");
            return;
        }
        logger.debug("His plan was to eat {}.", String.join(" and then ", plannedEatingOrder.stream().map(Object::toString).toList()));
    }

    void meet(Person aPerson) {
        if (killed) {
            return;
        }
        MeetingPolicy meeting = MeetingPolicyFactory.policyFor(masterPlan.plannedInteraction(), aPerson);
        if (meeting.hasFatalConsequencesForWolf()) {
            die();
            return;
        }
        Optional<Person> metPerson = meeting.run();
        metPerson.ifPresent(eatenPerson -> {
            eatenPeople.add(eatenPerson);
            logger.info("So he ate {} all up!", eatenPerson);
        });
    }

    private void die() {
        if (id != null) {
            events.add(new WolfKilled(id, eatenPeople));
        }
        eatenPeople.clear();
        killed = true;
    }

    private static class ActionPlan {
        private final Stack<Person> toEat = new Stack<>();

        ActionPlan(List<Person> eatingOrder) {
            toEat.addAll(eatingOrder);
        }

        Person plannedInteraction() {
            if (toEat.empty()) {
                return null;
            }
            return toEat.pop();
        }

        List<Person> getSnapshot() {
            return inReversedOrder(toEat);
        }
    }

    @Override
    public Snapshot getSnapshot() {
        return new Snapshot(id, masterPlan.getSnapshot(), eatenPeople, events);
    }

    private static List<Person> inReversedOrder(List<Person> plannedEatingOrder) {
        var order = new ArrayList<>(plannedEatingOrder);
        Collections.reverse(order);
        return order;
    }

    record Snapshot(
            Integer id,
            List<Person> plannedEatingOrder,
            List<Person> alreadyEatenPeople,
            List<? extends DomainEvent> events
    ) implements SnapshotWithEvents<Integer> {

        Snapshot(Integer id, List<Person> plannedEatingOrder, List<Person> alreadyEatenPeople) {
            this(id, plannedEatingOrder, alreadyEatenPeople, List.of());
        }

        Snapshot {
            plannedEatingOrder = List.copyOf(plannedEatingOrder);
            alreadyEatenPeople = List.copyOf(alreadyEatenPeople);
            events = List.copyOf(events);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Snapshot snapshot = (Snapshot) o;
            return Objects.equals(id, snapshot.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    static class NotFound extends RuntimeException {
        NotFound(int wolfId) {
            super("Wolf " + wolfId + " not found!");
        }
    }
}
