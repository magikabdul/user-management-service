package cloud.cholewa.user_management.api;

import cloud.cholewa.user_management.api.model.UserReply;
import cloud.cholewa.user_management.api.model.UserRequest;
import cloud.cholewa.user_management.error.NotImplementedException;
import cloud.cholewa.user_management.user.create.CreateUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
class UserController {

    private final CreateUserService createUserService;

    @PostMapping("/register")
    Mono<ResponseEntity<UserReply>> register(@Valid @RequestBody final UserRequest userRequest) {
        log.info("Invoked endpoint 'register'");
        return createUserService.createUser(userRequest);
    }

    @PostMapping("/login")
    Mono<ResponseEntity<Void>> login() {
        log.info("Invoked endpoint 'login'");
        return Mono.error(new NotImplementedException());
        //return Mono.just(ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build());
    }

    @PatchMapping("/update")
    Mono<ResponseEntity<Void>> update() {
        log.info("Invoked endpoint 'update'");
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build());
    }

    @DeleteMapping("/delete")
    Mono<ResponseEntity<Void>> delete() {
        log.info("Invoked endpoint 'delete'");
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build());
    }
}
