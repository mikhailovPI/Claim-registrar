package ru.mikhailov.claimregistrar.user.service;

import ru.mikhailov.claimregistrar.user.dto.UserAdminDto;
import ru.mikhailov.claimregistrar.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    void deleteUserById(Long adminId, Long userId);

    List<UserAdminDto> getAllUsers(Long adminId, int from, int size);

    UserAdminDto getUserByName(String namePart);

    UserAdminDto assignRightsOperator(Long adminId, Long userId);
}
