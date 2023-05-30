package ru.mikhailov.claimregistrar.user.service;

import ru.mikhailov.claimregistrar.user.model.User;

import java.util.List;

public interface UserService {

    User createUser(User user);

    void deleteUserById(Long userId);

    List<User> getAllUsers(int from, int size);

    User getUserByName(String text);

    User assignRightsOperator(Long userId);
}
