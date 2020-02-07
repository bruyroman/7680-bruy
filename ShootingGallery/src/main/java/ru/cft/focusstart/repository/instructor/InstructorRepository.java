package ru.cft.focusstart.repository.instructor;

import ru.cft.focusstart.entity.Instructor;
import ru.cft.focusstart.entity.InstructorCategory;

import java.util.List;
import java.util.Optional;

public interface InstructorRepository {

    List<Instructor> get(String fullName, InstructorCategory category);

    Optional<Instructor> getById(Long id);

    void add(Instructor instructor);
}
