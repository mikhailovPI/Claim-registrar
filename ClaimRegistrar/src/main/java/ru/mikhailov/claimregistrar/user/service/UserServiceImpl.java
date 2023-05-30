package ru.mikhailov.claimregistrar.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mikhailov.claimregistrar.config.PageRequestOverride;
import ru.mikhailov.claimregistrar.exception.ConflictingRequestException;
import ru.mikhailov.claimregistrar.user.model.User;
import ru.mikhailov.claimregistrar.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.mikhailov.claimregistrar.config.Validation.validationBodyUser;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User createUser(User user) {
        validationBodyUser(user);
        userRepository.findByNameOrderByEmail()
                .stream()
                .filter(email -> email.equals(user.getEmail()))
                .forEachOrdered(email -> {
                    throw new ConflictingRequestException(
                            String.format("Пользователь с email:  %s - уже существует", email));
                });
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }


    //TODO методы для админа
    @Override
    public List<User> getAllUsers(int from, int size) {
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        return userRepository.findAll(pageRequest)
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public User getUserByName(String text) {
        return null;
    }

    @Override
    public User assignRightsOperator(Long userId) {
        return null;
    }
}
