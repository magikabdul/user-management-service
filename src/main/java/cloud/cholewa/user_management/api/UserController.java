package cloud.cholewa.user_management.api;

import cloud.cholewa.user_management.api.model.UserReply;
import cloud.cholewa.user_management.api.model.UserRequest;
import cloud.cholewa.user_management.api.model.UserRequestUpdate;
import cloud.cholewa.user_management.user.authenticate.AuthenticateUserService;
import cloud.cholewa.user_management.user.create.CreateUserService;
import cloud.cholewa.user_management.user.delete.DeleteUserService;
import cloud.cholewa.user_management.user.update.UpdateUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
class UserController {

    private final CreateUserService createUserService;
    private final AuthenticateUserService authenticateUserService;
    private final DeleteUserService deleteUserService;
    private final UpdateUserService updateUserService;

    @PostMapping("/register")
    Mono<ResponseEntity<UserReply>> register(@Valid @RequestBody final UserRequest userRequest) {
        log.info("Invoked endpoint 'register'");
        return createUserService.createUser(userRequest);
    }

    @PostMapping("/login")
    Mono<ResponseEntity<String>> login(@Valid @RequestBody final UserRequest userRequest) {
        log.info("Invoked endpoint 'login'");
        return authenticateUserService.login(userRequest);
    }

    @PatchMapping("/update/{login}")
    Mono<ResponseEntity<UserReply>> update(@PathVariable String login, @RequestBody UserRequestUpdate userRequestUpdate) {
        log.info("Invoked endpoint 'update'");
        return updateUserService.updateUser(login, userRequestUpdate);
    }

    @DeleteMapping("/delete/{login}")
    Mono<ResponseEntity<Void>> delete(@PathVariable String login) {
        log.info("Invoked endpoint 'delete'");
        return deleteUserService.deleteUser(login);
    }
}
