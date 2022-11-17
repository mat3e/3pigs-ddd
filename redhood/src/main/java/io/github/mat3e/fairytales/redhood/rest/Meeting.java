package io.github.mat3e.fairytales.redhood.rest;

import io.github.mat3e.fairytales.redhood.Person;

record Meeting(Person participant) {
    static Meeting with(Person participant) {
        return new Meeting(participant);
    }
}
