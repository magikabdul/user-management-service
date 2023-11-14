package cloud.cholewa.user_management.security;

import cloud.cholewa.user_management.config.JwtTokenConfig;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.PlainHeader;
import com.nimbusds.jwt.JWTClaimNames;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.PlainJWT;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtTokenConfig config;

    public String generateToken(String login) {
        PlainHeader header = new PlainHeader.Builder()
                .type(JOSEObjectType.JWT)
                .build();

        return new PlainJWT(
                header,
                getJwtClaimSet(login, Instant.now(), Duration.ofMinutes(Integer.parseInt(config.getExpiration())))
        ).serialize();
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
                .claim(JWTClaimNames.ISSUED_AT, now.toEpochMilli())
                .claim(JWTClaimNames.EXPIRATION_TIME, now.toEpochMilli() + expiration.toMillis())
                .build();
    }
}
