package io.github.mat3e.app;

import io.github.mat3e.model.BigBadWolfService;
import io.github.mat3e.model.HouseFactory;
import io.github.mat3e.model.HouseRepository;
import io.github.mat3e.model.event.HouseAbandoned;
import io.github.mat3e.model.vo.HouseId;
import io.github.mat3e.model.vo.Pig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ThreePigsEventHandlerTest {
    @Mock
    private HouseQueryRepository queryRepository;

    private ThreePigsEventHandler toTest;

    @BeforeEach
    void init() {
        var factory = new HouseFactory();
        var repository = mock(HouseRepository.class);
        when(repository.findById(any(HouseId.class)))
                .thenReturn(Optional.of(factory.buildFor(Pig.NOT_LAZY)));
        toTest = new ThreePigsEventHandler(
                queryRepository,
                new ThreePigsCommandHandler(
                        repository,
                        factory,
                        mock(BigBadWolfService.class)
                )
        );
    }

    @Test
    @DisplayName("should look for the closest house")
    void houseAbandoned_shouldUseTheClosestHouse() {
        given(queryRepository.findClosestTo(any(HouseId.class)))
                .willReturn(Optional.of(HouseId.of(99)));

        // when
        toTest.handle(new HouseAbandoned(HouseId.of(11), List.of(Pig.NOT_LAZY)));

        then(queryRepository)
                .should(times(1))
                .findClosestTo(any(HouseId.class));
    }
}
