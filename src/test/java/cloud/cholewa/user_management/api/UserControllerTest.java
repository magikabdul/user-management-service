package cloud.cholewa.user_management.api;

import cloud.cholewa.user_management.api.model.UserReply;
import cloud.cholewa.user_management.api.model.UserRequest;
import cloud.cholewa.user_management.api.model.UserRequestUpdate;
import cloud.cholewa.user_management.filter.AuthorizationFilter;
import cloud.cholewa.user_management.user.authenticate.AuthenticateUserService;
import cloud.cholewa.user_management.user.create.CreateUserService;
import cloud.cholewa.user_management.user.delete.DeleteUserService;
import cloud.cholewa.user_management.user.update.UpdateUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

@WebFluxTest(
        controllers = UserController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthorizationFilter.class)
)
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
    void should_return_created_when_user_register() {
        Mockito.when(createUserService.createUser(Mockito.any()))
                .thenReturn(Mono.just(new ResponseEntity<>(HttpStatus.CREATED)));

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/users/register")
                        .build()
                )
                .body(BodyInserters.fromValue(new UserRequest("login", "pass")))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserReply.class);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("userTestData")
    void should_return_error_when_user_register(
            final String name,
            final UserRequest userRequest
    ) {
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/users/register").build())
                .body(BodyInserters.fromValue(
                        userRequest == null
                                ? BodyInserters.fromProducer(Mono.empty(), String.class)
                                : BodyInserters.fromValue(userRequest)
                ))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void should_return_ok_when_user_login() {
        Mockito.when(authenticateUserService.login(Mockito.any()))
                .thenReturn(Mono.just(ResponseEntity.ok("")));

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/users/login").build())
                .body(BodyInserters.fromValue(
                        new UserRequest("login", "pass")
                ))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("userTestData")
    void should_return_error_when_user_login(
            final String name,
            final UserRequest userRequest
    ) {
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/users/login").build())
                .body(BodyInserters.fromValue(
                        userRequest == null
                                ? BodyInserters.fromProducer(Mono.empty(), String.class)
                                : BodyInserters.fromValue(userRequest)
                ))
                .exchange()
                .expectStatus().isBadRequest();

        Mockito.verify(authenticateUserService, Mockito.times(0)).login(Mockito.any());
    }

    private static Stream<Arguments> userTestData() {
        return Stream.of(
                Arguments.of(
                        "missing body",
                        null
                ),
                Arguments.of(
                        "missing login field",
                        new UserRequest(null, "pass")
                ),
                Arguments.of(
                        "missing password field",
                        new UserRequest("log", null)
                )
        );
    }

    @Test
    void should_return_ok_after_user_updated() {
        Mockito.when(updateUserService.updateUser(Mockito.any(), Mockito.any()))
                .thenReturn(Mono.just(new ResponseEntity<>(HttpStatus.OK)));


        webTestClient.patch()
                .uri(uriBuilder -> uriBuilder.path("/users/update/userLogin").build())
                .body(BodyInserters.fromValue(
                        new UserRequestUpdate(null, true)
                ))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void should_return_bad_request_when_user_update_body_is_missing() {

        webTestClient.patch()
                .uri(uriBuilder -> uriBuilder.path("/users/update/userLogin").build())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void should_return_noContent_after_deleting_user() {
        Mockito.when(deleteUserService.deleteUser(Mockito.any()))
                .thenReturn(Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)));

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/users/delete/userLogin").build())
                .exchange()
                .expectStatus().isNoContent();
    }
}