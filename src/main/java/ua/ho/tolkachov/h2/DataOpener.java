package ua.ho.tolkachov.h2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


public class DataOpener {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/university";
    private static final String DB_USER = "teacher";
    private static final String DB_PASSWORD = "teacher";
    private static final String DB_DRIVER = "org.postgresql.Driver";

    private static final String SQL_CREATE_COURSES =  "CREATE TABLE courses( "
                                                      + "      course_id int NOT NULL "
                                                      + "     ,course_name varchar(100) "
                                                      + "     ,course_description varchar(250) "
                                                      + "  ); "
                                                      + "  ALTER TABLE courses ADD CONSTRAINT PK_courses PRIMARY KEY (course_id); ";

    private static final String SQL_DATA_FOR_COURSES = "INSERT INTO courses(course_id, course_name, course_description)\r\n"
                                                      + "    VALUES(1, 'Art', 'Art')\r\n"
                                                      + "         ,(2, 'Geography', 'Geography')\r\n"
                                                      + "         ,(3, 'History', 'History')\r\n"
                                                      + "         ,(4, 'Literacy', 'Literacy')\r\n"
                                                      + "         ,(5, 'Music', 'Music')\r\n"
                                                      + "         ,(6, 'Science', 'Science')\r\n"
                                                      + "         ,(7, 'Arithmetic', 'Arithmetic')\r\n"
                                                      + "         ,(8, 'Reading', 'Reading')\r\n"
                                                      + "         ,(9, 'Writing', 'Writing')\r\n"
                                                      + "         ,(10,'Math', 'Math');\r\n";
    private static final String SQL_GET_COURSES = "SELECT course_id, course_name, course_description FROM courses";

    private static final String SQL_GET_ROOM = "SELECT name FROM Rooms WHERE Id = ?";

    public static void main(String[] args)  {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DB_DRIVER);
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(DB_USER);
        dataSource.setPassword(DB_PASSWORD);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String groupName = null;

        groupName = jdbcTemplate.queryForObject("SELECT name FROM Rooms WHERE id = ?", String.class, 6);
        System.out.println(groupName);

        JdbcTemplate connection;
        try {
            connection = UniversityConnection.getConnection();
            System.out.println(connection.queryForObject(SQL_GET_ROOM, String.class, 5));
        } catch (IOException e) {
            e.printStackTrace();
        }


        try (Connection db = DriverManager.getConnection("jdbc:h2:mem:")) {
            try (Statement qry = db.createStatement() ) {
                qry.execute(SQL_CREATE_COURSES);
                qry.execute(SQL_DATA_FOR_COURSES);
            }

            try (PreparedStatement qry = db.prepareStatement(SQL_GET_COURSES)) {
                ResultSet rs = qry.executeQuery();
                while (rs.next()) {
                    String text = String.format("%s, %s", rs.getString(1), rs.getString("course_name"));
                    System.out.println(text);
                }
            }

        } catch (SQLException e) {
            System.out.println("DB failure: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
