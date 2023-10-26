package cloud.cholewa.user_management.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(UserController.class)
class UserControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void should_return_not_implemented_when_register() {
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/users/register")
                        .build()
                )
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void should_return_not_implemented_when_login() {
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/users/login")
                        .build()
                )
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void should_return_not_implemented_when_update() {
        webTestClient.patch()
                .uri(uriBuilder -> uriBuilder.path("/users/update")
                        .build()
                )
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void should_return_not_implemented_when_delete() {
        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/users/delete")
                        .build()
                )
                .exchange()
                .expectStatus().is5xxServerError();
    }
}