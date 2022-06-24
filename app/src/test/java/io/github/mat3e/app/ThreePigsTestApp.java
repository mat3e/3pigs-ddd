package io.github.mat3e.app;

import io.github.mat3e.ddd.event.DomainEvent;
import io.github.mat3e.ddd.event.DomainEventPublisher;
import io.github.mat3e.model.InMemoryThreePigsRepository;
import io.github.mat3e.model.event.HouseEvent;

import java.util.HashSet;
import java.util.Set;

class ThreePigsTestApp {
    private final HouseQueryRepository queryRepository;
    private final ThreePigsCommandHandler commandHandler;

    ThreePigsTestApp() {
        var publisher = new InMemoryDomainEventPublisher();
        var repo = new InMemoryThreePigsRepository(publisher);
        var app = new ThreePigsApp(repo, publisher, repo);
        publisher.register(app.getEventHandler());
        commandHandler = app.getCommandHandler();
        queryRepository = repo;
    }

    ThreePigsCommandHandler commandHandler() {
        return commandHandler;
    }

    HouseQueryRepository queryRepository() {
        return queryRepository;
    }

    private static class InMemoryDomainEventPublisher implements DomainEventPublisher {
        private final Set<ThreePigsEventHandler> handlers = new HashSet<>();

        @Override
        public void publish(DomainEvent event) {
            if (event instanceof HouseEvent houseEvent) {
                handlers.forEach(handler -> handler.handle(houseEvent));
            }
        }

        void register(ThreePigsEventHandler handler) {
            handlers.add(handler);
        }
    }
}
