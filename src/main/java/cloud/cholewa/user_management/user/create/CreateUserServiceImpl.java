package cloud.cholewa.user_management.user.create;

import cloud.cholewa.user_management.api.model.UserReply;
import cloud.cholewa.user_management.api.model.UserRequest;
import cloud.cholewa.user_management.db.model.UserEntity;
import cloud.cholewa.user_management.db.model.mapper.UserEntityMapper;
import cloud.cholewa.user_management.db.repository.CreateUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
class CreateUserServiceImpl implements CreateUserService {

    private final CreateUserRepository repository;

    @Override
    public Mono<ResponseEntity<UserReply>> createUser(UserRequest user) {

        Mono<UserEntity> userEntityMono = repository.save(UserEntityMapper.mapFrom(user));

        return userEntityMono.map(userEntity -> ResponseEntity.ok(new UserReply(userEntity.getLogin())));

    }
}
