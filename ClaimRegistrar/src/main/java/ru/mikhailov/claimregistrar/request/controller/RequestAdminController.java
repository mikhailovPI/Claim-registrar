package ru.mikhailov.claimregistrar.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mikhailov.claimregistrar.request.service.RequestService;
import ru.mikhailov.claimregistrar.user.model.User;
import ru.mikhailov.claimregistrar.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/request/admin")
@Slf4j
public class RequestAdminController {

    private final RequestService requestService;
    private final UserService userService;

    //Посмотреть список всех пользователей
    @GetMapping(path = "/users")
    public List<User> getAllUsers(
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        log.info("URL: /request/admin/users. GetMapping/Получить всех пользователей/getAllUsers");
        return userService.getAllUsers(from, size);
    }

    //Поиск пользователя по имени
    @GetMapping(path = "/user")
    public User getUserByName(
            @RequestParam(name = "text", required = false) String text) {
        log.info("URL: /request/admin/user. GetMapping/Поиск пользователя по имени/getUserByName");
        return userService.getUserByName(text);
    }

    //Назначение прав пользователей
    @PatchMapping(path = "/user/{userId}")
    public User assignRightsOperator(@PathVariable Long userId) {
        log.info("URL: /request/admin/user. GetMapping/Поиск пользователя по имени/getUserByName");
        return userService.assignRightsOperator(userId);
    }

    //Удаление пользователя по id
    @DeleteMapping(path = "/users/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        log.info("URL: /users/{userId}. DeleteMapping/Удаление пользователя с id: " + userId + "/deleteUserById");
        userService.deleteUserById(userId);
    }
}
