package cloud.cholewa.user_management.user.update;

import cloud.cholewa.user_management.api.model.UserReply;
import cloud.cholewa.user_management.api.model.UserRequestUpdate;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;


public interface UpdateUserService {

    Mono<ResponseEntity<UserReply>> updateUser(String login, UserRequestUpdate userRequestUpdate);
}
