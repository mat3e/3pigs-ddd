package io.github.mat3e.model.vo;

public enum Pig {
    NOT_LAZY,
    NOT_LAZY_ANYMORE,
    VERY_LAZY(NOT_LAZY_ANYMORE),
    LAZY(NOT_LAZY_ANYMORE);

    private final Pig learnt;

    Pig() {
        this(null);
    }

    Pig(Pig learnt) {
        this.learnt = learnt;
    }

    public Pig learnFromMistakes() {
        return learnt != null ? learnt : this;
    }
}
