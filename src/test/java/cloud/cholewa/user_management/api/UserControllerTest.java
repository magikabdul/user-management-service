package cloud.cholewa.user_management.api;

import cloud.cholewa.user_management.api.model.UserReply;
import cloud.cholewa.user_management.user.authenticate.AuthenticateUserService;
import cloud.cholewa.user_management.user.create.CreateUserService;
import cloud.cholewa.user_management.user.delete.DeleteUserService;
import cloud.cholewa.user_management.user.update.UpdateUserService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(UserController.class)
class UserControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    CreateUserService createUserService;

    @MockBean
    AuthenticateUserService authenticateUserService;

    @MockBean
    DeleteUserService deleteUserService;

    @MockBean
    UpdateUserService updateUserService;

    @Test
    @Disabled("to fix after finishing implementation")
    void should_return_created_when_register() {
        Mono<ResponseEntity<UserReply>> response = Mono.error(new RuntimeException("any"));
        HttpStatus expectedStatus = HttpStatus.CREATED;
        String eee = "";
//        Mockito.when(createUserService.createUser(Mockito.any())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/users/register")
                        .build()
                )
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class);
    }

    @Test
    @Disabled("to fix after finishing implementation")
    void should_return_not_implemented_when_login() {
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/users/login")
                        .build()
                )
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @Disabled("to fix after finishing implementation")
    void should_return_not_implemented_when_update() {
        webTestClient.patch()
                .uri(uriBuilder -> uriBuilder.path("/users/update")
                        .build()
                )
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @Disabled("to fix after finishing implementation")
    void should_return_not_implemented_when_delete() {
        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/users/delete")
                        .build()
                )
                .exchange()
                .expectStatus().is5xxServerError();
    }
}