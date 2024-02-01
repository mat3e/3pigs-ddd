package io.github.mat3e.fairytales.redhood;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mat3e.fairytales.redhood.query.RedHoodQuery;
import io.github.mat3e.fairytales.redhood.query.Wolf;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.BDDAssertions;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
class BusinessAssert extends BDDAssertions {
    static void thenShouldNotExist(RedHoodQuery query, int wolfId) {
        WolfAssert.thenShouldNotExist(query.findById(wolfId));
    }

    static WolfAssert expect(Optional<Wolf> potentialWolf) {
        return WolfAssert.then(potentialWolf);
    }

    static WolfAssert thenFoundWith(RedHoodQuery query, int wolfId) {
        return WolfAssert.then(query.findById(wolfId));
    }

    static WolfAssert then(io.github.mat3e.fairytales.redhood.Wolf wolf) {
        return WolfAssert.then(wolf);
    }

    static WolfAssert thenWolfFromJson(String jsonString) throws JsonProcessingException {
        return new WolfAssert(WolfJson.from(jsonString));
    }

    record WolfJson(@JsonProperty("eatenPeople") List<String> getEatenPeople) implements Wolf {
        static Wolf from(String jsonString) throws JsonProcessingException {
            return new ObjectMapper().readerFor(WolfJson.class).readValue(jsonString);
        }

        WolfJson {
            getEatenPeople = List.copyOf(getEatenPeople);
        }
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static class WolfAssert extends AbstractAssert<WolfAssert, Wolf> {
        static void thenShouldNotExist(Optional<Wolf> potentialWolf) {
            assertThat(potentialWolf).isEmpty();
        }

        static WolfAssert then(Optional<Wolf> potentialWolf) {
            assertThat(potentialWolf).isPresent();
            return new WolfAssert(potentialWolf.get());
        }

        static WolfAssert then(io.github.mat3e.fairytales.redhood.Wolf wolf) {
            return new WolfAssert(() -> wolf.getSnapshot().alreadyEatenPeople().stream().map(Enum::name).toList());
        }

        protected WolfAssert(Wolf wolf) {
            super(wolf, WolfAssert.class);

        }

        WolfAssert hasNotEatenAnyone() {
            assertThat(actual.getEatenPeople()).isEmpty();
            return this;
        }

        WolfAssert hasEaten(Person... people) {
            assertThat(actual.getEatenPeople()).map(Person::valueOf).containsExactly(people);
            return this;
        }
    }
}
