package cloud.cholewa.user_management.error;

import cloud.cholewa.user_management.error.model.Messages;
import cloud.cholewa.user_management.error.processor.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import javax.security.sasl.AuthenticationException;
import java.util.Map;

@Slf4j
@Order(-2)
public class GlobalErrorExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final Map<Class<? extends Exception>, ExceptionProcessor> processorMap;

    private final ExceptionProcessor defaultProcessor = new DefaultExceptionProcessor();

    public GlobalErrorExceptionHandler(
            final ErrorAttributes errorAttributes,
            final WebProperties.Resources resources,
            final ApplicationContext applicationContext,
            final ServerCodecConfigurer configurer
    ) {
        super(errorAttributes, resources, applicationContext);

        setMessageWriters(configurer.getWriters());

        processorMap = Map.ofEntries(
                Map.entry(AuthenticationException.class, new AuthenticationExceptionProcessor()),
                Map.entry(NotImplementedException.class, new NotImplementedExceptionProcessor()),
                Map.entry(ServerWebInputException.class, new ServerWebInputExceptionProcessor())
        );
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(
                RequestPredicates.all(), this::generateErrorResponse
        );
    }

    Mono<ServerResponse> generateErrorResponse(final ServerRequest request) {
        Throwable throwable = getError(request);

        Messages errorMessage = processorMap.getOrDefault(throwable.getClass(), defaultProcessor).apply(throwable);

        return ServerResponse
                .status(errorMessage.getHttpStatus())
                .body(BodyInserters.fromValue(errorMessage));
    }
}
