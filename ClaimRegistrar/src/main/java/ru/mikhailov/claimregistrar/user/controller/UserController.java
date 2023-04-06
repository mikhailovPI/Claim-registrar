package ru.mikhailov.claimregistrar.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.mikhailov.claimregistrar.user.model.User;
import ru.mikhailov.claimregistrar.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping(path = "/users")
    public List<User> getUsersList(
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        log.info("URL: /users. GetMapping/Получение всех пользователей/getUsersList");
        return userService.getUsersList(from, size);
    }

    @PostMapping(path = "/registration/users")
    public User createUser(@RequestBody User user) {
        log.info("URL: /users. PostMapping/Создание пользователя/createUser");
        return userService.createUser(user);
    }

    @DeleteMapping(path = "/users/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        log.info("URL: /users/{userId}. DeleteMapping/Удаление пользователя с id: " + userId + "/deleteUserById");
        userService.deleteUserById(userId);
    }


}
