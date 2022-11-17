package io.github.mat3e.fairytales.redhood.query;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RedHoodQueryConfiguration {
    private final WolfQueryRepository wolfQueryRepository;

    RedHoodQueryConfiguration(WolfQueryRepository wolfQueryRepository) {
        this.wolfQueryRepository = wolfQueryRepository;
    }

    @Bean
    RedHoodQuery redHoodQuery() {
        return new RedHoodQuery(wolfQueryRepository);
    }
}
