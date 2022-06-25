package io.github.mat3e.fairytales.model.vo;

public record HouseId(int value) {
    public static HouseId empty() {
        return new HouseId(0);
    }

    public static HouseId of(int value) {
        return new HouseId(value);
    }

    public HouseId(final String value) {
        this(Integer.parseInt(value));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
