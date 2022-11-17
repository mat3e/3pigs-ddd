package io.github.mat3e.fairytales.redhood;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNullElse;

public class InMemoryWolfRepository implements WolfRepository {
    private final Map<Integer, Wolf> db = new HashMap<>();
    private final Function<Wolf, Integer> idGetter;

    InMemoryWolfRepository(Function<Wolf, Integer> idGetter) {
        this.idGetter = idGetter;
    }

    public List<Person> findEatenPeople(Integer id) {
        return findById(id)
                .map(wolf -> wolf.getSnapshot().alreadyEatenPeople().stream().toList())
                .orElseThrow();
    }

    @Override
    public Optional<Wolf> findById(Integer id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public Wolf save(Wolf aggregate) {
        int id = requireNonNullElse(idGetter.apply(aggregate), db.size() + 1);
        var source = aggregate.getSnapshot();
        var toSave = Wolf.fromSnapshot(new Wolf.Snapshot(id, source.plannedEatingOrder(), source.alreadyEatenPeople()));
        db.put(id, toSave);
        return toSave;
    }
}
