package ru.mikhailov.claimregistrar.user.service;

import ru.mikhailov.claimregistrar.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    void deleteUserById(Long adminId, Long userId);

    List<UserDto> getAllUsers(Long adminId, int from, int size);

    UserDto getUserByName(String namePart);

    UserDto assignRightsOperator(Long adminId, Long userId);
}
