package cloud.cholewa.user_management.user.authenticate;

import cloud.cholewa.user_management.api.model.UserRequest;
import cloud.cholewa.user_management.db.model.UserEntity;
import cloud.cholewa.user_management.db.repository.AuthenticateUserRepository;
import cloud.cholewa.user_management.error.UserException;
import cloud.cholewa.user_management.security.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserServiceImplTest {

    private final static String EXISTING_USER = "existingUser";
    private final static String NON_EXISTING_USER = "NonExistingUser";
    private final static String PASSWORD = "password";

    @Mock
    AuthenticateUserRepository repository;

    @Mock
    JwtTokenService tokenService;

    @InjectMocks
    AuthenticateUserServiceImpl sut;

    @Test
    void should_login_user_and_return_token() {
        Mockito.when(repository.findByLoginAndPassword(EXISTING_USER, PASSWORD))
                .thenReturn(Mono.just(UserEntity.builder().id(1L).build()));

        Mockito.when(tokenService.generateToken(EXISTING_USER)).thenReturn("token");


        sut.login(new UserRequest(EXISTING_USER, PASSWORD))
                .as(StepVerifier::create)
                .expectNext(ResponseEntity.ok("token"))
                .verifyComplete();
    }

    @Test
    void should_return_error_when_user_not_registered() {
        Mockito.when(repository.findByLoginAndPassword(NON_EXISTING_USER, PASSWORD))
                .thenReturn(Mono.empty());

        sut.login(new UserRequest(NON_EXISTING_USER, PASSWORD))
                .as(StepVerifier::create)
                .expectError(UserException.class)
                .verify();

        Mockito.verify(tokenService, Mockito.times(0)).generateToken(Mockito.any());
    }
}