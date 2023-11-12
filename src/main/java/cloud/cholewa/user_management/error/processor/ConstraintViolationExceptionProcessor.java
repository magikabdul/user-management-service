package cloud.cholewa.user_management.error.processor;

import cloud.cholewa.user_management.error.model.ErrorMessage;
import cloud.cholewa.user_management.error.model.Messages;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;

import java.util.stream.Collectors;

//TODO add implementation when will be needed
public class ConstraintViolationExceptionProcessor implements ExceptionProcessor {
    @Override
    public Messages apply(Throwable throwable) {
        return Messages.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .errors(
                        ((ConstraintViolationException) throwable).getConstraintViolations().stream()
                                .map(constraintViolation -> getErrorMessages(constraintViolation, throwable))
                                .collect(Collectors.toSet())
                )
                .build();
    }

    private ErrorMessage getErrorMessages(
            final ConstraintViolation<?> constraintViolation,
            final Throwable throwable) {
        return ErrorMessage.builder()
                .title("ttt")
                .detail(constraintViolation.getMessage())
                .build();
    }
}
