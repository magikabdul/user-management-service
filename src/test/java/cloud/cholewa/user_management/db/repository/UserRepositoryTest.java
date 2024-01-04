package cloud.cholewa.user_management.db.repository;

import cloud.cholewa.user_management.BaseContainer;
import cloud.cholewa.user_management.db.model.UserEntity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@SpringBootTest
class UserRepositoryTest extends BaseContainer {

    private static final String USER_NAME = "login1";
    private static final UserEntity USER = UserEntity.builder().login(USER_NAME).password("password1").build();

    @Autowired
    CreateUserRepository createUserRepository;

    @Autowired
    AuthenticateUserRepository authenticateUserRepository;

    @Autowired
    UpdateUserRepository updateUserRepository;

    @Autowired
    DeleteUserRepository deleteUserRepository;

    @BeforeAll
    static void init() {
        startPostgres();
    }

    @AfterAll
    static void cleanup() {
        stopPostgres();
    }

    @BeforeEach
    void cleanDb() {
        createUserRepository.deleteAll().as(StepVerifier::create).verifyComplete();
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

    @Test
    void should_not_add_user_with_the_same_login() {
        createUserRepository.save(USER)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        createUserRepository.save(USER)
                .as(StepVerifier::create)
                .expectError(DuplicateKeyException.class)
                .verify();

    }

    @Test
    void should_find_user() {
        createUserRepository.save(USER)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        authenticateUserRepository.findByLoginAndPassword("login1", "password1")
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void should_update_user() {
        AtomicReference<Long> userId = new AtomicReference<>();

        createUserRepository.save(USER)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        updateUserRepository.findByLoginIgnoreCase(USER_NAME)
                .as(StepVerifier::create)
                .expectNextMatches(userEntity -> {
                    userId.set(userEntity.getId());
                    return userEntity.getLogin().equals(USER_NAME);
                })
                .verifyComplete();

        updateUserRepository.save(
                        UserEntity.builder().id(userId.get()).login(USER_NAME).password("test").build()
                )
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        updateUserRepository.findAll()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        updateUserRepository.findByLoginIgnoreCase(USER_NAME)
                .as(StepVerifier::create)
                .expectNextMatches(userEntity -> userEntity.getPassword().equals("test"))
                .verifyComplete();
    }

    @Test
    void should_delete_user() {
        deleteUserRepository.findAll()
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();

        createUserRepository.save(USER)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        deleteUserRepository.findByLoginIgnoreCase(USER_NAME)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        deleteUserRepository.deleteByLogin(USER_NAME)
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }
}