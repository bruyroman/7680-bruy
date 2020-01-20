package ru.cft.focusstart.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import java.util.Objects;

@JsonDeserialize(builder = InstructorDto.Builder.class)
public class InstructorDto {

    private final Long id;
    private final String surname;
    private final String name;
    private final String patronymic;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate birthdate;
    private final String category;

    private InstructorDto(Long id, String surname, String name, String patronymic, LocalDate birthdate, String category) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.birthdate = birthdate;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstructorDto instructorDto = (InstructorDto) o;
        return Objects.equals(id, instructorDto.id) &&
                Objects.equals(surname, instructorDto.surname) &&
                Objects.equals(name, instructorDto.name) &&
                Objects.equals(patronymic, instructorDto.patronymic) &&
                Objects.equals(birthdate, instructorDto.birthdate) &&
                Objects.equals(category, instructorDto.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surname, name, patronymic, birthdate, category);
    }

    @Override
    public String toString() {
        return "InstructorDto{" +
                "id=" + id +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", category='" + category + '\'' +
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
        private String category;

        private Builder() {}

        private Builder(InstructorDto instructorDto) {
            this.id = instructorDto.id;
            this.surname = instructorDto.surname;
            this.name = instructorDto.name;
            this.patronymic = instructorDto.patronymic;
            this.birthdate = instructorDto.birthdate;
            this.category = instructorDto.category;
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

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public InstructorDto build() {
            return new InstructorDto(id, surname, name, patronymic, birthdate, category);
        }
    }
}
