package cloud.cholewa.user_management.filter;

import cloud.cholewa.user_management.config.JwtTokenConfig;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import io.micrometer.common.lang.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.security.sasl.AuthenticationException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthorizationFilter implements WebFilter {

    private final JwtTokenConfig config;

    @SneakyThrows
    @Override
    @NonNull
    public Mono<Void> filter(@NonNull final ServerWebExchange exchange, @NonNull final WebFilterChain chain) {
        log.info("Authorization activated");

        if (isAuthorizationRequired(exchange)) {
            exchange.getRequest().getHeaders().entrySet().stream()
                    .filter(entry -> entry.getKey().equalsIgnoreCase(HttpHeaders.AUTHORIZATION))
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .map(value -> value.get(0))
                    .filter(this::isBearerToken)
                    .map(token -> token.replace("Bearer ", ""))
                    .filter(this::isTokenValid)
                    .filter(this::isTokenNotExpired)
                    .orElseThrow(AuthenticationException::new);
        }

        return chain.filter(exchange);
    }

    private boolean isAuthorizationRequired(ServerWebExchange exchange) {
        List<String> unauthorizedEndpoints = List.of("register", "login");
        String path = exchange.getRequest().getPath().value();

        log.info("Request path: {}", path);

        return unauthorizedEndpoints.stream().noneMatch(path::endsWith);
    }

    private boolean isBearerToken(String value) {
        return value.startsWith("Bearer ");
    }

    @SneakyThrows
    private boolean isTokenValid(String value) {
        JWSVerifier verifier = new MACVerifier(config.getSecret());

        return JWSObject.parse(value).verify(verifier);
    }

    @SneakyThrows
    private boolean isTokenNotExpired(String token) {
        Object exp = JWSObject.parse(token)
                .getPayload()
                .toJSONObject()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase("exp"))
                .map(Map.Entry::getValue)
                .findAny()
                .orElseThrow();

        if (exp instanceof Long number) {
            return number > Instant.now().toEpochMilli() / 1000;
        }

        return false;
    }
}
