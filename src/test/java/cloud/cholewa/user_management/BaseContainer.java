package cloud.cholewa.user_management;

import org.flywaydb.core.Flyway;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ActiveProfiles("test")
public class BaseContainer {

    private static final String IMAGE_NAME = "postgres:13";
    private static final String DATABASE_NAME = "test";
    private static final String USERNAME = "login";
    private static final String PASSWORD = "password";
    private static final int DATABASE_PORT = 5432;

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(IMAGE_NAME)
            .withDatabaseName(DATABASE_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD)
            .withExposedPorts(DATABASE_PORT);

    public static void startPostgres() {
        postgres.start();

        System.setProperty("spring.datasource.host", postgres.getHost());
        System.setProperty("spring.datasource.port", String.valueOf(postgres.getFirstMappedPort()));
        System.setProperty("spring.datasource.database", postgres.getDatabaseName());
        System.setProperty("spring.datasource.username", postgres.getUsername());
        System.setProperty("spring.datasource.password", postgres.getPassword());
        System.setProperty("spring.datasource.sslMode", "DISABLE");

        final Flyway flyway = Flyway.configure()
                .dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
                .locations("classpath:/db/migration")
                .load();

        flyway.migrate();
    }

    public static void stopPostgres() {
        postgres.stop();
    }
}