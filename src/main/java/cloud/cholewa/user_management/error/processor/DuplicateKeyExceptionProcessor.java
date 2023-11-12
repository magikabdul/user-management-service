package cloud.cholewa.user_management.error.processor;

import cloud.cholewa.user_management.error.model.ErrorMessage;
import cloud.cholewa.user_management.error.model.Messages;
import org.springframework.http.HttpStatus;

import java.util.Collections;

public class DuplicateKeyExceptionProcessor implements ExceptionProcessor {
    @Override
    public Messages apply(Throwable throwable) {
        return Messages.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .errors(Collections.singleton(
                                ErrorMessage.builder()
                                        .title("Duplicate key exception")
                                        .detail(throwable.getCause().getLocalizedMessage())
                                        .build()
                        )
                )
                .build();
    }
}
