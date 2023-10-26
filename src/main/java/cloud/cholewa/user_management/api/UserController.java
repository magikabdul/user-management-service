package cloud.cholewa.user_management.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping("/users")
class UserController {

    @PostMapping("/register")
    Mono<ResponseEntity<Void>> register() {
        log.info("Invoked endpoint 'register'");
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build());
    }

    @PostMapping("/login")
    Mono<ResponseEntity<Void>> login() {
        log.info("Invoked endpoint 'login'");
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build());
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
