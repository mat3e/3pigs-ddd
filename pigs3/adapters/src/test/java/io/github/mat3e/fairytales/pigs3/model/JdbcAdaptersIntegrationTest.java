package io.github.mat3e.fairytales.pigs3.model;

import io.github.mat3e.fairytales.pigs3.model.event.HouseAbandoned;
import io.github.mat3e.fairytales.pigs3.model.vo.HouseId;
import io.github.mat3e.fairytales.pigs3.model.vo.HouseSnapshot;
import io.github.mat3e.fairytales.pigs3.model.vo.Material;
import io.github.mat3e.fairytales.pigs3.model.vo.Pig;
import org.assertj.core.api.Assertions;
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
class JdbcAdaptersIntegrationTest {
    @Autowired
    private HouseRepository domainRepository;

    @Test
    @DisplayName("should save, publish events and then read")
    void saveAndRead_worksAsExpected() {
        // given
        var snapshot = new HouseSnapshot(
                HouseId.empty(),
                Material.WOOD,
                List.of(Pig.LAZY, Pig.VERY_LAZY),
                List.of(new HouseAbandoned(HouseId.of(555), List.of(Pig.NOT_LAZY, Pig.NOT_LAZY_ANYMORE)))
        );

        // when
        HouseId savedId = domainRepository.save(House.from(snapshot)).getSnapshot().id();
        // and
        HouseSnapshot result = domainRepository.findById(savedId).get().getSnapshot();

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id", "events")
                .isEqualTo(snapshot);
        Assertions.assertThat(result.events()).isEmpty();
    }
}
