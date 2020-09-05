package io.github.mat3e.app;

import io.github.mat3e.model.BigBadWolfService;
import io.github.mat3e.model.HouseFactory;
import io.github.mat3e.model.HouseRepository;
import io.github.mat3e.model.event.HouseEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
class AppConfiguration {
    @Bean
    ThreePigsCommandHandler commandHandler(final HouseRepository repository, final BigBadWolfService wolf) {
        return new ThreePigsCommandHandler(repository, new HouseFactory(), wolf);
    }
}

@Configuration
class EventConfiguration {
    private final ThreePigsEventHandler eventHandler;

    EventConfiguration(final HouseQueryRepository queryRepository, final ThreePigsCommandHandler commandHandler) {
        this.eventHandler = new ThreePigsEventHandler(queryRepository, commandHandler);
    }

    @Async
    @EventListener
    public void on(final HouseEvent event) {
        eventHandler.handle(event);
    }
}

@EnableAsync
@Configuration
@Profile("!test")
@ConditionalOnProperty(value = "spring.main.web-application-type", havingValue = "servlet")
class AsyncConfiguration {
}
