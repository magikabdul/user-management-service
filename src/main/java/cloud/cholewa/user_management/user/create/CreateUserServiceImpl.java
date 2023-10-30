package cloud.cholewa.user_management.user.create;

import cloud.cholewa.user_management.api.model.UserRequest;
import cloud.cholewa.user_management.api.model.UserReply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
class CreateUserServiceImpl implements CreateUserService {
    @Override
    public Mono<ResponseEntity<UserReply>> createUser(UserRequest userRegister) {
        return Mono.empty();
    }
}
