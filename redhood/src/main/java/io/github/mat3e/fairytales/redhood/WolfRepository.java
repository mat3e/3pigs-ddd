package io.github.mat3e.fairytales.redhood;

import io.github.mat3e.ddd.DomainRepository;

interface WolfRepository extends DomainRepository<Integer, Wolf> {
    void deleteById(int wolfId);
}
