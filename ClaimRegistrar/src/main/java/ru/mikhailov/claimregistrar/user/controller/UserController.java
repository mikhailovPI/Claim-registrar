package ru.mikhailov.claimregistrar.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.mikhailov.claimregistrar.user.model.User;
import ru.mikhailov.claimregistrar.user.service.UserService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping(path = "/registration/users")
    public User createUser(@RequestBody User user) {
        log.info("URL: /users. PostMapping/Создание пользователя/createUser");
        return userService.createUser(user);
    }
}
