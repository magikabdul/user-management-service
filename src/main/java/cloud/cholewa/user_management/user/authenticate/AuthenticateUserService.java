package cloud.cholewa.user_management.user.authenticate;

import cloud.cholewa.user_management.api.model.UserRequest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface AuthenticateUserService {

    Mono<ResponseEntity<String>> login(UserRequest userRequest);
}
