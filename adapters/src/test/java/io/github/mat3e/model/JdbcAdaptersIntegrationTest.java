package io.github.mat3e.model;

import io.github.mat3e.model.vo.HouseId;
import io.github.mat3e.model.vo.HouseSnapshot;
import io.github.mat3e.model.vo.Material;
import io.github.mat3e.model.vo.Pig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Tag("integration")
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class JdbcAdaptersIntegrationTest {
    @Autowired
    private HouseRepository domainRepository;

    @Test
    @DisplayName("should save and then read")
    void saveAndRead_worksAsExpected() {
        // given
        var snapshot = new HouseSnapshot(HouseId.empty(), Material.WOOD, List.of(Pig.LAZY, Pig.VERY_LAZY));

        // when
        HouseId savedId = domainRepository.save(House.from(snapshot)).getSnapshot().id();
        // and
        HouseSnapshot result = domainRepository.findById(savedId).get().getSnapshot();

        // then
        assertThat(result)
                .isEqualToComparingOnlyGivenFields(snapshot, "material", "pigs");
    }
}