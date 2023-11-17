package cloud.cholewa.user_management.user.delete;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeleteUserServiceImpl implements DeleteUserService {
    @Override
    public Mono<ResponseEntity<Void>> deleteUser(String login) {
        return Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}
