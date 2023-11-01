package cloud.cholewa.user_management.error.processor;

import cloud.cholewa.user_management.error.model.ErrorMessage;
import cloud.cholewa.user_management.error.model.Messages;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@Slf4j
public class WebClientResponseExceptionProcessor implements ExceptionProcessor {
    @Override
    public Messages apply(Throwable throwable) {
        return Messages.builder()
//                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .errors(Collections.singleton(
                        ErrorMessage.builder()
                                .title("ddddddd")
                                .build()
                ))
                .build();
    }
}
