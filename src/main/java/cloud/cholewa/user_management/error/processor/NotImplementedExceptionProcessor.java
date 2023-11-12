package cloud.cholewa.user_management.error.processor;

import cloud.cholewa.user_management.error.model.ErrorMessage;
import cloud.cholewa.user_management.error.model.Messages;
import org.springframework.http.HttpStatus;

import java.util.Collections;

public class NotImplementedExceptionProcessor implements ExceptionProcessor {
    @Override
    public Messages apply(Throwable throwable) {
        return Messages.builder()
                .httpStatus(HttpStatus.NOT_IMPLEMENTED)
                .errors(Collections.singleton(
                        ErrorMessage.builder()
                                .title("Not implemented")
                                .detail("Logic for this request has not been implemented yet")
                                .build()
                ))
                .build();
    }
}
