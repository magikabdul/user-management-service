package cloud.cholewa.user_management.security;

import cloud.cholewa.user_management.config.JwtTokenConfig;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimNames;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtTokenConfig config;

    @SneakyThrows
    public String generateToken(String login) {

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .build();

        SignedJWT jwt = new SignedJWT(
                header,
                getJwtClaimSet(login, Instant.now(), Duration.ofMinutes(Integer.parseInt(config.getExpiration())))
        );

        jwt.sign(new MACSigner(config.getSecret()));

        return jwt.serialize();
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
