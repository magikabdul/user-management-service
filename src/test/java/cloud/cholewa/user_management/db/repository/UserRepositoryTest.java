package cloud.cholewa.user_management.db.repository;

import cloud.cholewa.user_management.BaseContainer;
import cloud.cholewa.user_management.db.model.UserEntity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DataR2dbcTest
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

    @Test
    void should_create_suer_and_add_to_database() {
//        createUserRepository.save(UserEntity.builder().login("john").build()).block();
//
//        createUserRepository.findById(1L).as(StepVerifier::create).expectNext(UserEntity.builder().build()).verifyComplete();

    }
}