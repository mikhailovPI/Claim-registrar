package ru.mikhailov.claimregistrar.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mikhailov.claimregistrar.config.PageRequestOverride;
import ru.mikhailov.claimregistrar.exception.ConflictingRequestException;
import ru.mikhailov.claimregistrar.exception.NotFoundException;
import ru.mikhailov.claimregistrar.request.repository.RequestRepository;
import ru.mikhailov.claimregistrar.user.dto.UserDto;
import ru.mikhailov.claimregistrar.user.mapper.UserMapper;
import ru.mikhailov.claimregistrar.user.model.Role;
import ru.mikhailov.claimregistrar.user.model.User;
import ru.mikhailov.claimregistrar.user.model.UserRole;
import ru.mikhailov.claimregistrar.user.repository.RoleRepository;
import ru.mikhailov.claimregistrar.user.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.mikhailov.claimregistrar.config.Validation.validationBodyUser;
import static ru.mikhailov.claimregistrar.user.mapper.UserMapper.toUser;
import static ru.mikhailov.claimregistrar.user.mapper.UserMapper.toUserDto;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RequestRepository requestRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        validationBodyUser(toUser(userDto));
        User user = toUser(userDto);
        if (userRepository.findByNameOrderByEmail()
                .stream()
                .noneMatch(email -> email.equals(userDto.getEmail()))) {
            Set<Role> roles = new HashSet<>();
            Set<Role> roleUserDto = userDto.getUserRole();

            if (roleRepository.findAll().isEmpty()) {
                user.setUserRole(roleUserDto);
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
                user = userRepository.save(user);
                return toUserDto(user);
            }
            for (Role role : roleUserDto) {
                Role roleFromDataBase = roleRepository.findByName(role.getName());
                if (roleFromDataBase != null) {
                    roles.add(roleFromDataBase);
                } else {
                    roles.add(role);
                }
            }
            user.setUserRole(roles);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user = userRepository.save(user);
        } else {
            throw new ConflictingRequestException(
                    String.format("Пользователь с email:  %s - уже существует!", userDto.getEmail()));
        }
        return toUserDto(user);
    }

    //TODO методы для админа
    @Override
    public List<UserDto> getAllUsers(Long adminId, int from, int size) {
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        User admin = validationUser(adminId);
        admin.getUserRole()
                .stream()
                .filter(role -> !role.getName().equals(String.valueOf(UserRole.ADMIN)))
                .forEach(role -> {
                    throw new NotFoundException(
                            String.format("Пользователь %s не может просматривать всех пользователей, " +
                                            "т.к. не является %s!",
                                    admin.getName(),
                                    UserRole.ADMIN));
                });
        return userRepository.findAll(pageRequest)
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserByName(String text) {
        return toUserDto(userRepository.findFirstUserByNamePart(text));
    }

    @Override
    @Transactional
    public UserDto assignRightsOperator(Long adminId, Long userId) {
        User admin = validationUser(adminId);
        User user = validationUser(userId);
        admin.getUserRole()
                .stream()
                .filter(role -> !role.getName().equals(String.valueOf(UserRole.ADMIN)))
                .forEach(role -> {
                    throw new NotFoundException(
                            String.format("Пользователь %s не может назначить новую роль пользователю, " +
                                            "т.к. не является %s!",
                                    admin.getName(),
                                    UserRole.ADMIN));
                });
        user.getUserRole()
                .stream()
                .filter(role -> role.getName().equals(String.valueOf(UserRole.USER)))
                .forEach(role -> role.setName(String.valueOf(UserRole.OPERATOR)));
        userRepository.save(user);
        return toUserDto(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long adminId, Long userId) {
        User admin = validationUser(adminId);
        validationUser(userId);
        admin.getUserRole()
                .stream()
                .filter(role -> !role.getName().equals(String.valueOf(UserRole.ADMIN)))
                .forEach(role -> {
                    throw new NotFoundException(
                            String.format("Пользователь %s не может удалить пользователя, " +
                                            "т.к. не является %s!",
                                    admin.getName(),
                                    UserRole.ADMIN));
                });
        requestRepository.deleteRequestsByUserId(userId);
        userRepository.deleteById(userId);

    }

    private User validationUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь %s не существует!", userId)));
    }

}

