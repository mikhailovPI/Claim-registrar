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
    public List<User> getUsersList(int from, int size) {
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        return userRepository.findAll(pageRequest)
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public User createUser(User user) {
        validationBodyUser(user);
        userRepository.findByNameOrderByName()
                .stream()
                .filter(name -> name.equals(user.getName()))
                .forEachOrdered(name -> {
                    throw new ConflictingRequestException(
                            String.format("Пользователь с именем %s - уже существует", name));
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
    public List<User> getAllUsers() {
        return null;
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
