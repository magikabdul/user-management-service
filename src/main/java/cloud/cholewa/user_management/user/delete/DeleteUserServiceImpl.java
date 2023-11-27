package cloud.cholewa.user_management.user.delete;

import cloud.cholewa.user_management.db.repository.DeleteUserRepository;
import cloud.cholewa.user_management.error.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
class DeleteUserServiceImpl implements DeleteUserService {

    private final DeleteUserRepository repository;

    @Override
    public Mono<ResponseEntity<Void>> deleteUser(String login) {
        return repository.findByLoginIgnoreCase(login)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UserException("User not found"))))
                .flatMap(userEntity -> repository.deleteByLogin(userEntity.getLogin()))
                .then(Mono.fromCallable(() -> ResponseEntity.noContent().build()));
    }
}
