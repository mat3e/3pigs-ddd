package io.github.mat3e.fairytales.redhood.query;

import java.util.Optional;

public class RedHoodQuery {
    private final WolfQueryRepository wolfQueryRepository;

    public RedHoodQuery(WolfQueryRepository wolfQueryRepository) {
        this.wolfQueryRepository = wolfQueryRepository;
    }

    public Optional<Wolf> findById(int wolfId) {
        return wolfQueryRepository.findById(wolfId);
    }
}
