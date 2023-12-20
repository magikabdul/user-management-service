package cloud.cholewa.user_management.user.create;

import cloud.cholewa.user_management.api.model.UserReply;
import cloud.cholewa.user_management.api.model.UserRequest;
import cloud.cholewa.user_management.db.model.UserEntity;
import cloud.cholewa.user_management.db.repository.CreateUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceImplTest {

    private final UserRequest userRequest = new UserRequest("login", "password");

    @Mock
    CreateUserRepository repository;

    @InjectMocks
    CreateUserServiceImpl sut;

    @Test
    void should_create_user() {
        Mockito.when(repository.save(Mockito.any()))
                .thenReturn(Mono.just(UserEntity.builder().login("login").build()));

        sut.createUser(userRequest)
                .as(StepVerifier::create)
                .expectNext(ResponseEntity.status(HttpStatus.CREATED).body(new UserReply("login")))
                .verifyComplete();
    }

    @Test
    void should_not_create_user_when_login_exists() {
        Mockito.when(repository.save(Mockito.any())).thenReturn(Mono.error(IllegalArgumentException::new));

        sut.createUser(userRequest)
                .as(StepVerifier::create)
                .expectError()
                .verify();
    }
}