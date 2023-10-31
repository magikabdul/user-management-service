package cloud.cholewa.user_management.db.model.mapper;

import cloud.cholewa.user_management.api.model.UserRequest;
import cloud.cholewa.user_management.db.model.UserEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserEntityMapper {

    public static UserEntity mapFrom(UserRequest userRequest) {
        return UserEntity.builder().login(userRequest.login()).password(userRequest.password()).build();
    }
}
