package ru.mikhailov.claimregistrar.user.mapper;

import ru.mikhailov.claimregistrar.user.dto.UserAdminDto;
import ru.mikhailov.claimregistrar.user.dto.UserDto;
import ru.mikhailov.claimregistrar.user.model.User;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getEmail(),
                user.getUserRole()
        );
    }

    public static User toUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getPassword(),
                userDto.getEmail(),
                userDto.getUserRole()
        );
    }

    public static UserAdminDto toUserAdminDto(User user) {
        return new UserAdminDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUserRole()
        );
    }
}
