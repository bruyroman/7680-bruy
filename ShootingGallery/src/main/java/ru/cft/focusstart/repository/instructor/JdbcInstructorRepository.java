package ru.cft.focusstart.repository.instructor;

import ru.cft.focusstart.entity.*;
import ru.cft.focusstart.repository.DataAccessException;
import ru.cft.focusstart.repository.DataSourceProvider;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

import static ru.cft.focusstart.repository.reader.InstructorReader.readInstructor;
import static ru.cft.focusstart.repository.reader.VisitReader.readVisit;
import static ru.cft.focusstart.repository.reader.WeaponReader.readWeapon;


public class JdbcInstructorRepository implements InstructorRepository {

    private static final String GET_QUERY =
            "select inst.\"ID\" INSTRUCTOR_ID, " +
                    "prs.\"ID\" PERSON_ID,  " +
                    "prs.\"SURNAME\" ,  " +
                    "prs.\"NAME\",  " +
                    "prs.\"PATRONYMIC\",  " +
                    "prs.\"BIRTHDATE\", " +
                    "inst.\"CATEGORY\" " +
                    "from public.\"INSTRUCTOR\" inst " +
                    "inner join public.\"PERSON\" prs on prs.\"ID\" = inst.\"PERSON_ID\"";

    private static final String GET_WEAPONS_QUERY =
            "SELECT  wpn.\"ID\" WEAPON_ID," +
                    "wpn.\"INSTRUCTOR_ID\", " +
                    "wpn.\"TYPE\"," +
                    "wpn.\"MODEL\"," +
                    "wpn.\"SERIES\"," +
                    "wpn.\"NUMBER\" " +
                    "FROM \"INSTRUCTOR\" inst " +
                    "inner join public.\"WEAPON\" wpn on wpn.\"INSTRUCTOR_ID\" = inst.\"ID\"";

    private static final String GET_VISITS_QUERY =
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
                    "FROM \"INSTRUCTOR\" inst " +
                    "inner join public.\"VISIT\" vst on vst.\"INSTRUCTOR_ID\" = inst.\"ID\" " +
                    "inner join public.\"PERSON\" prs on prs.\"ID\" = vst.\"CLIENT_ID\"";

    private static final String GET_INSTRUCTOR_BY_FULLNAME_AND_CATEGORY_QUERY =
            GET_QUERY +
                    " WHERE lower(prs.\"SURNAME\"||' '||prs.\"NAME\"||' '||prs.\"PATRONYMIC\") like lower('%' || ? || '%') " +
                    "and lower(CAST(inst.\"CATEGORY\" as text)) like lower('%' || ? || '%')";

    private static final String GET_WEAPONS_BY_INSTRUCTOR_FULLNAME_AND_CATEGORY_QUERY =
            GET_WEAPONS_QUERY +
                    " inner join public.\"PERSON\" prs_ins on prs_ins.\"ID\" = inst.\"PERSON_ID\" " +
                    "WHERE lower(prs_ins.\"SURNAME\"||' '||prs_ins.\"NAME\"||' '||prs_ins.\"PATRONYMIC\") like lower('%' || ? || '%') " +
                    "and lower(CAST(inst.\"CATEGORY\" as text)) like lower('%' || ? || '%')";

    private static final String GET_VISITS_BY_INSTRUCTOR_FULLNAME_AND_CATEGORY_QUERY =
            GET_VISITS_QUERY +
                    " inner join public.\"PERSON\" prs_ins on prs_ins.\"ID\" = inst.\"PERSON_ID\" " +
                    "WHERE lower(prs_ins.\"SURNAME\"||' '||prs_ins.\"NAME\"||' '||prs_ins.\"PATRONYMIC\") like lower('%' || ? || '%') " +
                    "and lower(CAST(inst.\"CATEGORY\" as text)) like lower('%' || ? || '%')";

    private static final String GET_INSTRUCTOR_BY_ID_QUERY =
            GET_QUERY +
                    " WHERE inst.\"ID\" = ?";

    private static final String GET_WEAPONS_BY_INSTRUCTOR_ID_QUERY =
            GET_WEAPONS_QUERY +
                    " WHERE inst.\"ID\" = ?";

    private static final String GET_VISITS_BY_INSTRUCTOR_ID_QUERY =
            GET_VISITS_QUERY +
                    " WHERE inst.\"ID\" = ?";

    private static final String ADD_PERSON_QUERY =
            "INSERT INTO public.\"PERSON\" (\"SURNAME\", \"NAME\", \"PATRONYMIC\", \"BIRTHDATE\") " +
                    "VALUES (?, ?, ?, ?)";

    private static final String ADD_QUERY =
            "INSERT INTO public.\"INSTRUCTOR\" (\"PERSON_ID\", \"CATEGORY\") " +
                    "VALUES (?, CAST(? as instructor_category))";

    private static final String UPDATE_PERSON_QUERY =
            "UPDATE public.\"PERSON\" SET \"SURNAME\"=?, \"NAME\"=?, \"PATRONYMIC\"=?, \"BIRTHDATE\"=? " +
                    "WHERE \"ID\"=?";

    private static final String UPDATE_QUERY =
            "UPDATE public.\"INSTRUCTOR\" SET \"PERSON_ID\"=?, \"CATEGORY\"=CAST(? as instructor_category) " +
                    "WHERE \"ID\"=?";

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
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement psInstructors = con.prepareStatement(GET_INSTRUCTOR_BY_FULLNAME_AND_CATEGORY_QUERY);
                PreparedStatement psWeapons = con.prepareStatement(GET_WEAPONS_BY_INSTRUCTOR_FULLNAME_AND_CATEGORY_QUERY);
                PreparedStatement psVisits = con.prepareStatement(GET_VISITS_BY_INSTRUCTOR_FULLNAME_AND_CATEGORY_QUERY);
        ) {
            psInstructors.setString(1, fullName == null ? "" : fullName);
            psInstructors.setString(2, category == null ? "" : category.toString());

            psWeapons.setString(1, fullName == null ? "" : fullName);
            psWeapons.setString(2, category == null ? "" : category.toString());

            psVisits.setString(1, fullName == null ? "" : fullName);
            psVisits.setString(2, category == null ? "" : category.toString());

            ResultSet rsInstructors = psInstructors.executeQuery();
            ResultSet rsWeapons = psWeapons.executeQuery();
            ResultSet rsVisits = psVisits.executeQuery();

            Collection<Instructor> instructors = readInstructorsList(rsInstructors, rsWeapons, rsVisits);

            return new ArrayList<>(instructors);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    private Collection<Instructor> readInstructorsList(ResultSet rsInstructors, ResultSet rsWeapons, ResultSet rsVisits) throws SQLException {
        Map<Long, Instructor> result = new HashMap<>();
        while (rsInstructors.next()) {
            long id = rsInstructors.getLong("INSTRUCTOR_ID");

            Instructor instructor = result.get(id);
            if (instructor == null) {
                instructor = readInstructor(rsInstructors);
                result.put(id, instructor);
            }
        }

        while (rsWeapons.next()) {
            long id = rsWeapons.getLong("INSTRUCTOR_ID");

            Instructor instructor = result.get(id);
            if (instructor != null) {

                if (instructor.getWeapons() == null) {
                    instructor.setWeapons(new ArrayList<>());
                }

                Weapon weapon = readWeapon(rsWeapons);
                instructor.getWeapons().add(weapon);
            }
        }

        while (rsVisits.next()) {
            long id = rsVisits.getLong("INSTRUCTOR_ID");

            Instructor instructor = result.get(id);
            if (instructor != null) {

                if (instructor.getVisits() == null) {
                    instructor.setVisits(new ArrayList<>());
                }

                Visit visit = readVisit(rsVisits);
                instructor.getVisits().add(visit);
            }
        }

        return result.values();
    }

    @Override
    public Optional<Instructor> getById(Long id) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement psInstructors = con.prepareStatement(GET_INSTRUCTOR_BY_ID_QUERY);
                PreparedStatement psWeapons = con.prepareStatement(GET_WEAPONS_BY_INSTRUCTOR_ID_QUERY);
                PreparedStatement psVisits = con.prepareStatement(GET_VISITS_BY_INSTRUCTOR_ID_QUERY);
        ) {
            psInstructors.setLong(1, id);
            psWeapons.setLong(1, id);
            psVisits.setLong(1, id);

            ResultSet rsInstructors = psInstructors.executeQuery();
            ResultSet rsWeapons = psWeapons.executeQuery();
            ResultSet rsVisits = psVisits.executeQuery();

            Collection<Instructor> instructors = readInstructorsList(rsInstructors, rsWeapons, rsVisits);

            if (instructors.isEmpty()) {
                return Optional.empty();
            } else if (instructors.size() == 1) {
                return Optional.of(instructors.iterator().next());
            } else {
                throw new SQLException("Unexpected result set size");
            }
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void add(Instructor instructor) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement psPersons = con.prepareStatement(ADD_PERSON_QUERY, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement psInstructors = con.prepareStatement(ADD_QUERY, Statement.RETURN_GENERATED_KEYS);
        ) {
            Person person = instructor.getPerson();
            psPersons.setString(1, person.getSurname());
            psPersons.setString(2, person.getName());
            psPersons.setString(3, person.getPatronymic());
            psPersons.setDate(4, new java.sql.Date(person.getBirthdate().getTime()));
            psPersons.executeUpdate();

            ResultSet rsPersons = psPersons.getGeneratedKeys();
            Long idPerson = rsPersons.next() ? rsPersons.getLong(1) : null;
            if (idPerson == null) {
                throw new SQLException("Unexpected error - could not obtain id");
            }
            person.setId(idPerson);

            psInstructors.setLong(1, idPerson);
            psInstructors.setString(2, instructor.getCategory().toString());
            psInstructors.executeUpdate();

            ResultSet rsInstructors = psInstructors.getGeneratedKeys();
            Long idInstructors = rsInstructors.next() ? rsInstructors.getLong(1) : null;
            if (idInstructors == null) {
                throw new SQLException("Unexpected error - could not obtain id");
            }
            instructor.setId(idInstructors);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void update(Instructor instructor) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement psPersons = con.prepareStatement(UPDATE_PERSON_QUERY);
                PreparedStatement psInstructors = con.prepareStatement(UPDATE_QUERY);
        ) {
            Person person = instructor.getPerson();
            psPersons.setString(1, person.getSurname());
            psPersons.setString(2, person.getName());
            psPersons.setString(3, person.getPatronymic());
            psPersons.setDate(4, new java.sql.Date(person.getBirthdate().getTime()));
            psPersons.setLong(5, person.getId());
            psPersons.executeUpdate();

            psInstructors.setLong(1, person.getId());
            psInstructors.setString(2, instructor.getCategory().toString());
            psInstructors.setLong(3, instructor.getId());
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }
}
