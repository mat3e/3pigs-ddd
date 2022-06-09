package io.github.mat3e.app;

import io.github.mat3e.app.command.BlowDown;
import io.github.mat3e.app.command.Enter;
import io.github.mat3e.model.House;
import io.github.mat3e.model.HouseFactory;
import io.github.mat3e.model.HouseRepository;
import io.github.mat3e.model.vo.HouseId;
import io.github.mat3e.model.vo.HouseSnapshot;
import io.github.mat3e.model.vo.Pig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@SpringBootTest(webEnvironment = NONE, properties = "server.port=9997")
@Tag("integration")
@ActiveProfiles("test")
class AppIntegrationTest {
    @Autowired
    private HouseRepository domainRepository;

    @Autowired
    private ThreePigsCommandHandler commandHandler;

    @Test
    @DisplayName("pigs should learn after wolf resigned")
    void wolfResigned_causesPigsLearning() {
        // given
        HouseId savedId = domainRepository.save(
                new HouseFactory()
                        .buildFor(Pig.NOT_LAZY)
        ).getSnapshot().id();
        // and
        commandHandler.handle(new Enter(Pig.LAZY, savedId));

        // when
        commandHandler.handle(new BlowDown(savedId));

        // then
        List<Pig> pigs = domainRepository.findById(savedId)
                .map(House::getSnapshot)
                .map(HouseSnapshot::pigs)
                .get();
        assertThat(pigs).containsOnly(Pig.NOT_LAZY, Pig.NOT_LAZY_ANYMORE);
    }

    @Test
    @DisplayName("pigs should escape to the nearest house")
    void houseDestroyed_causesPigsEscape() {
        // given
        HouseId first = domainRepository.save(
                new HouseFactory()
                        .buildFor(Pig.VERY_LAZY)
        ).getSnapshot().id();
        HouseId second = domainRepository.save(
                new HouseFactory()
                        .buildFor(Pig.LAZY)
        ).getSnapshot().id();

        // when
        commandHandler.handle(new BlowDown(first));

        // then
        List<Pig> pigs = domainRepository.findById(second)
                .map(House::getSnapshot)
                .map(HouseSnapshot::pigs)
                .get();
        assertThat(pigs).containsOnly(Pig.VERY_LAZY, Pig.LAZY);
    }
}
