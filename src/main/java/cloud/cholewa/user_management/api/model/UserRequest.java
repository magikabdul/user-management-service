package cloud.cholewa.user_management.api.model;

import jakarta.validation.constraints.NotNull;

public record UserRequest(@NotNull String login, @NotNull String password) {
}
