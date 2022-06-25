package io.github.mat3e.fairytales.pigs3.app;

import io.github.mat3e.fairytales.pigs3.app.command.BlowDown;
import io.github.mat3e.fairytales.pigs3.app.command.BuildHouse;
import io.github.mat3e.fairytales.pigs3.model.vo.HouseId;
import io.github.mat3e.fairytales.pigs3.model.vo.Material;
import io.github.mat3e.fairytales.pigs3.model.vo.Pig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

// facade-like tests
@TestInstance(PER_CLASS)
class AppTest {
    private final ThreePigsTestApp app = new ThreePigsTestApp();

    @Test
    void buildHouse_allowsFinding() {
        // given
        var buildCommand = new BuildHouse(Pig.LAZY);

        // when
        HouseId createdId = app.commandHandler().handle(buildCommand);

        // then
        assertThat(app.queryRepository().findDirect(createdId)).hasValueSatisfying(house -> assertAll(
                () -> assertThat(house.isDestroyed()).isFalse(),
                () -> assertThat(house.getMaterial()).isEqualTo(Material.WOOD),
                () -> assertThat(house.getPigs()).containsExactly(Pig.LAZY)
        ));
    }

    @Test
    void blowDown_movesPigToNeighbor() {
        // given
        HouseId firstHouseId = app.commandHandler().handle(new BuildHouse(Pig.LAZY));
        // and
        app.commandHandler().handle(new BuildHouse(Pig.LAZY));
        // and
        var blowDownCommand = new BlowDown(firstHouseId);

        // when
        HouseId destroyedId = app.commandHandler().handle(blowDownCommand);

        // then
        assertThat(firstHouseId).isEqualTo(destroyedId);
        assertThat(app.queryRepository().findDirect(destroyedId)).hasValueSatisfying(house -> assertAll(
                () -> assertThat(house.isDestroyed()).isTrue(),
                () -> assertThat(house.getPigs()).isEmpty()
        ));
        assertThat(app.queryRepository().findClosestTo(destroyedId).flatMap(app.queryRepository()::findDirect)).hasValueSatisfying(house -> assertAll(
                () -> assertThat(house.isDestroyed()).isFalse(),
                () -> assertThat(house.getMaterial()).isEqualTo(Material.WOOD),
                () -> assertThat(house.getPigs()).containsExactly(Pig.LAZY, Pig.LAZY)
        ));
    }

    @Test
    void resignation_causesPigsLearning() {
        // given
        HouseId firstHouseId = app.commandHandler().handle(new BuildHouse(Pig.VERY_LAZY));
        // and
        HouseId secondHouseId = app.commandHandler().handle(new BuildHouse(Pig.NOT_LAZY));
        // and
        app.commandHandler().handle(new BlowDown(firstHouseId));

        // when
        app.commandHandler().handle(new BlowDown(secondHouseId));

        // then
        assertThat(app.queryRepository().findDirect(secondHouseId)).hasValueSatisfying(house -> assertAll(
                () -> assertThat(house.isDestroyed()).isFalse(),
                () -> assertThat(house.getMaterial()).isEqualTo(Material.BRICKS),
                () -> assertThat(house.getPigs()).containsExactly(Pig.NOT_LAZY, Pig.NOT_LAZY_ANYMORE)
        ));
    }
}
