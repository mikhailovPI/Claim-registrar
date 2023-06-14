package ru.mikhailov.claimregistrar.user.service;

import org.springframework.web.bind.annotation.PathVariable;
import ru.mikhailov.claimregistrar.user.dto.UserDto;
import ru.mikhailov.claimregistrar.user.model.User;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    void deleteUserById(Long adminId, Long userId);

    List<UserDto> getAllUsers(Long adminId, int from, int size);

    UserDto getUserByName(String text);

    UserDto assignRightsOperator(Long adminId, Long userId);
}
