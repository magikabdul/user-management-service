package cloud.cholewa.user_management.user.update;

import cloud.cholewa.user_management.api.model.UserReply;
import cloud.cholewa.user_management.api.model.UserRequestUpdate;
import cloud.cholewa.user_management.db.model.UserEntity;
import cloud.cholewa.user_management.db.repository.UpdateUserRepository;
import cloud.cholewa.user_management.error.UserException;
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
class UpdateUserServiceImplTest {

    private final static String EXISTING_USER = "existingUser";
    private final static String NON_EXISTING_USER = "nonExistingUser";
    private final static String PASSWORD = "password";

    @Mock
    UpdateUserRepository repository;

    @InjectMocks
    UpdateUserServiceImpl sut;

    @Test
    void should_update_user_and_return_ok() {
        Mockito.when(repository.findByLoginIgnoreCase(EXISTING_USER))
                .thenReturn(Mono.just(UserEntity
                        .builder()
                        .id(1L)
                        .login(EXISTING_USER)
                        .password(PASSWORD)
                        .build()));

        Mockito.when(repository.save(Mockito.any()))
                .thenReturn(Mono.just(UserEntity.builder().login(EXISTING_USER).build()));

        sut.updateUser(EXISTING_USER, new UserRequestUpdate(null, true))
                .as(StepVerifier::create)
                .expectNext(ResponseEntity.ok(new UserReply(EXISTING_USER)))
                .verifyComplete();

        Mockito.verify(repository, Mockito.times(1)).findByLoginIgnoreCase(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void should_not_update_when_user_not_found() {
        Mockito.when(repository.findByLoginIgnoreCase(NON_EXISTING_USER))
                .thenReturn(Mono.empty());

        sut.updateUser(NON_EXISTING_USER, new UserRequestUpdate(null, false))
                .as(StepVerifier::create)
                .expectError(UserException.class)
                .verify();

        Mockito.verify(repository, Mockito.times(1)).findByLoginIgnoreCase(Mockito.any());
        Mockito.verify(repository, Mockito.times(0)).save(Mockito.any());
    }
}