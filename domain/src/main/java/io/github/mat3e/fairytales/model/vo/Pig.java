package io.github.mat3e.fairytales.model.vo;

public enum Pig {
    NOT_LAZY,
    NOT_LAZY_ANYMORE,
    VERY_LAZY(NOT_LAZY_ANYMORE),
    LAZY(NOT_LAZY_ANYMORE);

    public static Pig learnFromMistakes(final Pig original) {
        return original == null ? null : original.learnFromMistakes();
    }

    private final Pig learnt;

    Pig() {
        this(null);
    }

    Pig(final Pig learnt) {
        this.learnt = learnt;
    }

    private Pig learnFromMistakes() {
        return learnt != null ? learnt : this;
    }
}
