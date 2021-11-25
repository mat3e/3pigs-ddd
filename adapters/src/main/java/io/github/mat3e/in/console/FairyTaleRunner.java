package io.github.mat3e.in.console;

import io.github.mat3e.app.ThreePigsCommandHandler;
import io.github.mat3e.model.vo.HouseId;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

class FairyTaleRunner implements CommandLineRunner {
    private final ThreePigsCommandHandler handler;
    private final JdbcTemplate jdbcTemplate;

    FairyTaleRunner(final ThreePigsCommandHandler handler, final JdbcTemplate jdbcTemplate) {
        this.handler = handler;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(final String... args) {
        List<HouseId> houses = handler.tellTheStory();
        jdbcTemplate.update(
                "delete from houses where id in (?, ?, ?)",
                houses.get(0).value(),
                houses.get(1).value(),
                houses.get(2).value()
        );
    }
}
