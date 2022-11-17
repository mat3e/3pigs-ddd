package io.github.mat3e.fairytales.redhood.query;

import java.util.Optional;

interface WolfQueryRepository {
    Optional<Wolf> findById(int wolfId);
}

class WolfEntity {
}
