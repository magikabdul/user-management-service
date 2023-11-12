package cloud.cholewa.user_management.user.authenticate;

import cloud.cholewa.user_management.api.model.UserRequest;
import cloud.cholewa.user_management.db.repository.AuthenticateUserRepository;
import cloud.cholewa.user_management.error.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
class AuthenticateUserServiceImpl implements AuthenticateUserService {
    private final AuthenticateUserRepository repository;

    @Override
    public Mono<ResponseEntity<String>> login(UserRequest userRequest) {
        return Mono.error(new UserException("User not found"));
    }
}
