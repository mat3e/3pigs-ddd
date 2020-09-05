package io.github.mat3e.app;

import io.github.mat3e.model.vo.HouseId;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

interface HouseQueryRepositoryImpl extends HouseQueryRepository, Repository<HouseEntity, Integer> {
    @NotNull
    @Override
    default Optional<HouseId> findClosestTo(@NotNull HouseId houseId) {
        return findNextFor(houseId.value())
                .map(HouseId::of);
    }

    @Query("select id from houses where id > :id limit 1")
    Optional<Integer> findNextFor(@Param("id") int houseId);
}

@Table("houses")
class HouseEntity {
    @Id
    private final int id;

    HouseEntity(final int id) {
        this.id = id;
    }
}
