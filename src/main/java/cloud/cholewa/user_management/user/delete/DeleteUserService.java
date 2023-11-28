package cloud.cholewa.user_management.user.delete;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface DeleteUserService {

    Mono<ResponseEntity<Void>> deleteUser(String login);
}
