package cloud.cholewa.user_management.security;

import cloud.cholewa.user_management.config.JwtTokenConfig;
import com.nimbusds.jose.KeyLengthException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {

    private final static String SECRET = "secretsecretsecretsecretsecretsecret";

    @Mock
    JwtTokenConfig tokenConfig;

    @InjectMocks
    JwtTokenService sut;

    @Test
    void should_generate_token() {
        Mockito.when(tokenConfig.getExpiration()).thenReturn("5");
        Mockito.when(tokenConfig.getSecret()).thenReturn(SECRET);

        String token = sut.generateToken("login");

        assertNotEquals(0, token.length());
    }

    @Test
    void should_not_generate_token() {
        Mockito.when(tokenConfig.getExpiration()).thenReturn("5");
        Mockito.when(tokenConfig.getSecret()).thenReturn("tooShortSecret");

        assertThrowsExactly(
                KeyLengthException.class,
                () -> sut.generateToken("login"),
                "The secret length must be at least 256 bits"
        );
    }
}