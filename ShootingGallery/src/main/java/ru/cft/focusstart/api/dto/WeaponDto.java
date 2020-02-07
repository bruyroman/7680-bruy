package ru.cft.focusstart.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = WeaponDto.Builder.class)
public class WeaponDto {

    private final Long id;
    private final Long instructorId;
    private final String type;
    private final String model;
    private final String series;
    private final Integer number;

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {}
}
