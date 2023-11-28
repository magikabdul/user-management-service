package cloud.cholewa.user_management.user.update;

import cloud.cholewa.user_management.api.model.UserReply;
import cloud.cholewa.user_management.api.model.UserRequestUpdate;
import cloud.cholewa.user_management.db.model.UserEntity;
import cloud.cholewa.user_management.db.repository.UpdateUserRepository;
import cloud.cholewa.user_management.error.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateUserServiceImpl implements UpdateUserService {

    private final UpdateUserRepository repository;

    @Override
    public Mono<ResponseEntity<UserReply>> updateUser(String login, UserRequestUpdate userRequestUpdate) {
        return repository.findByLoginIgnoreCase(login)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UserException("user not found"))))
                .flatMap(userEntity ->
                        repository.save(UserEntity.builder()
                                .id(userEntity.getId())
                                .login(userEntity.getLogin())
                                .password(Optional.ofNullable(userRequestUpdate.password()).orElse(userEntity.getPassword()))
                                .build())
                )
                .map(userEntity -> new ResponseEntity<>(new UserReply(userEntity.getLogin()), HttpStatus.OK));
    }
}
