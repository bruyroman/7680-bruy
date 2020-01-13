package ru.cft.focusstart.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Objects;

@JsonDeserialize(builder = WeaponDto.Builder.class)
public class WeaponDto {

    private final Long id;
    private final Long instructorId;
    private final String type;
    private final String model;
    private final String series;
    private final Integer number;

    private WeaponDto(Long id, Long instructorId, String type, String model, String series, Integer number) {
        this.id = id;
        this.instructorId = instructorId;
        this.type = type;
        this.model = model;
        this.series = series;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public Long getInstructorId() {
        return instructorId;
    }

    public String getType() {
        return type;
    }

    public String getModel() {
        return model;
    }

    public String getSeries() {
        return series;
    }

    public Integer getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeaponDto weaponDto = (WeaponDto) o;
        return Objects.equals(id, weaponDto.id) &&
                Objects.equals(instructorId, weaponDto.instructorId) &&
                Objects.equals(type, weaponDto.type) &&
                Objects.equals(model, weaponDto.model) &&
                Objects.equals(series, weaponDto.series) &&
                Objects.equals(number, weaponDto.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, instructorId, type, model, series, number);
    }

    @Override
    public String toString() {
        return "WeaponDto{" +
                "id=" + id +
                ", instructorId=" + instructorId +
                ", type='" + type + '\'' +
                ", model='" + model + '\'' +
                ", series='" + series + '\'' +
                ", number=" + number +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private Long id;
        private Long instructorId;
        private String type;
        private String model;
        private String series;
        private Integer number;

        private Builder() {}

        private Builder(WeaponDto weaponDto) {
            this.id = weaponDto.id;
            this.instructorId = weaponDto.instructorId;
            this.type = weaponDto.type;
            this.model = weaponDto.model;
            this.series = weaponDto.series;
            this.number = weaponDto.number;
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setInstructorId(Long instructorId) {
            this.instructorId = instructorId;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setModel(String model) {
            this.model = model;
            return this;
        }

        public Builder setSeries(String series) {
            this.series = series;
            return this;
        }

        public Builder setNumber(Integer number) {
            this.number = number;
            return this;
        }

        public WeaponDto build() {
            return new WeaponDto(id, instructorId, type, model, series, number);
        }
    }
}
