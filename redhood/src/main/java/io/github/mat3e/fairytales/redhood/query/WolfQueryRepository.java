package io.github.mat3e.fairytales.redhood.query;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

interface WolfQueryRepository {
    Optional<Wolf> findById(int wolfId);
}

@Table("wolfs")
class WolfEntity {
    @Id
    private final Integer id;
    private final List<String> eatenPeople;

    public List<String> getEatenPeople() {
        return eatenPeople;
    }

    WolfEntity(Integer id, List<String> eatenPeople) {
        this.id = id;
        this.eatenPeople = eatenPeople;
    }
}

interface JdbcWolfQueryRepository extends WolfQueryRepository, Repository<WolfEntity, Integer> {
}
