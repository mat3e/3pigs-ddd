package io.github.mat3e.app;

import io.github.mat3e.model.vo.HouseId;
import io.github.mat3e.model.vo.Material;
import io.github.mat3e.model.vo.Pig;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableList;

@org.springframework.stereotype.Repository
class HouseQueryRepositoryImpl implements HouseQueryRepository {
    private final JdbcHouseQueryRepository springRepository;

    HouseQueryRepositoryImpl(final JdbcHouseQueryRepository springRepository) {
        this.springRepository = springRepository;
    }

    @NotNull
    @Override
    public Optional<HouseId> findClosestTo(@NotNull HouseId houseId) {
        return springRepository.findNextFor(houseId.value())
                .map(HouseId::of);
    }

    @NotNull
    @Override
    public Optional<HouseReadModel> findDirect(@NotNull HouseId id) {
        return springRepository.findById(id.value())
                .map(entity -> new HouseReadModel(
                        entity.id(),
                        entity.material(),
                        entity.pigs().toList()
                ));
    }
}

interface JdbcHouseQueryRepository extends Repository<HouseEntity, Integer> {
    @Query("select id from houses where id > :id limit 1")
    Optional<Integer> findNextFor(@Param("id") int houseId);

    Optional<HouseEntity> findById(int id);
}

@Table("houses")
record HouseEntity(@Id int id, Material material, @Embedded.Empty Pigs pigs) {
}

record Pigs(Pig first, Pig second, Pig third) {
    List<Pig> toList() {
        return Stream.of(first, second, third)
                .filter(Objects::nonNull)
                .collect(toUnmodifiableList());
    }
}
