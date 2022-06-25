package io.github.mat3e.fairytales.app;

import io.github.mat3e.ddd.event.DomainEventPublisher;
import io.github.mat3e.fairytales.model.HouseRepository;
import io.github.mat3e.fairytales.model.event.HouseEvent;
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

    @Async
    @EventListener
    public void on(final HouseEvent event) {
        app.getEventHandler().handle(event);
    }
}

@EnableAsync
@Configuration
@Profile("!test")
@ConditionalOnProperty(value = "spring.main.web-application-type", havingValue = "servlet")
class AsyncConfiguration {
}
