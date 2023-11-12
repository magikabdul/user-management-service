package cloud.cholewa.user_management.error.processor;

import cloud.cholewa.user_management.error.UserException;
import cloud.cholewa.user_management.error.model.ErrorMessage;
import cloud.cholewa.user_management.error.model.Messages;
import org.springframework.http.HttpStatus;

import java.util.Collections;

public class UserExceptionProcessor implements ExceptionProcessor {
    @Override
    public Messages apply(Throwable throwable) {
        UserException cause = (UserException) throwable;

        return Messages.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .errors(Collections.singleton(
                        ErrorMessage.builder()
                                .title("User Exception")
                                .detail(cause.getMessage())
                                .build()
                ))
                .build();
    }
}
