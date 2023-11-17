package cloud.cholewa.user_management.user.authenticate;

import cloud.cholewa.user_management.api.model.UserRequest;
import cloud.cholewa.user_management.db.repository.AuthenticateUserRepository;
import cloud.cholewa.user_management.error.UserException;
import cloud.cholewa.user_management.security.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
class AuthenticateUserServiceImpl implements AuthenticateUserService {

    private final AuthenticateUserRepository repository;
    private final JwtTokenService tokenService;

    @Override
    public Mono<ResponseEntity<String>> login(UserRequest userRequest) {

        return repository.findByLoginAndPassword(userRequest.login(), userRequest.password())
                .switchIfEmpty(Mono.error(new UserException("Invalid login or password")))
                .map(userEntity -> ResponseEntity.ok(tokenService.generateToken(userRequest.login())));
    }
}
