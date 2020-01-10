package ru.cft.focusstart.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@JsonDeserialize(builder = VisitDto.Builder.class)
public class VisitDto {

    private final Long id;
    private final String surname;
    private final String name;
    private final String patronymic;
    private final Date birthdate;
    private final Long instructorId;
    private final Long weaponId;
    private final LocalDateTime datetimeStart;
    private final LocalDateTime datetimeEnd;

    private VisitDto(Long id, String surname, String name, String patronymic, Date birthdate, Long instructorId, Long weaponId, LocalDateTime datetimeStart, LocalDateTime datetimeEnd) {
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

    public Date getBirthdate() {
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
        private Date birthdate;
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

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setPatronymic(String patronymic) {
            this.patronymic = patronymic;
            return this;
        }

        public Builder setBirthdate(Date birthdate) {
            this.birthdate = birthdate;
            return this;
        }

        public Builder setInstructorId(Long instructorId) {
            this.instructorId = instructorId;
            return this;
        }

        public Builder setWeaponId(Long weaponId) {
            this.weaponId = weaponId;
            return this;
        }

        public Builder setDatetimeStart(LocalDateTime datetimeStart) {
            this.datetimeStart = datetimeStart;
            return this;
        }

        public Builder setDatetimeEnd(LocalDateTime datetimeEnd) {
            this.datetimeEnd = datetimeEnd;
            return this;
        }

        public VisitDto build() {
            return new VisitDto(id, surname, name, patronymic, birthdate, instructorId, weaponId, datetimeStart, datetimeEnd);
        }
    }
}
