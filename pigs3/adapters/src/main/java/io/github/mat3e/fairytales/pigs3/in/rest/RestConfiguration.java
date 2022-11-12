package io.github.mat3e.fairytales.pigs3.in.rest;

import io.github.mat3e.fairytales.pigs3.app.HouseReadModel;
import io.github.mat3e.fairytales.pigs3.app.command.BuildHouse;
import io.github.mat3e.fairytales.pigs3.model.vo.HouseId;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@Configuration
@ConditionalOnProperty(value = "spring.main.web-application-type", havingValue = "servlet")
class RestConfiguration {
    @Bean
    Jackson2ObjectMapperBuilderCustomizer registerMixIns() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder
                .mixIn(BuildHouse.class, BuildHouseJson.class)
                .mixIn(HouseId.class, HouseIdJson.class)
                .mixIn(HouseReadModel.class, HouseReadModelJson.class);
    }
}

@Configuration
@ConditionalOnProperty(value = "spring.main.web-application-type", havingValue = "servlet")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL_FORMS)
class HateoasConfiguration {
    @Bean
    HouseProcessor houseProcessor() {
        return new HouseProcessor();
    }
}
