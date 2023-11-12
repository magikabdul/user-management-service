package cloud.cholewa.user_management.db.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
@Value
@Builder
public class UserEntity {

    @Id
    Long id;

    @NotNull
    String login;

    @NotNull
    String password;
}
