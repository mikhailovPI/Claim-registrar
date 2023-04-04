package ru.mikhailov.claimregistrar.user.service;

import ru.mikhailov.claimregistrar.user.dto.UserDto;
import ru.mikhailov.claimregistrar.user.model.User;

import java.util.List;

public interface UserService {

    List<User> getUsersList(int from, int size);

    User createUser(User user);

    void deleteUserById(Long userId);
}
