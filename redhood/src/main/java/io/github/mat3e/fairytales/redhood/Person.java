package io.github.mat3e.fairytales.redhood;

public enum Person {
    RED_HOOD("Red Riding Hood"), GRANDMA("grandma"), HUNTSMAN("huntsman");

    private final String writtenName;

    Person(String name) {
        this.writtenName = name;
    }

    @Override
    public String toString() {
        return writtenName;
    }
}
