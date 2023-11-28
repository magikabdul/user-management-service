package cloud.cholewa.user_management.user.update;

import cloud.cholewa.user_management.api.model.UserReply;
import cloud.cholewa.user_management.api.model.UserRequestUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateUserServiceImpl implements UpdateUserService {
    @Override
    public Mono<ResponseEntity<UserReply>> updateUser(String login, UserRequestUpdate userRequestUpdate) {
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build());
    }
}
