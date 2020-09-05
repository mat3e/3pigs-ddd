package io.github.mat3e.in.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.github.mat3e.model.vo.HouseId;
import io.github.mat3e.model.vo.Material;
import io.github.mat3e.model.vo.Pig;

import java.util.List;

abstract class BuildHouseJson {
    @JsonCreator
    BuildHouseJson(@JsonProperty Pig owner) {
    }
}

abstract class HouseIdJson {
    @JsonValue
    public abstract int value();
}

abstract class HouseSnapshotJson {
    @JsonProperty
    abstract HouseId id();

    @JsonProperty
    abstract Material material();

    @JsonProperty
    abstract List<Pig> pigs();
}