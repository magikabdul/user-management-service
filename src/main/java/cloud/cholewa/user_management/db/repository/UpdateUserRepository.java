package cloud.cholewa.user_management.db.repository;

import cloud.cholewa.user_management.db.model.UserEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UpdateUserRepository extends R2dbcRepository<UserEntity, Long> {

    Mono<UserEntity> findByLoginIgnoreCase(String login);
}
