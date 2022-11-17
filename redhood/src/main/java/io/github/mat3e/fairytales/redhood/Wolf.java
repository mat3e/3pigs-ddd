package io.github.mat3e.fairytales.redhood;

import ch.qos.logback.classic.Level;
import io.github.mat3e.ddd.Aggregate;
import io.github.mat3e.ddd.vo.EntitySnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Supplier;

class Wolf implements Aggregate<Integer, Wolf.Snapshot> {
    private static final Logger logger = LoggerFactory.getLogger(Wolf.class);
    private Integer id;
    private final ActionPlan masterPlan;
    private final List<Person> eatenPeople = new ArrayList<>();

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
        if (plannedEatingOrder.isEmpty()) {
            logger.debug("He didn't have plans to eat anyone.");
        } else {
            logger.debug("His plan was to eat {}.", String.join(" and then ", plannedEatingOrder.stream().map(Object::toString).toList()));
        }
        this.masterPlan = new ActionPlan(inReversedOrder(plannedEatingOrder));
    }

    void meet(Person aPerson) {
        MeetingPolicy meeting = MeetingPolicyFactory.policyFor(masterPlan.plannedInteraction(), aPerson);
        /*if (meeting.hasFatalConsequences()) {
            // publish events?
            return;
        }*/
        Optional<Person> metPerson = meeting.run();
        metPerson.ifPresent(eatenPerson -> {
            eatenPeople.add(eatenPerson);
            logger.info("So he devoured {}!", eatenPerson);
        });
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
        return new Snapshot(id, masterPlan.getSnapshot(), eatenPeople);
    }

    private static List<Person> inReversedOrder(List<Person> plannedEatingOrder) {
        var order = new ArrayList<>(plannedEatingOrder);
        Collections.reverse(order);
        return order;
    }

    record Snapshot(Integer id, List<Person> plannedEatingOrder, List<Person> alreadyEatenPeople)
            implements EntitySnapshot<Integer> {
        Snapshot {
            plannedEatingOrder = List.copyOf(plannedEatingOrder);
            alreadyEatenPeople = List.copyOf(alreadyEatenPeople);
        }
    }
}
