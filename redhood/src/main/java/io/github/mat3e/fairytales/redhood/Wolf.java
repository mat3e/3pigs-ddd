package io.github.mat3e.fairytales.redhood;

import io.github.mat3e.ddd.Aggregate;
import io.github.mat3e.ddd.event.DomainEvent;
import io.github.mat3e.ddd.event.SnapshotWithEvents;
import io.github.mat3e.fairytales.redhood.event.WolfEvent;
import io.github.mat3e.fairytales.redhood.event.WolfKilled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Stack;

import static java.util.stream.Collectors.joining;
import static org.springframework.data.annotation.AccessType.Type.PROPERTY;

@Table("wolfs")
@AccessType(PROPERTY)
class Wolf implements Aggregate<Integer, Wolf.Snapshot> {
    private static final Logger logger = LoggerFactory.getLogger(Wolf.class);
    private @Id Integer id;
    private final @Transient ActionPlan masterPlan;
    private final @Transient List<Person> alreadyEatenPeople = new ArrayList<>();
    private @Transient boolean killed;
    private final @Transient List<WolfEvent> events = new ArrayList<>();

    static Wolf fromSnapshot(Snapshot snapshot) {
        var result = new Wolf(snapshot.plannedEatingOrder(), true);
        result.id = snapshot.id();
        result.alreadyEatenPeople.addAll(snapshot.alreadyEatenPeople());
        return result;
    }

    Wolf(List<Person> plannedEatingOrder) {
        this(plannedEatingOrder, false);
    }

    private Wolf(List<Person> plannedEatingOrder, boolean skipLogging) {
        if (!skipLogging) {
            logCreation(plannedEatingOrder);
        }
        this.masterPlan = new ActionPlan(inReversedOrder(plannedEatingOrder));
    }

    private void logCreation(List<Person> plannedEatingOrder) {
        if (plannedEatingOrder.isEmpty()) {
            logger.info("He didn't have plans to eat anyone.");
            return;
        }
        logger.info(
                "His plan was to eat {}.",
                String.join(" and then ", plannedEatingOrder.stream().map(Object::toString).toList()));
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
            alreadyEatenPeople.add(eatenPerson);
            logger.info("So he ate {} all up!", eatenPerson);
        });
    }

    private void die() {
        if (id != null) {
            events.add(new WolfKilled(id, alreadyEatenPeople));
        }
        alreadyEatenPeople.clear();
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
    @Transient
    public Snapshot getSnapshot() {
        return new Snapshot(id, masterPlan.getSnapshot(), alreadyEatenPeople, events);
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

    // JDBC
    public String getPlannedOrder() {
        return stringify(masterPlan.getSnapshot());
    }

    // todo: still public in newer Data-JDBC?
    public String getEatenPeople() {
        return stringify(alreadyEatenPeople);
    }

    private String stringify(List<Person> peopleList) {
        return peopleList.stream().map(Enum::name).collect(joining(","));
    }

    @PersistenceCreator
    Wolf(Integer id, List<String> plannedOrder, List<String> eatenPeople) {
        this(fromStrings(plannedOrder), true);
        this.id = id;
        this.alreadyEatenPeople.addAll(fromStrings(eatenPeople));
    }

    private static List<Person> fromStrings(List<String> stringList) {
        return stringList == null ? List.of() : stringList.stream().map(Person::valueOf).toList();
    }
}
