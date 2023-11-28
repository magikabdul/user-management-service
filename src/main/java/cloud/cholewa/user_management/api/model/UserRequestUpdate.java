package cloud.cholewa.user_management.api.model;

public record UserRequestUpdate(String password, boolean isActive) {
}
