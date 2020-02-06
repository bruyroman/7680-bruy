package ru.cft.focusstart.repository.visit;

import org.springframework.stereotype.Repository;
import ru.cft.focusstart.entity.Person;
import ru.cft.focusstart.entity.Visit;
import ru.cft.focusstart.repository.DataAccessException;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import static ru.cft.focusstart.repository.reader.VisitReader.readVisit;

@Repository
public class JdbcVisitRepository implements VisitRepository {

    private static final String GET_QUERY =
            "SELECT  vst.\"ID\" VISIT_ID," +
                    "prs.\"ID\" CLIENT_ID," +
                    "prs.\"SURNAME\"," +
                    "prs.\"NAME\"," +
                    "prs.\"PATRONYMIC\"," +
                    "prs.\"BIRTHDATE\"," +
                    "vst.\"INSTRUCTOR_ID\"," +
                    "vst.\"WEAPON_ID\"," +
                    "vst.\"DATETIME_START\"," +
                    "vst.\"DATETIME_END\" " +
                    "FROM public.\"VISIT\" vst " +
                    "inner join public.\"PERSON\" prs on prs.\"ID\" = vst.\"CLIENT_ID\"";

    private static final String GET_BY_ID_QUERY =
            GET_QUERY +
                    " WHERE vst.\"ID\" = ?";

    private static final String GET_BY_FULLNAME_QUERY =
            GET_QUERY +
                    " WHERE lower(prs.\"SURNAME\"||' '||prs.\"NAME\"||' '||COALESCE(prs.\"PATRONYMIC\", '')) like lower('%' || ? || '%') ";

    private static final String ADD_PERSON_QUERY =
            "INSERT INTO public.\"PERSON\" (\"SURNAME\", \"NAME\", \"PATRONYMIC\", \"BIRTHDATE\") " +
                    "VALUES (?, ?, ?, ?)";

    private static final String ADD_QUERY =
            "INSERT INTO public.\"VISIT\" (\"CLIENT_ID\", \"INSTRUCTOR_ID\", \"WEAPON_ID\", \"DATETIME_START\", \"DATETIME_END\") " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_PERSON_QUERY =
            "UPDATE public.\"PERSON\" SET \"SURNAME\"=?, \"NAME\"=?, \"PATRONYMIC\"=?, \"BIRTHDATE\"=? " +
                    "WHERE \"ID\"=?";

    private static final String UPDATE_QUERY =
            "UPDATE public.\"VISIT\" SET \"CLIENT_ID\"=?, \"INSTRUCTOR_ID\"=?, \"WEAPON_ID\"=?, \"DATETIME_START\"=?, \"DATETIME_END\"=? " +
                    "WHERE \"ID\"=?";

    private static final String DELETE_QUERY =
            "delete from public.\"VISIT\" where \"ID\" = ?";

    private final DataSource dataSource;

    private static String getByFullnameAndDatetimeQuery(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo) {
        return GET_BY_FULLNAME_QUERY +
                (dateTimeFrom == null ? "" : " and vst.\"DATETIME_START\" = ?") +
                (dateTimeTo == null ? "" : " and vst.\"DATETIME_END\" = ?");
    }

    public JdbcVisitRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Visit> get(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo, String fullNameClient) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement psVisits = con.prepareStatement(getByFullnameAndDatetimeQuery(dateTimeFrom, dateTimeTo));
        ) {
            int argsNumber = 1;
            psVisits.setString(argsNumber++, fullNameClient == null ? "" : fullNameClient);

            if (dateTimeFrom != null) {
                psVisits.setTimestamp(argsNumber++, Timestamp.valueOf(dateTimeFrom));
            }

            if (dateTimeTo != null) {
                psVisits.setTimestamp(argsNumber++, Timestamp.valueOf(dateTimeTo));
            }

            ResultSet rsVisits = psVisits.executeQuery();

            Collection<Visit> visits = readVisitsList(rsVisits);

            return new ArrayList<>(visits);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    private Collection<Visit> readVisitsList(ResultSet rsVisits) throws SQLException {
        Map<Long, Visit> result = new HashMap<>();

        while (rsVisits.next()) {
            long id = rsVisits.getLong("VISIT_ID");

            Visit visit = result.get(id);
            if (visit == null) {
                visit = readVisit(rsVisits);
                result.put(id, visit);
            }
        }

        return result.values();
    }

    @Override
    public Optional<Visit> getById(Long id) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement psVisits = con.prepareStatement(GET_BY_ID_QUERY);
        ) {
            psVisits.setLong(1, id);

            ResultSet rsVisits = psVisits.executeQuery();

            Collection<Visit> visits = readVisitsList(rsVisits);

            if (visits.isEmpty()) {
                return Optional.empty();
            } else if (visits.size() == 1) {
                return Optional.of(visits.iterator().next());
            } else {
                throw new SQLException("Unexpected result set size");
            }
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void add(Visit visit) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement psClient = con.prepareStatement(ADD_PERSON_QUERY, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement psVisit = con.prepareStatement(ADD_QUERY, Statement.RETURN_GENERATED_KEYS);
        ) {
            Person client = visit.getClient();
            setQueryClient(psClient, client);
            psClient.executeUpdate();
            client.setId(getGeneratedKeys(psClient));

            visit.setClient(client);
            setQueryVisit(psVisit, visit);
            psVisit.executeUpdate();
            visit.setId(getGeneratedKeys(psVisit));
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    private void setQueryClient(PreparedStatement psClient, Person client) throws SQLException {
        psClient.setString(1, client.getSurname());
        psClient.setString(2, client.getName());
        psClient.setString(3, client.getPatronymic());
        psClient.setDate(4, java.sql.Date.valueOf(client.getBirthdate()));
    }

    private void setQueryVisit(PreparedStatement psVisit, Visit visit) throws SQLException {
        psVisit.setLong(1, visit.getClient().getId());
        psVisit.setLong(2, visit.getInstructor().getId());
        psVisit.setLong(3, visit.getWeapon().getId());
        psVisit.setTimestamp(4, Timestamp.valueOf(visit.getDatetimeStart()));
        psVisit.setTimestamp(5, visit.getDatetimeEnd() == null ? null : Timestamp.valueOf(visit.getDatetimeEnd()));
    }

    private Long getGeneratedKeys(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.getGeneratedKeys();
        Long id = rs.next() ? rs.getLong(1) : null;
        if (id == null) {
            throw new SQLException("Unexpected error - could not obtain id");
        }
        return id;
    }

    @Override
    public void update(Visit visit) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement psClient = con.prepareStatement(UPDATE_PERSON_QUERY);
                PreparedStatement psVisit = con.prepareStatement(UPDATE_QUERY);
        ) {
            Person client = visit.getClient();
            setQueryClient(psClient, client);
            psClient.setLong(5, client.getId());
            psClient.executeUpdate();

            visit.setClient(client);
            setQueryVisit(psVisit, visit);
            psVisit.setLong(6, visit.getId());
            psVisit.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void delete(Visit visit) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(DELETE_QUERY)
        ) {
            ps.setLong(1, visit.getId());

            ps.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }
}