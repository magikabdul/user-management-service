package cloud.cholewa.user_management.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimNames;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
class TokenGeneratorTest {

    private final static String SECRET = "secretsecretsecretsecretsecretsecret";

    @SneakyThrows
    @Test

    void generateToken() {

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .build();

        SignedJWT jwt = new SignedJWT(
                header,
                getJwtClaimSet("login", Instant.now(), Duration.ofMinutes(Integer.parseInt("1000000000")))
        );

        jwt.sign(new MACSigner(SECRET));

        System.out.println(jwt.serialize());
        assertTrue(true);
    }

    private JWTClaimsSet getJwtClaimSet(
            final @NotNull String login,
            final Instant now,
            final Duration expiration
    ) {

        return new JWTClaimsSet.Builder()
                .subject("token")
                .issuer("user mgmt service")
                .claim("login", login)
                .claim(JWTClaimNames.ISSUED_AT, (now.toEpochMilli() / 1000))
                .claim(JWTClaimNames.EXPIRATION_TIME, now.toEpochMilli() / 1000 + expiration.toSeconds())
                .build();
    }
}
