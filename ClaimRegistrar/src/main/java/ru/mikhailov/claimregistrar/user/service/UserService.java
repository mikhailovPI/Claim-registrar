package ru.mikhailov.claimregistrar.user.service;

import ru.mikhailov.claimregistrar.user.model.User;

import java.util.List;

public interface UserService {

    List<User> getUsersList(int from, int size);

    User createUser(User user);

    void deleteUserById(Long userId);

    List<User> getAllUsers();

    User getUserByName(String text);

    User assignRightsOperator(Long userId);
}
