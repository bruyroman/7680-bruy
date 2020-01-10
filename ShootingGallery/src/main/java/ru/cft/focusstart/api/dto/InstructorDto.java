package ru.cft.focusstart.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import ru.cft.focusstart.entity.InstructorCategory;

import java.util.Date;
import java.util.Objects;

@JsonDeserialize(builder = InstructorDto.Builder.class)
public class InstructorDto {

    private final Long id;
    private final String surname;
    private final String name;
    private final String patronymic;
    private final Date birthdate;
    private final InstructorCategory category;

    private InstructorDto(Long id, String surname, String name, String patronymic, Date birthdate, InstructorCategory category) {
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

    public Date getBirthdate() {
        return birthdate;
    }

    public InstructorCategory getCategory() {
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
        return "BookDto{" +
                "id=" + id +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthdate=" + birthdate +
                ", category=" + category +
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
        private InstructorCategory category;

        private Builder() {}

        private Builder(InstructorDto instructorDto) {
            this.id = instructorDto.id;
            this.surname = instructorDto.surname;
            this.name = instructorDto.name;
            this.patronymic = instructorDto.patronymic;
            this.birthdate = instructorDto.birthdate;
            this.category = instructorDto.category;
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

        public Builder setCategory(InstructorCategory category) {
            this.category = category;
            return this;
        }

        public InstructorDto build() {
            return new InstructorDto(id, surname, name, patronymic, birthdate, category);
        }
    }
}
