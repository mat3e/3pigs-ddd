package io.github.mat3e.fairytales.model;

import io.github.mat3e.ddd.event.DomainEventPublisher;
import io.github.mat3e.fairytales.app.HouseQueryRepository;
import io.github.mat3e.fairytales.app.HouseReadModel;
import io.github.mat3e.fairytales.model.vo.HouseId;
import io.github.mat3e.fairytales.model.vo.HouseSnapshot;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryThreePigsRepository implements HouseQueryRepository, HouseRepository {
    private final Map<HouseId, House> impl = new HashMap<>();
    private final DomainEventPublisher publisher;

    public InMemoryThreePigsRepository(final DomainEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public House save(final House aggregate) {
        var newId = HouseId.of(impl.size() + 1);
        var source = aggregate.getSnapshot();
        var houseWithId = House.from(new HouseSnapshot(newId, source.material(), source.pigs()));
        impl.put(newId, houseWithId);
        source.events().forEach(publisher::publish);
        return houseWithId;
    }

    @Override
    public Optional<House> findById(final HouseId houseId) {
        return Optional.ofNullable(impl.get(houseId));
    }

    @NotNull
    @Override
    public Optional<HouseId> findClosestTo(@NotNull HouseId houseId) {
        return impl.keySet().stream()
                .map(HouseId::value)
                .sorted()
                .dropWhile(id -> id <= houseId.value())
                .findFirst()
                .map(HouseId::of);
    }

    @NotNull
    @Override
    public Optional<HouseReadModel> findDirect(@NotNull HouseId id) {
        return Optional.ofNullable(impl.get(id))
                .map(House::getSnapshot)
                .map(source -> new HouseReadModel(source.id().value(), source.material(), source.pigs()));
    }
}
