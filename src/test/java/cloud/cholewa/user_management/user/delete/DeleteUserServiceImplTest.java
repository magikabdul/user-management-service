package cloud.cholewa.user_management.user.delete;

import cloud.cholewa.user_management.db.model.UserEntity;
import cloud.cholewa.user_management.db.repository.DeleteUserRepository;
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
class DeleteUserServiceImplTest {

    private static final String EXISTING_USER = "existingUser";
    private static final String NON_EXISTING_USER = "nonExistingUser";

    @Mock
    DeleteUserRepository repository;

    @InjectMocks
    DeleteUserServiceImpl sut;

    @Test
    void shouldDeleteExistingUser() {
        Mockito.when(repository.findByLoginIgnoreCase(EXISTING_USER))
               .thenReturn(Mono.just(UserEntity.builder().id(1L).login(EXISTING_USER).build()));

        Mockito.when(repository.deleteByLogin(EXISTING_USER))
               .thenReturn(Mono.empty());


        sut.deleteUser(EXISTING_USER)
                .as(StepVerifier::create)
                .expectNext(ResponseEntity.noContent().build())
                .verifyComplete();
    }

    @Test
    void shouldThrowExceptionWhenNoUser() {
        Mockito.when(repository.findByLoginIgnoreCase(NON_EXISTING_USER))
               .thenReturn(Mono.empty());  // Nonexistent user

        sut.deleteUser(NON_EXISTING_USER)
                .as(StepVerifier::create)
                .expectErrorMatches(throwable -> throwable instanceof UserException)
                .verify();
    }


}