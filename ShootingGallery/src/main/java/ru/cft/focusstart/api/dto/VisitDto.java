package ru.cft.focusstart.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@JsonDeserialize(builder = VisitDto.Builder.class)
public class VisitDto {

    private final Long id;
    private final String surname;
    private final String name;
    private final String patronymic;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate birthdate;
    private final Long instructorId;
    private final Long weaponId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime datetimeStart;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime datetimeEnd;

    private VisitDto(Long id, String surname, String name, String patronymic, LocalDate birthdate, Long instructorId, Long weaponId, LocalDateTime datetimeStart, LocalDateTime datetimeEnd) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.birthdate = birthdate;
        this.instructorId = instructorId;
        this.weaponId = weaponId;
        this.datetimeStart = datetimeStart;
        this.datetimeEnd = datetimeEnd;
    }

    public Long getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public Long getInstructorId() {
        return instructorId;
    }

    public Long getWeaponId() {
        return weaponId;
    }

    public LocalDateTime getDatetimeStart() {
        return datetimeStart;
    }

    public LocalDateTime getDatetimeEnd() {
        return datetimeEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisitDto visitDto = (VisitDto) o;
        return Objects.equals(id, visitDto.id) &&
                Objects.equals(surname, visitDto.surname) &&
                Objects.equals(name, visitDto.name) &&
                Objects.equals(patronymic, visitDto.patronymic) &&
                Objects.equals(birthdate, visitDto.birthdate) &&
                Objects.equals(instructorId, visitDto.instructorId) &&
                Objects.equals(weaponId, visitDto.weaponId) &&
                Objects.equals(datetimeStart, visitDto.datetimeStart) &&
                Objects.equals(datetimeEnd, visitDto.datetimeEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surname, name, patronymic, birthdate, instructorId, weaponId, datetimeStart, datetimeEnd);
    }

    @Override
    public String toString() {
        return "VisitDto{" +
                "id=" + id +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", instructorId=" + instructorId +
                ", weaponId=" + weaponId +
                ", datetimeStart='" + datetimeStart + '\'' +
                ", datetimeEnd='" + datetimeEnd + '\'' +
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
        private String surname;
        private String name;
        private String patronymic;
        private LocalDate birthdate;
        private Long instructorId;
        private Long weaponId;
        private LocalDateTime datetimeStart;
        private LocalDateTime datetimeEnd;

        private Builder() {}

        private Builder(VisitDto visitDto) {
            this.id = visitDto.id;
            this.surname = visitDto.surname;
            this.name = visitDto.name;
            this.patronymic = visitDto.patronymic;
            this.birthdate = visitDto.birthdate;
            this.instructorId = visitDto.instructorId;
            this.weaponId = visitDto.weaponId;
            this.datetimeStart = visitDto.datetimeStart;
            this.datetimeEnd = visitDto.datetimeEnd;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder patronymic(String patronymic) {
            this.patronymic = patronymic;
            return this;
        }

        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        public Builder birthdate(LocalDate birthdate) {
            this.birthdate = birthdate;
            return this;
        }

        public Builder instructorId(Long instructorId) {
            this.instructorId = instructorId;
            return this;
        }

        public Builder weaponId(Long weaponId) {
            this.weaponId = weaponId;
            return this;
        }

        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        public Builder datetimeStart(LocalDateTime datetimeStart) {
            this.datetimeStart = datetimeStart;
            return this;
        }

        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        public Builder datetimeEnd(LocalDateTime datetimeEnd) {
            this.datetimeEnd = datetimeEnd;
            return this;
        }

        public VisitDto build() {
            return new VisitDto(id, surname, name, patronymic, birthdate, instructorId, weaponId, datetimeStart, datetimeEnd);
        }
    }
}
