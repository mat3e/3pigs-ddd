package io.github.mat3e.app;

import io.github.mat3e.ddd.event.DomainEventPublisher;
import io.github.mat3e.model.HouseRepository;
import io.github.mat3e.model.event.HouseEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration(proxyBeanMethods = false)
class AppConfiguration {
    private final ThreePigsApp app;

    AppConfiguration(final HouseRepository repository, final DomainEventPublisher eventPublisher, final HouseQueryRepository queryRepository) {
        app = new ThreePigsApp(repository, eventPublisher, queryRepository);
    }

    @Bean
    ThreePigsCommandHandler commandHandler() {
        return app.getCommandHandler();
    }
}

@Configuration
class EventConfiguration {
    private final ThreePigsEventHandler eventHandler;

    // allows overriding ThreePigsCommandHandler as ThreePigsApp#getEventHandler already has a hardcoded ThreePigsCommandHandler
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
