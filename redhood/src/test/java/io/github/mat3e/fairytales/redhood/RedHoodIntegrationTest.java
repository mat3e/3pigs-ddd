package io.github.mat3e.fairytales.redhood;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static io.github.mat3e.fairytales.redhood.BusinessAssert.thenWolfFromJson;
import static io.github.mat3e.fairytales.redhood.Person.GRANDMA;
import static io.github.mat3e.fairytales.redhood.Person.RED_HOOD;
import static java.util.Arrays.stream;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Tag("integration")
@AutoConfigureMockMvc
class RedHoodIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void redHoodFairyTale_fullFlow() throws Exception {
        // given
        var wolfPath = startWolfPlan(GRANDMA, RED_HOOD)
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader(HttpHeaders.LOCATION);

        // when
        metWithWolf(GRANDMA, wolfPath).andExpect(status().isOk());
        // and
        var json = metWithWolf(RED_HOOD, wolfPath).andReturn().getResponse().getContentAsString();

        thenWolfFromJson(json).hasEaten(GRANDMA, RED_HOOD);

        // when
        wolfKilled(wolfPath).andExpect(status().isNoContent());

        thenLookingForWolf(wolfPath).andExpect(status().isNotFound());
    }

    private ResultActions startWolfPlan(Person... people) throws Exception {
        List<String> parsedPeople = stream(people)
                .map(Enum::name)
                .map(personName -> STR."\"\{personName}\"")
                .toList();
        return mvc.perform(post("/wolfs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(STR."""
                        {
                            "expectedPeopleToMeet": \{parsedPeople}
                        }"""));
    }

    private ResultActions metWithWolf(Person person, String wolfPath) throws Exception {
        return mvc.perform(put(STR."/wolfs\{wolfPath}/meetings/recent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(STR."""
                        {
                            "participant": "\{person.name()}"
                        }"""));
    }

    private ResultActions wolfKilled(String wolfPath) throws Exception {
        return mvc.perform(delete(STR."/wolfs\{wolfPath}"));
    }

    private ResultActions thenLookingForWolf(String wolfPath) throws Exception {
        return mvc.perform(get(STR."/wolfs\{wolfPath}"));
    }
}
