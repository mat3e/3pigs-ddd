package io.github.mat3e.fairytales.model;

import io.github.mat3e.ddd.Aggregate;
import io.github.mat3e.fairytales.model.event.HouseAbandoned;
import io.github.mat3e.fairytales.model.event.HouseEvent;
import io.github.mat3e.fairytales.model.vo.HouseId;
import io.github.mat3e.fairytales.model.vo.HouseSnapshot;
import io.github.mat3e.fairytales.model.vo.Material;
import io.github.mat3e.fairytales.model.vo.Pig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class House implements Aggregate<HouseId, HouseSnapshot> {
    static House from(final HouseSnapshot snapshot) {
        return new House(snapshot.id(), snapshot.material(), snapshot.pigs(), snapshot.events());
    }

    private final HouseId id;
    private final Material material;
    private final Pigs tenants = new Pigs(3);
    private final List<HouseEvent> eventsToPublish = new ArrayList<>();

    private House(final HouseId id, final Material material, final List<Pig> pigs, final List<HouseEvent> events) {
        this.id = id;
        this.material = material;
        pigs.forEach(tenants::welcome);
        eventsToPublish.addAll(events);
    }

    public void letIn(final Pig guest) {
        tenants.welcome(guest);
    }

    public void runBrainstorming() {
        tenants.runBrainstorming();
    }

    void handleHurricane(final BlowingDownSpecification canBeBlownDown) {
        if (!canBeBlownDown.isSatisfiedBy(this)) {
            throw new IndestructibleHouseException();
        }
        eventsToPublish.add(new HouseAbandoned(id, tenants.takeCover()));
    }

    @Override
    public HouseSnapshot getSnapshot() {
        return new HouseSnapshot(id, material, tenants.getSnapshot(), eventsToPublish.stream().toList());
    }

    private static class Pigs /*implements DomainEntity*/ {
        private final Pig[] spots;

        Pigs(final int max) {
            spots = new Pig[max];
        }

        void welcome(final Pig pig) {
            assertHasFreeSpots();
            spots[firstEmptySpotNumber()] = pig;
        }

        List<Pig> takeCover() {
            List<Pig> result = getSnapshot();
            clear();
            return result;
        }

        void runBrainstorming() {
            for (int i = 0; i < maxAvailableSpots(); ++i) {
                spots[i] = Pig.learnFromMistakes(spots[i]);
            }
        }

        List<Pig> getSnapshot() {
            return Arrays.stream(spots).filter(Objects::nonNull).toList();
        }

        private void assertHasFreeSpots() {
            if (firstEmptySpotNumber() >= maxAvailableSpots()) {
                throw new TooManyPigsException(maxAvailableSpots());
            }
        }

        private int firstEmptySpotNumber() {
            for (int i = 0; i < maxAvailableSpots(); ++i) {
                if (spots[i] == null) {
                    return i;
                }
            }
            return maxAvailableSpots();
        }

        private int maxAvailableSpots() {
            return spots.length;
        }

        private void clear() {
            Arrays.fill(spots, null);
        }
    }

    static class TooManyPigsException extends RuntimeException {
        TooManyPigsException(final int limit) {
            super("No more than " + limit + " pigs allowed in the house");
        }
    }

    static class IndestructibleHouseException extends RuntimeException {
    }
}
