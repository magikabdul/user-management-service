package cloud.cholewa.user_management.config;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Value
@ConfigurationProperties("jwt")
public class JwtTokenConfig {

    String expiration;
}
