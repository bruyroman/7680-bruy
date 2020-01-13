package ru.cft.focusstart.repository.visit;

import ru.cft.focusstart.entity.Visit;
import ru.cft.focusstart.repository.DataSourceProvider;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class JdbcVisitRepository implements VisitRepository {

    private static final JdbcVisitRepository INSTANCE = new JdbcVisitRepository();

    private final DataSource dataSource;

    private JdbcVisitRepository() {
        this.dataSource = DataSourceProvider.getDataSource();
    }

    public static VisitRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Visit> get(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo, String fullNameClient) {
        return null;
    }

    @Override
    public Optional<Visit> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public void add(Visit visit) {

    }

    @Override
    public void update(Visit visit) {

    }

    @Override
    public void delete(Visit visit) {

    }
}
