package cloud.cholewa.user_management.user.create;

import cloud.cholewa.user_management.api.model.UserReply;
import cloud.cholewa.user_management.api.model.UserRequest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface CreateUserService {

    Mono<ResponseEntity<UserReply>> createUser(UserRequest userRegister);
}
