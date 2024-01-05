package cloud.cholewa.user_management.filter;

import cloud.cholewa.user_management.config.JwtTokenConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.security.sasl.AuthenticationException;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class AuthorizationFilterTest {

    private final static String SECRET = "secretsecretsecretsecretsecretsecret";

    @Mock
    JwtTokenConfig tokenConfig;

    @Mock
    WebFilterChain chain;

    @Mock
    ServerWebExchange exchange;

    @Mock
    ServerHttpRequest request;

    @InjectMocks
    AuthorizationFilter authorizationFilter;

    @Test
    void should_not_invoke_authorisation() {
        Mockito.when(request.getPath()).thenReturn(RequestPath.parse("/login", "/login"));
        Mockito.when(exchange.getRequest()).thenReturn(request);
        Mockito.when(chain.filter(exchange)).thenReturn(Mono.empty());

        authorizationFilter.filter(exchange, chain)
                .as(StepVerifier::create)
                .verifyComplete();

        Mockito.verify(chain, Mockito.times(1)).filter(exchange);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidSamples")
    void should_throw_exception_when_token_is_not_valid(
            String name,
            String headerName,
            String token
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(headerName, token);

        Mockito.when(request.getHeaders()).thenReturn(headers);
        Mockito.when(exchange.getRequest()).thenReturn(request);
        Mockito.when(request.getPath()).thenReturn(RequestPath.parse("/update", "/update"));

        Assertions.assertThrows(
                AuthenticationException.class,
                () -> authorizationFilter.filter(exchange, chain)
        );

        Mockito.verify(chain, Mockito.never()).filter(exchange);
    }

    private static Stream<Arguments> invalidSamples() {


        return Stream.of(
                Arguments.of(
                        "not bearer token",
                        HttpHeaders.AUTHORIZATION,
                        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
                ),
                Arguments.of(
                        "invalid JWT token",
                        HttpHeaders.AUTHORIZATION,
                        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ"
                ),
                Arguments.of(
                        "missing authorization header",
                        "dummy name",
                        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
                )
        );
    }

    @Test
    void should_accept_token() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ1c2VyIG1nbXQgc2VydmljZSIsInN1YiI6InRva2VuIiwibG9naW4iOiJsb2dpbiIsImV4cCI6NjE3MDQ0NjE4MjcsImlhdCI6MTcwNDQ2MTgyN30.QhHhcMcVUG6XWx2I24j2kJjKte81ebmhYbEUQ_tcY9Q");

        Mockito.when(tokenConfig.getSecret()).thenReturn(SECRET);

        Mockito.when(request.getHeaders()).thenReturn(headers);
        Mockito.when(exchange.getRequest()).thenReturn(request);
        Mockito.when(chain.filter(exchange)).thenReturn(Mono.empty());
        Mockito.when(request.getPath()).thenReturn(RequestPath.parse("/update", "/update"));

        authorizationFilter.filter(exchange, chain)
                .as(StepVerifier::create)
                .verifyComplete();

        Mockito.verify(chain, Mockito.times(1)).filter(exchange);
    }
}