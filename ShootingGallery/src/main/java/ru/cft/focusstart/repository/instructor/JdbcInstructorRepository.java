package ru.cft.focusstart.repository.instructor;

import ru.cft.focusstart.entity.Instructor;
import ru.cft.focusstart.entity.InstructorCategory;
import ru.cft.focusstart.repository.DataSourceProvider;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class JdbcInstructorRepository implements InstructorRepository {

    private static final JdbcInstructorRepository INSTANCE = new JdbcInstructorRepository();

    private final DataSource dataSource;

    private JdbcInstructorRepository() {
        this.dataSource = DataSourceProvider.getDataSource();
    }

    public static InstructorRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Instructor> get(String fullName, InstructorCategory category) {
        return null;
    }

    @Override
    public Optional<Instructor> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public void add(Instructor instructor) {

    }

    @Override
    public void update(Instructor instructor) {

    }
}
