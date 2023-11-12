package cloud.cholewa.user_management.error.processor;

import cloud.cholewa.user_management.error.model.ErrorMessage;
import cloud.cholewa.user_management.error.model.Messages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.Collections;

@Slf4j
public class DefaultExceptionProcessor implements ExceptionProcessor {
    @Override
    public Messages apply(Throwable throwable) {
        return Messages.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .errors(Collections.singleton(
                        ErrorMessage.builder()
                                .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                                .title("Unhandled error, update processor configuration")
                                .detail(throwable.getMessage())
                                .build()
                ))
                .build();
    }
}
