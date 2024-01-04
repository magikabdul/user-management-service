package cloud.cholewa.user_management.db.repository;

import cloud.cholewa.user_management.BaseContainer;
import cloud.cholewa.user_management.db.model.UserEntity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import reactor.test.StepVerifier;

import java.util.stream.Stream;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class UserRepositoryTest extends BaseContainer {

    @Autowired
    CreateUserRepository createUserRepository;

    @BeforeAll
    static void init() {
        startPostgres();
    }

    @AfterAll
    static void cleanup() {
        stopPostgres();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("databaseConstraints")
    void should_not_add_user_to_database(
            String name,
            UserEntity userEntity
    ) {
        createUserRepository.save(userEntity)
                .as(StepVerifier::create)
                .expectError(DataIntegrityViolationException.class)
                .verify();

        createUserRepository.findAll()
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }

    private static Stream<Arguments> databaseConstraints() {
        return Stream.of(
                Arguments.of(
                        "missing login and password",
                        UserEntity.builder().build()
                ),
                Arguments.of(
                        "missing login",
                        UserEntity.builder().password("password").build()
                ),
                Arguments.of(
                        "missing password",
                        UserEntity.builder().login("login").build()
                )
        );
    }
}