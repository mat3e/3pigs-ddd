package io.github.mat3e.fairytales.pigs3.app;

import io.github.mat3e.ddd.event.DomainEventPublisher;
import io.github.mat3e.fairytales.pigs3.model.HouseRepository;
import io.github.mat3e.fairytales.pigs3.model.event.HouseEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

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
        System.out.println(Thread.currentThread().getName());
        app.getEventHandler().handle(event);
    }
}
