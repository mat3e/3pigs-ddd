package io.github.mat3e.model;

import io.github.mat3e.app.ThreePigsCommandHandler;
import io.github.mat3e.app.command.Enter;
import io.github.mat3e.model.event.HouseAbandoned;
import io.github.mat3e.model.vo.HouseId;
import io.github.mat3e.model.vo.HouseSnapshot;
import io.github.mat3e.model.vo.Material;
import io.github.mat3e.model.vo.Pig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@SpringBootTest(webEnvironment = NONE)
@Tag("integration")
@ActiveProfiles("test")
class JdbcAdaptersIntegrationTest {
    @Autowired
    private HouseRepository domainRepository;

    @MockBean
    private ThreePigsCommandHandler commandHandler;

    @Test
    @DisplayName("should save, publish events and then read")
    void saveAndRead_worksAsExpected() {
        // given
        var snapshot = new HouseSnapshot(
                HouseId.empty(),
                Material.WOOD,
                List.of(Pig.LAZY, Pig.VERY_LAZY),
                List.of(new HouseAbandoned(HouseId.empty(), List.of(Pig.NOT_LAZY, Pig.NOT_LAZY_ANYMORE)))
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
        verify(commandHandler, times(2))
                .handle(any(Enter.class)); // house abandoned event with 2 pigs published
    }
}
