package cloud.cholewa.user_management.error.processor;

import cloud.cholewa.user_management.error.model.ErrorMessage;
import cloud.cholewa.user_management.error.model.Messages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class WebExchangeBindExceptionProcessor implements ExceptionProcessor {
    @Override
    public Messages apply(Throwable throwable) {
        WebExchangeBindException exception = (WebExchangeBindException) throwable;

        return Messages.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .errors(exception.getAllErrors().stream()
                        .map(objectError -> ErrorMessage.builder()
                                .title("Missing parameter")
                                .detail(Optional.ofNullable(objectError.getArguments())
                                        .map(args -> (DefaultMessageSourceResolvable) args[0] )
                                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                        .orElseGet(() -> {
                                            log.info("No relevant information to extract");
                                            return null;
                                        }))
                                .build()
                        )
                        .collect(Collectors.toSet()))
                .build();
    }
}
