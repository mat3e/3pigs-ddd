package io.github.mat3e.fairytales.app;

import io.github.mat3e.fairytales.app.command.*;
import io.github.mat3e.fairytales.model.BigBadWolfService;
import io.github.mat3e.fairytales.model.House;
import io.github.mat3e.fairytales.model.HouseFactory;
import io.github.mat3e.fairytales.model.HouseRepository;
import io.github.mat3e.fairytales.model.vo.HouseId;
import io.github.mat3e.fairytales.model.vo.HouseSnapshot;
import io.github.mat3e.fairytales.model.vo.Material;
import io.github.mat3e.fairytales.model.vo.Pig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ThreePigsCommandHandlerTest {
    @Mock
    private HouseRepository repository;

    private ThreePigsCommandHandler toTest;

    @BeforeEach
    void init() {
        toTest = new ThreePigsCommandHandler(repository, new HouseFactory(), mock(BigBadWolfService.class));
    }

    @Test
    @DisplayName("should save a new house")
    void validCommand_shouldSaveNewHouse() {
        // given
        savingRepository();

        // when
        HouseId savedId = toTest.handle(new BuildHouse(Pig.LAZY));

        then(repository)
                .should(times(1))
                .save(any(House.class));
        assertThat(savedId.value()).isNotNull();
    }

    @MethodSource
    @ParameterizedTest
    @DisplayName("should save house after the command")
    void validCommand_shouldSaveUpdatedHouse(ThreePigsCommand command) {
        House house = new HouseFactory().buildFor(Pig.LAZY);
        given(repository.findById(any(HouseId.class)))
                .willReturn(Optional.of(house));
        // and
        savingRepository();

        // when
        toTest.handle(command);

        then(repository)
                .should(times(1))
                .save(any(House.class));
    }

    static Stream<Arguments> validCommand_shouldSaveUpdatedHouse() {
        return Stream.of(
                arguments(new Enter(List.of(Pig.LAZY), HouseId.of(999))),
                arguments(new ShareKnowledge(HouseId.of(999)))
        );
    }

    @MethodSource
    @ParameterizedTest
    @DisplayName("should throw when no house found")
    void updateCommand_throwsWhenNoHouse(UpdateCommand command) {
        // when
        var thrown = catchThrowable(() -> toTest.handle(command));

        // then
        assertThat(thrown)
                .isInstanceOf(HouseNotFoundException.class)
                .hasMessageContaining(String.valueOf(command.getId().value()));
    }

    static Stream<Arguments> updateCommand_throwsWhenNoHouse() {
        return Stream.of(
                arguments(new BlowDown(HouseId.of(123))),
                arguments(new Enter(List.of(Pig.LAZY), HouseId.of(456))),
                arguments(new ShareKnowledge(HouseId.of(789)))
        );
    }

    @MethodSource
    @ParameterizedTest
    @DisplayName("should not save if no changes happen")
    void updateCommand_notSavesIfNotNeeded(UpdateCommand command) {
        House house = unchangeableHouse();
        given(repository.findById(any(HouseId.class)))
                .willReturn(Optional.of(house));

        // when
        toTest.handle(command);

        then(repository).shouldHaveNoMoreInteractions();
    }

    static Stream<Arguments> updateCommand_notSavesIfNotNeeded() {
        return Stream.of(
                arguments(new BlowDown(HouseId.of(123))),
                arguments(new Enter(List.of(Pig.LAZY), HouseId.of(456))),
                arguments(new ShareKnowledge(HouseId.of(789)))
        );
    }

    private House unchangeableHouse() {
        var savedId = HouseId.of(999);
        var savedHouse = mock(House.class);
        given(savedHouse.getSnapshot())
                .willReturn(new HouseSnapshot(savedId, Material.WOOD, emptyList()));
        return savedHouse;
    }

    private void savingRepository() {
        given(repository.save(any(House.class)))
                .willAnswer(invocation -> invocation.getArgument(0));
    }
}
