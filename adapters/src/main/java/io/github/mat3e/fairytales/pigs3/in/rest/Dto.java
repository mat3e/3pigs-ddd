package io.github.mat3e.fairytales.pigs3.in.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.github.mat3e.fairytales.pigs3.model.vo.Pig;

abstract class BuildHouseJson {
    @JsonCreator
    BuildHouseJson(@JsonProperty Pig owner) {
    }
}

abstract class HouseIdJson {
    @JsonValue
    public abstract int value();
}
