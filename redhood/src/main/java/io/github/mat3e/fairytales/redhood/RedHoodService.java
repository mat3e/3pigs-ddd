package io.github.mat3e.fairytales.redhood;

import io.github.mat3e.ddd.event.DomainEvent;
import io.github.mat3e.ddd.event.DomainEventPublisher;
import io.github.mat3e.fairytales.redhood.event.WolfKilled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Arrays.stream;

public class RedHoodService {
    private static final Logger logger = LoggerFactory.getLogger(RedHoodService.class);
    private final WolfRepository repository;
    private final DomainEventPublisher eventPublisher;

    RedHoodService(WolfRepository repository, DomainEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public int startInteracting(Person... mentionedPeople) {
        var newWolf = new Wolf(stream(mentionedPeople).toList());
        var afterSave = repository.save(newWolf).getSnapshot().id();
        publishEvents(newWolf);
        return afterSave;
    }

    @Transactional
    public void meetWolf(Person aPerson, int aWolf) {
        repository.findById(aWolf).ifPresentOrElse(
                wolf -> {
                    logger.info("The wolf met {}.", aPerson);
                    wolf.meet(aPerson);
                    repository.save(wolf);
                    publishEvents(wolf);
                },
                () -> logger.info("There is no wolf #{}!", aWolf)
        );
    }

    @Async
    @EventListener
    public void onEvent(DomainEvent event) {
        if (event instanceof WolfKilled wolfKilled) {
            repository.deleteById(wolfKilled.wolfId());
            logger.info("Killed wolf got his belly cut open!");
            if (wolfKilled.rescuedPeople().isEmpty()) {
                logger.info("But no one was inside.");
            } else {
                logger.info(
                        "And {} jumped out!",
                        String.join(" with ", wolfKilled.rescuedPeople().stream().map(Object::toString).toList())
                );
            }
        }
    }

    private void publishEvents(Wolf wolf) {
        wolf.getSnapshot().events().forEach(eventPublisher::publish);
    }
}
