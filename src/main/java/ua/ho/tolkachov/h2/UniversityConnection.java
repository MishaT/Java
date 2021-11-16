package ua.ho.tolkachov.h2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class UniversityConnection {
    private static final String CONFIG_FILE = "config.properties";
    private static final String DB_URL_PROPERTY = "db.url";
    private static final String DB_USER_PROPERTY = "db.user";
    private static final String DB_PASSWORD_PROPERTY = "db.password";
    private static final String DB_DRIVER_PROPERTY = "db.driver";

    private static UniversityConnection instance;
    private static JdbcTemplate jdbcTemplate = null;

    private final Properties credentialProperties;

    public static JdbcTemplate getConnection() throws IOException {
        if (instance == null) {
            instance = new UniversityConnection();
        }

        if (jdbcTemplate == null) {
            jdbcTemplate = instance.createConnection();
        }
        return jdbcTemplate;
    }

    private UniversityConnection() throws IOException {
        credentialProperties = loadCredentials(CONFIG_FILE);
    }

    private JdbcTemplate createConnection() {
        String dbUrl = credentialProperties.getProperty(DB_URL_PROPERTY);
        String username = credentialProperties.getProperty(DB_USER_PROPERTY);
        String password = credentialProperties.getProperty(DB_PASSWORD_PROPERTY);
        String driver = credentialProperties.getProperty(DB_DRIVER_PROPERTY);

        System.out.println("\n------------------------------");
        System.out.println(dbUrl + "\n" + username + "\n" + driver);
        System.out.println("------------------------------");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return new JdbcTemplate(dataSource);
    }

    private Properties loadCredentials(String filename) throws IOException {
        Properties cred = new Properties();

        try (InputStream input = UniversityConnection.class.getClassLoader().getResourceAsStream(filename)) {
            cred.load(input);
        }
        return cred;
    }
}
