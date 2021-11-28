package io.github.mat3e.model;

import io.github.mat3e.ddd.Aggregate;
import io.github.mat3e.model.event.HouseAbandoned;
import io.github.mat3e.model.event.HouseEvent;
import io.github.mat3e.model.vo.HouseId;
import io.github.mat3e.model.vo.HouseSnapshot;
import io.github.mat3e.model.vo.Material;
import io.github.mat3e.model.vo.Pig;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toUnmodifiableList;

public class House implements Aggregate<HouseId, HouseSnapshot> {
    static BlowingDownPossibility CAN_BE_BLOWN_DOWN = new BlowingDownPossibility();

    static House from(final HouseSnapshot snapshot) {
        return new House(snapshot.id(), snapshot.material(), snapshot.pigs(), snapshot.events());
    }

    private final HouseId id;
    private final Material material;
    private final Pigs3 tenants = new Pigs3();
    private final List<HouseEvent> eventsToPublish = new ArrayList<>();

    private House(final HouseId id, final Material material, final List<Pig> pigs, final List<HouseEvent> events) {
        this.id = id;
        this.material = material;
        pigs.forEach(tenants::append);
        eventsToPublish.addAll(events);
    }

    public void letIn(final Pig guest) {
        tenants.append(guest);
    }

    public void runBrainstorming() {
        for (int i = 0; i < tenants.size(); ++i) {
            final Pig pig;
            if ((pig = tenants.get(i)) != null) {
                tenants.set(i, pig.learnFromMistakes());
            }
        }
    }

    void handleHurricane() {
        if (!CAN_BE_BLOWN_DOWN.isSatisfiedBy(this)) {
            throw new IndestructibleHouseException();
        }
        eventsToPublish.add(new HouseAbandoned(id, tenants.getSnapshot()));
        tenants.clear();
    }

    @Override
    public HouseSnapshot getSnapshot() {
        return new HouseSnapshot(id, material, tenants.getSnapshot(), eventsToPublish.stream().collect(toUnmodifiableList()));
    }

    private static class Pigs3 extends AbstractList<Pig> {
        private final Pig[] wrappedPigs = new Pig[3];

        int append(final Pig pig) {
            for (int i = 0; i < wrappedPigs.length; ++i) {
                if (wrappedPigs[i] == null) {
                    wrappedPigs[i] = pig;
                    return i;
                }
            }
            throw new TooManyPigsException(wrappedPigs.length);
        }

        List<Pig> getSnapshot() {
            return stream()
                    .filter(Objects::nonNull)
                    .collect(toUnmodifiableList());
        }

        @Override
        public Pig get(final int index) {
            return wrappedPigs[index];
        }

        @Override
        public Pig set(final int index, final Pig pig) {
            Pig old = wrappedPigs[index];
            wrappedPigs[index] = pig;
            return old;
        }

        @Override
        public void clear() {
            Arrays.fill(wrappedPigs, null);
        }

        @Override
        public int size() {
            return wrappedPigs.length;
        }
    }

    public static class TooManyPigsException extends RuntimeException {
        TooManyPigsException(final int limit) {
            super("No more than " + limit + " pigs allowed in the house");
        }
    }

    static class IndestructibleHouseException extends RuntimeException {
    }
}
