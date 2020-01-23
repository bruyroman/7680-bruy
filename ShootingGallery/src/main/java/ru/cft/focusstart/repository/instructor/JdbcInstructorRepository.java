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
                    "clnt.\"ID\" CLIENT_ID," +
                    "clnt.\"SURNAME\"," +
                    "clnt.\"NAME\"," +
                    "clnt.\"PATRONYMIC\"," +
                    "clnt.\"BIRTHDATE\"," +
                    "vst.\"INSTRUCTOR_ID\"," +
                    "vst.\"WEAPON_ID\"," +
                    "vst.\"DATETIME_START\"," +
                    "vst.\"DATETIME_END\" " +
                    "FROM \"INSTRUCTOR\" inst " +
                    "inner join public.\"VISIT\" vst on vst.\"INSTRUCTOR_ID\" = inst.\"ID\" " +
                    "inner join public.\"PERSON\" clnt on clnt.\"ID\" = vst.\"CLIENT_ID\"";

    private static final String INSTRUCTOR_BY_FULLNAME_AND_CATEGORY_WHERE =
            " WHERE lower(prs.\"SURNAME\"||' '||prs.\"NAME\"||' '||COALESCE(prs.\"PATRONYMIC\", '')) like lower('%' || ? || '%') " +
                    "and lower(CAST(inst.\"CATEGORY\" as text)) like lower('%' || ? || '%')";

    private static final String GET_INSTRUCTOR_BY_FULLNAME_AND_CATEGORY_QUERY =
            GET_QUERY + INSTRUCTOR_BY_FULLNAME_AND_CATEGORY_WHERE;

    private static final String GET_WEAPONS_BY_INSTRUCTOR_FULLNAME_AND_CATEGORY_QUERY =
            GET_WEAPONS_QUERY +
                    " inner join public.\"PERSON\" prs on prs.\"ID\" = inst.\"PERSON_ID\" " +
                    INSTRUCTOR_BY_FULLNAME_AND_CATEGORY_WHERE;

    private static final String GET_VISITS_BY_INSTRUCTOR_FULLNAME_AND_CATEGORY_QUERY =
            GET_VISITS_QUERY +
                    " inner join public.\"PERSON\" prs on prs.\"ID\" = inst.\"PERSON_ID\" " +
                    INSTRUCTOR_BY_FULLNAME_AND_CATEGORY_WHERE;

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
                PreparedStatement psPerson = con.prepareStatement(ADD_PERSON_QUERY, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement psInstructor = con.prepareStatement(ADD_QUERY, Statement.RETURN_GENERATED_KEYS);
        ) {
            Person person = instructor.getPerson();
            setQueryPerson(psPerson, person);
            psPerson.executeUpdate();
            person.setId(getGeneratedKeys(psPerson));

            psInstructor.setLong(1, person.getId());
            psInstructor.setString(2, instructor.getCategory().toString());
            psInstructor.executeUpdate();

            instructor.setId(getGeneratedKeys(psInstructor));
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    private void setQueryPerson(PreparedStatement psPerson, Person person) throws SQLException {
        psPerson.setString(1, person.getSurname());
        psPerson.setString(2, person.getName());
        psPerson.setString(3, person.getPatronymic() != null ? person.getPatronymic() : "");
        psPerson.setDate(4, java.sql.Date.valueOf(person.getBirthdate()));
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
    public void update(Instructor instructor) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement psPerson = con.prepareStatement(UPDATE_PERSON_QUERY);
                PreparedStatement psInstructor = con.prepareStatement(UPDATE_QUERY);
        ) {
            Person person = instructor.getPerson();
            setQueryPerson(psPerson, person);
            psPerson.setLong(5, person.getId());
            psPerson.executeUpdate();

            psInstructor.setLong(1, person.getId());
            psInstructor.setString(2, instructor.getCategory().toString());
            psInstructor.setLong(3, instructor.getId());
            psInstructor.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }
}