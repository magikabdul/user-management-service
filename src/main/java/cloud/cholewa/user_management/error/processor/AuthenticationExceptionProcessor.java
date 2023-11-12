package cloud.cholewa.user_management.error.processor;

import cloud.cholewa.user_management.error.model.ErrorMessage;
import cloud.cholewa.user_management.error.model.Messages;
import org.springframework.http.HttpStatus;

import java.util.Collections;

public class AuthenticationExceptionProcessor implements ExceptionProcessor {
    @Override
    public Messages apply(Throwable throwable) {
        return Messages.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .errors(Collections.singleton(
                        ErrorMessage.builder()
                                .title("User not authorized")
                                .detail("Invalid authorization token")
                                .build()
                ))
                .build();
    }
}
