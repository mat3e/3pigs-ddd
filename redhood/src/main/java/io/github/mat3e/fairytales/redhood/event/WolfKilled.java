package io.github.mat3e.fairytales.redhood.event;

import io.github.mat3e.fairytales.redhood.Person;

import java.time.Instant;
import java.util.List;

public record WolfKilled(Instant occurredOn, int wolfId, List<Person> rescuedPeople) implements WolfEvent {
    public WolfKilled {
        rescuedPeople = List.copyOf(rescuedPeople);
    }

    public WolfKilled(int wolfId, List<Person> rescuedPeople) {
        this(Instant.now(), wolfId, rescuedPeople);
    }
}
