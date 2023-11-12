package cloud.cholewa.user_management.error.processor;

import cloud.cholewa.user_management.error.model.ErrorMessage;
import cloud.cholewa.user_management.error.model.Messages;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebInputException;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class ServerWebInputExceptionProcessor implements ExceptionProcessor {
    @Override
    public Messages apply(Throwable throwable) {

        final Throwable cause = ((ServerWebInputException) throwable).getMostSpecificCause();

        ErrorMessage errorMessage = ErrorMessage.builder()
                .title("Unknown type of ServerWebInputException")
                .build();

        if (cause instanceof ServerWebInputException) {
            return Messages.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .errors(Collections.singleton(
                            handleServerWebInputException((ServerWebInputException) cause, errorMessage)
                    ))
                    .build();
        } else if (cause instanceof DecodingException) {
            return Messages.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .errors(handleDecodingException(cause, errorMessage))
                    .build();
        } else {
            return Messages.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .errors(handleUnknownTypeOfServerWebInputException(cause, errorMessage))
                    .build();
        }
    }

    private ErrorMessage handleServerWebInputException(
            final ServerWebInputException ex,
            final ErrorMessage errorMessage
    ) {
        return Optional.ofNullable(ex.getMethodParameter())
                .map(methodParameter -> {
                    errorMessage.setTitle("aaaa");
                    errorMessage.setDetail("det");
                    return errorMessage;
                })
                .orElse(errorMessage);
    }

    private Set<ErrorMessage> handleUnknownTypeOfServerWebInputException(
            final Throwable throwable,
            final ErrorMessage errorMessage
    ) {
        errorMessage.setDetail(throwable.fillInStackTrace().toString());

        return Collections.singleton(errorMessage);
    }

    private Set<ErrorMessage> handleDecodingException(
            final Throwable throwable,
            final ErrorMessage errorMessage
    ) {
        errorMessage.setTitle("Missing request body");
        errorMessage.setDetail(throwable.fillInStackTrace().toString());

        return Collections.singleton(errorMessage);
    }
}
