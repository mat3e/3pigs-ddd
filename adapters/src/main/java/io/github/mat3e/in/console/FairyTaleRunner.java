package io.github.mat3e.in.console;

import io.github.mat3e.app.ThreePigsCommandHandler;
import io.github.mat3e.app.command.BlowDown;
import io.github.mat3e.app.command.BuildHouse;
import io.github.mat3e.model.vo.Pig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;

class FairyTaleRunner implements CommandLineRunner {
    private final ThreePigsCommandHandler handler;
    private final JdbcTemplate jdbcTemplate;

    FairyTaleRunner(final ThreePigsCommandHandler handler, final JdbcTemplate jdbcTemplate) {
        this.handler = handler;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(final String... args) {
        var firstHouse = handler.handle(new BuildHouse(Pig.VERY_LAZY));
        var secondHouse = handler.handle(new BuildHouse(Pig.LAZY));
        var thirdHouse = handler.handle(new BuildHouse(Pig.NOT_LAZY));
        handler.handle(new BlowDown(firstHouse));
        handler.handle(new BlowDown(secondHouse));
        handler.handle(new BlowDown(thirdHouse));
        jdbcTemplate.update("delete from houses where id in (?, ?, ?)", firstHouse.value(), secondHouse.value(), thirdHouse.value());
    }
}
