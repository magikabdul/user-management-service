package cloud.cholewa.user_management;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.Location;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@ActiveProfiles("test")
public class BaseContainer {

    private static final GenericContainer<?> postgres = new GenericContainer<>(
            DockerImageName.parse("postgres:13"))
            .withEnv("POSTGRES_PASSWORD", "password")
            .withEnv("POSTGRES_DB", "test")
            //.withEnv("spring.datasource.url=", "jdbc:postgresql://localhost:5432/crm")
            .withExposedPorts(5432);

    public static void startPostgres() {
        postgres.start();

        final ClassicConfiguration flywayConfiguration = new ClassicConfiguration();
        flywayConfiguration.setDataSource("jdbc:tc:postgresql:13:///test", "sa", "password");
        flywayConfiguration.setLocations(new Location("db/migration"));
        final Flyway flyway = new Flyway(flywayConfiguration);
        flyway.migrate();
    }



    public static void stopPostgres() {
        postgres.stop();
    }
}


//r2dbc:tc:postgresql:///databasename?TC_IMAGE_TAG=9.6.8