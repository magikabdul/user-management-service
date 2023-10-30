package cloud.cholewa.user_management.filter;

import io.micrometer.common.lang.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.security.sasl.AuthenticationException;
import java.util.List;

@Component
@Slf4j
class AuthorizationFilter implements WebFilter {
    @SneakyThrows
    @Override
    @NonNull
    public Mono<Void> filter(@NonNull final ServerWebExchange exchange, @NonNull final WebFilterChain chain) {
        log.info("Authorization activated");
        if (isAuthorizationRequired(exchange)) {
            throw new AuthenticationException();
        }
        return chain.filter(exchange);
    }

    private boolean isAuthorizationRequired(ServerWebExchange exchange) {
        List<String> unauthorizedEndpoints = List.of("register", "login");
        String path = exchange.getRequest().getPath().value();

        log.info("Request path: {}", path);

        return unauthorizedEndpoints.stream().noneMatch(path::endsWith);
    }
}
