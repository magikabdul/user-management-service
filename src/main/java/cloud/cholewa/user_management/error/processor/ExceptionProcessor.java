package cloud.cholewa.user_management.error.processor;

import cloud.cholewa.user_management.error.model.Messages;

@FunctionalInterface
public interface ExceptionProcessor {

    Messages apply(final Throwable throwable);
}
