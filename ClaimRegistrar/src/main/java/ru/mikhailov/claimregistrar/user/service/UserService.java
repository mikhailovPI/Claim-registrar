package ru.mikhailov.claimregistrar.user.service;

import ru.mikhailov.claimregistrar.user.dto.UserDto;
import ru.mikhailov.claimregistrar.user.model.User;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    void deleteUserById(Long userId);

    List<User> getAllUsers(int from, int size);

    User getUserByName(String text);

    User assignRightsOperator(Long userId);
}
