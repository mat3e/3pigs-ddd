package io.github.mat3e.fairytales.redhood;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Arrays.stream;

public class RedHoodCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(RedHoodCommandHandler.class);
    private final WolfRepository repository;

    RedHoodCommandHandler(WolfRepository repository) {
        this.repository = repository;
    }

    int startInteracting(Person... mentionedPeople) {
        var newWolf = new Wolf(stream(mentionedPeople).toList());
        return repository.save(newWolf).getSnapshot().id();
    }

    void meetWolf(Person aPerson, int aWolf) {
        repository.findById(aWolf).ifPresent(wolf -> {
            logger.info("The wolf met {}.", aPerson);
            wolf.meet(aPerson);
            repository.save(wolf);
        });
    }
}
