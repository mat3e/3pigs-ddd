package io.github.mat3e.in.console;

import io.github.mat3e.app.ThreePigsCommandHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ConditionalOnProperty(value = "spring.main.web-application-type", havingValue = "none")
class ConsoleConfiguration {
    @Bean
    FairyTaleRunner fairyTale(final ThreePigsCommandHandler handler, final JdbcTemplate jdbcTemplate) {
        return new FairyTaleRunner(handler, jdbcTemplate);
    }
}
