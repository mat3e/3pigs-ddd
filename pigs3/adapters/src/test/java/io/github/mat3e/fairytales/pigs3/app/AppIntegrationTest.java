package io.github.mat3e.fairytales.pigs3.app;

import io.github.mat3e.fairytales.pigs3.app.command.BlowDown;
import io.github.mat3e.fairytales.pigs3.app.command.BuildHouse;
import io.github.mat3e.fairytales.pigs3.model.vo.HouseId;
import io.github.mat3e.fairytales.pigs3.model.vo.Pig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@Tag("integration")
@SpringBootTest(webEnvironment = NONE)
class AppIntegrationTest {
    @Autowired
    private ThreePigsCommandHandler commandHandler;

    @Autowired
    private HouseQueryRepository queryRepository;

    @Test
    @DisplayName("pigs should escape to the nearest house")
    void houseDestroyed_causesPigsEscape() {
        // given
        HouseId lazyPigHouseId = commandHandler.handle(new BuildHouse(Pig.VERY_LAZY));
        // and
        HouseId smartPigHouseId = commandHandler.handle(new BuildHouse(Pig.NOT_LAZY));

        // when
        commandHandler.handle(new BlowDown(lazyPigHouseId));

        // then
        List<Pig> pigs = queryRepository.findDirect(smartPigHouseId)
                .map(HouseReadModel::getPigs)
                .get();
        assertThat(pigs).containsExactly(Pig.NOT_LAZY, Pig.VERY_LAZY);
    }

    @Test
    @DisplayName("pigs should learn after wolf resigned")
    void wolfResigned_causesPigsLearning() {
        // given
        HouseId lazyPigHouseId = commandHandler.handle(new BuildHouse(Pig.VERY_LAZY));
        // and
        HouseId smartPigHouseId = commandHandler.handle(new BuildHouse(Pig.NOT_LAZY));
        // and
        commandHandler.handle(new BlowDown(lazyPigHouseId));

        // when
        commandHandler.handle(new BlowDown(smartPigHouseId));

        // then
        List<Pig> pigs = queryRepository.findDirect(smartPigHouseId)
                .map(HouseReadModel::getPigs)
                .get();
        assertThat(pigs).containsOnly(Pig.NOT_LAZY, Pig.NOT_LAZY_ANYMORE);
    }
}
