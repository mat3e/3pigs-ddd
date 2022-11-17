package io.github.mat3e.fairytales.redhood;

import io.github.mat3e.ddd.DomainRepository;
import org.springframework.data.repository.Repository;

interface WolfRepository extends DomainRepository<Integer, Wolf> {
    void deleteById(int wolfId);
}

interface JdbcWolfRepository extends WolfRepository, Repository<Wolf, Integer> {
}
