package cloud.cholewa.user_management.config;

import cloud.cholewa.user_management.error.GlobalErrorExceptionHandler;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;

@Configuration
public class ExceptionHandlerConfig {

    @Bean
    public WebProperties.Resources resources(final WebProperties webProperties) {
        return webProperties.getResources();
    }

    @Bean
    @Order(-2)
    public GlobalErrorExceptionHandler globalErrorExceptionHandler(
            final ErrorAttributes errorAttributes,
            final WebProperties.Resources resources,
            final ApplicationContext applicationContext,
            final ServerCodecConfigurer configurer
    ) {
        return new GlobalErrorExceptionHandler(
                errorAttributes,
                resources,
                applicationContext,
                configurer
        );
    }
}
