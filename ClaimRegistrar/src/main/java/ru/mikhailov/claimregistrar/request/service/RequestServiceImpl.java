package ru.mikhailov.claimregistrar.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mikhailov.claimregistrar.config.PageRequestOverride;
import ru.mikhailov.claimregistrar.exception.NotFoundException;
import ru.mikhailov.claimregistrar.request.dto.RequestAllDto;
import ru.mikhailov.claimregistrar.request.dto.RequestDto;
import ru.mikhailov.claimregistrar.request.dto.RequestNewDto;
import ru.mikhailov.claimregistrar.request.dto.RequestUpdateDto;
import ru.mikhailov.claimregistrar.request.mapper.RequestMapper;
import ru.mikhailov.claimregistrar.request.model.Request;
import ru.mikhailov.claimregistrar.request.model.RequestStatus;
import ru.mikhailov.claimregistrar.request.repository.RequestRepository;
import ru.mikhailov.claimregistrar.user.model.Role;
import ru.mikhailov.claimregistrar.user.model.User;
import ru.mikhailov.claimregistrar.user.model.UserRole;
import ru.mikhailov.claimregistrar.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.mikhailov.claimregistrar.request.mapper.RequestMapper.toRequestAllDto;
import static ru.mikhailov.claimregistrar.request.mapper.RequestMapper.toRequestDto;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    //Методы для пользователя
    @Override
    public List<RequestDto> getRequestsByUser(Long userId, Integer sort, int from, int size) {
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        validationUser(userId);
        if (sort.equals(0)) {
            //сортировка по возрастанию даты
            return requestRepository.findRequestsByUserId(userId, pageRequest)
                    .stream()
                    .sorted(Comparator.comparingInt(o -> o.getPublishedOn().getNano()))
                    .map(RequestMapper::toRequestDto)
                    .collect(Collectors.toList());
        } else if (sort.equals(1)) {
            //сортировка по убыванию даты
            return requestRepository.findRequestsByUserId(userId, pageRequest)
                    .stream()
                    .sorted((o1, o2) -> o2.getPublishedOn().getNano() - o1.getPublishedOn().getNano())
                    .map(RequestMapper::toRequestDto)
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException("Сортировка возможна только по возрастанию или убыванию!");
        }
    }

    @Override
    @Transactional
    public RequestAllDto createRequest(RequestNewDto requestDto, Long userId) {
        User user = validationUser(userId);
        user.getUserRole()
                .stream()
                .filter(role -> !role.getName().equals(String.valueOf(UserRole.USER)))
                .forEach(role -> {
                    throw new NotFoundException(
                            String.format("Пользователь %s (роль - %s) не может создавать заявку, т.к. не является %s!",
                                    user.getName(),
                                    user.getUserRole()
                                            .stream()
                                            .map(Role::getName)
                                            .collect(Collectors.toSet()),
                                    UserRole.USER));
                });
        Request request = RequestMapper.toRequest(requestDto);
        request.setPublishedOn(LocalDateTime.now());
        request.setUser(user);
        return toRequestAllDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public RequestDto sendRequest(Long userId, Long requestId) {
        Request request = validationRequest(requestId);
        User user = validationUser(userId);
        if (!request.getUser().getId().equals(userId)) {
            throw new NotFoundException(
                    String.format("Пользователь %s не может отправить чужую заявку!", user.getName()));
        }
        user.getUserRole()
                .stream()
                .filter(role -> !role.getName().equals(String.valueOf(UserRole.USER)))
                .forEach(role -> {
                    throw new NotFoundException(
                            String.format("Пользователь %s (роль - %s) не может отправить заявку," +
                                            " т.к. не является %s!",
                                    user.getName(),
                                    user.getUserRole()
                                            .stream()
                                            .map(Role::getName)
                                            .collect(Collectors.toSet()),
                                    UserRole.USER));
                });
        if (request.getStatus().equals(RequestStatus.DRAFT)) {
            request.setStatus(RequestStatus.SHIPPED);
            return toRequestDto(requestRepository.save(request));
        } else {
            throw new NotFoundException(
                    String.format("Заявка имеет статус %s, а должна иметь статус '%s'!",
                            request.getStatus(),
                            RequestStatus.DRAFT));
        }
    }

    @Override
    public RequestDto updateRequest(Long userId, Long requestId, RequestUpdateDto requestUprateDto) {
        Request request = validationRequest(requestId);
        User user = validationUser(userId);
        user.getUserRole()
                .stream()
                .filter(role -> !role.getName().equals(String.valueOf(UserRole.USER)))
                .forEach(role -> {
                    throw new NotFoundException(
                            String.format("Пользователь %s (роль - %s) не может редактировать заявку, " +
                                            "т.к. не является %s!",
                                    user.getName(),
                                    user.getUserRole()
                                            .stream()
                                            .map(Role::getName)
                                            .collect(Collectors.toSet()),
                                    UserRole.USER));
                });
        if (!request.getUser().getId().equals(userId)) {
            throw new NotFoundException(
                    String.format("Пользователь %s не может редактировать чужую заявку!", user.getName()));
        }
        if (!request.getStatus().equals(RequestStatus.DRAFT)) {
            throw new NotFoundException(
                    String.format("Статус заявки не позволяет ее редактировать. Должен быть статус - '%s'!",
                            RequestStatus.REJECTED));
        }
        request.setText(requestUprateDto.getText());
        return toRequestDto(requestRepository.save(request));
    }

    //Методы для оператора
    @Override
    public List<RequestAllDto> getRequests(Integer sort, int from, int size) {
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        if (sort.equals(0)) {
            //сортировка по убыванию даты
            return requestRepository.findRequestStatusShipped(pageRequest)
                    .stream()
                    .sorted(Comparator.comparingInt(o -> o.getPublishedOn().getNano()))
                    .map(RequestMapper::toRequestAllDto)
                    .collect(Collectors.toList());
        } else if (sort.equals(1)) {
            //сортировка по возрастанию даты
            return requestRepository.findRequestStatusShipped(pageRequest)
                    .stream()
                    .sorted((o1, o2) -> o2.getPublishedOn().getNano() - o1.getPublishedOn().getNano())
                    .map(RequestMapper::toRequestAllDto)
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException("Сортировка возможна только по возрастанию или убыванию!");
        }
    }

    @Override
    public List<RequestDto> getUserRequest(String namePart, Integer sort, int from, int size) {
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        if (sort.equals(0)) {
            //сортировка по убыванию даты
            return requestRepository.findOrdersByUserNamePart(namePart, pageRequest)
                    .stream()
                    .sorted(Comparator.comparingInt(o -> o.getPublishedOn().getNano()))
                    .map(RequestMapper::toRequestDto)
                    .collect(Collectors.toList());
        } else if (sort.equals(1)) {
            //сортировка по возрастанию даты
            return requestRepository.findOrdersByUserNamePart(namePart, pageRequest)
                    .stream()
                    .sorted((o1, o2) -> o2.getPublishedOn().getNano() - o1.getPublishedOn().getNano())
                    .map(RequestMapper::toRequestDto)
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException(
                    "Сортировка возможна только по возрастанию или убыванию!");
        }
    }

    @Override
    public List<RequestAllDto> getAcceptRequest(Integer sort, int from, int size) {
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        if (sort.equals(0)) {
            //сортировка по убыванию даты
            return requestRepository.findRequestStatusAccepted(pageRequest)
                    .stream()
                    .sorted(Comparator.comparingInt(o -> o.getPublishedOn().getNano()))
                    .map(RequestMapper::toRequestAllDto)
                    .collect(Collectors.toList());
        } else if (sort.equals(1)) {
            //сортировка по возрастанию даты
            return requestRepository.findRequestStatusAccepted(pageRequest)
                    .stream()
                    .sorted((o1, o2) -> o2.getPublishedOn().getNano() - o1.getPublishedOn().getNano())
                    .map(RequestMapper::toRequestAllDto)
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException("Сортировка возможна только по возрастанию или убыванию!");
        }
    }

    @Override
    public List<RequestAllDto> getRejectRequest(Integer sort, int from, int size) {
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        if (sort.equals(0)) {
            //сортировка по убыванию даты
            return requestRepository.findRequestStatusRejected(pageRequest)
                    .stream()
                    .sorted(Comparator.comparingInt(o -> o.getPublishedOn().getNano()))
                    .map(RequestMapper::toRequestAllDto)
                    .collect(Collectors.toList());
        } else if (sort.equals(1)) {
            //сортировка по возрастанию даты
            return requestRepository.findRequestStatusRejected(pageRequest)
                    .stream()
                    .sorted((o1, o2) -> o2.getPublishedOn().getNano() - o1.getPublishedOn().getNano())
                    .map(RequestMapper::toRequestAllDto)
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException("Сортировка возможна только по возрастанию или убыванию!");
        }
    }

    @Override
    @Transactional
    public RequestAllDto acceptRequest(Long operatorId, Long requestId) {
        Request request = validationRequest(requestId);
        User user = validationUser(operatorId);
        user.getUserRole()
                .stream()
                .filter(role -> !role.getName().equals(String.valueOf(UserRole.OPERATOR)))
                .forEach(role -> {
                    throw new NotFoundException(
                            String.format("Пользователь %s (роль - %s) не может принимать заявку, " +
                                            "т.к. не является %s!",
                                    user.getName(),
                                    user.getUserRole()
                                            .stream()
                                            .map(Role::getName)
                                            .collect(Collectors.toSet()),
                                    UserRole.OPERATOR));
                });
        if (request.getStatus().equals(RequestStatus.SHIPPED)) {
            request.setStatus(RequestStatus.ACCEPTED);
            return toRequestAllDto(requestRepository.save(request));
        } else {
            throw new NotFoundException(
                    String.format("Заявка не имеет статус %s!", RequestStatus.SHIPPED));
        }
    }

    @Override
    @Transactional
    public RequestAllDto rejectRequest(Long operatorId, Long requestId) {
        Request request = validationRequest(requestId);
        User user = validationUser(operatorId);
        user.getUserRole()
                .stream()
                .filter(role -> !role.getName().equals(String.valueOf(UserRole.OPERATOR)))
                .forEach(role -> {
                    throw new NotFoundException(
                            String.format("Пользователь %s (роль - %s)не может отклонять заявку, " +
                                            "т.к. не является %s!",
                                    user.getName(),
                                    user.getUserRole()
                                            .stream()
                                            .map(Role::getName)
                                            .collect(Collectors.toSet()),
                                    UserRole.OPERATOR));
                });
        if (request.getStatus().equals(RequestStatus.SHIPPED) ||
                request.getStatus().equals(RequestStatus.ACCEPTED)) {
            request.setStatus(RequestStatus.REJECTED);
            return toRequestAllDto(requestRepository.save(request));
        } else {
            throw new NotFoundException(
                    String.format("Заявка не имеет статус %s или %s!",
                            RequestStatus.SHIPPED,
                            RequestStatus.ACCEPTED));
        }
    }

    //Методы исполнителя
    @Override
    public List<RequestAllDto> getDoneRequest(Integer sort, int from, int size) {
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        if (sort.equals(0)) {
            //сортировка по убыванию даты
            return requestRepository.findRequestStatusDone(pageRequest)
                    .stream()
                    .sorted(Comparator.comparingInt(o -> o.getPublishedOn().getNano()))
                    .map(RequestMapper::toRequestAllDto)
                    .collect(Collectors.toList());
        } else if (sort.equals(1)) {
            //сортировка по возрастанию даты
            return requestRepository.findRequestStatusDone(pageRequest)
                    .stream()
                    .sorted((o1, o2) -> o2.getPublishedOn().getNano() - o1.getPublishedOn().getNano())
                    .map(RequestMapper::toRequestAllDto)
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException("Сортировка возможна только по возрастанию или убыванию!");
        }
    }

    @Override
    @Transactional
    public RequestAllDto doneRequest(Long executorId, Long requestId) {
        Request request = validationRequest(requestId);
        User executor = validationUser(executorId);
        executor.getUserRole()
                .stream()
                .filter(role -> !role.getName().equals(String.valueOf(UserRole.EXECUTOR)))
                .forEach(role -> {
                    throw new NotFoundException(
                            String.format("Пользователь %s (роль - %s)не может отклонять заявку, " +
                                            "т.к. не является %s!",
                                    executor.getName(),
                                    executor.getUserRole()
                                            .stream()
                                            .map(Role::getName)
                                            .collect(Collectors.toSet()),
                                    UserRole.EXECUTOR));
                });
        if (request.getStatus().equals(RequestStatus.ACCEPTED)) {
            request.setStatus(RequestStatus.DONE);
            return toRequestAllDto(requestRepository.save(request));
        } else {
            throw new NotFoundException(
                    String.format("Заявка не имеет статус %s!",
                            RequestStatus.ACCEPTED));
        }
    }

    //Методы валидации переданного id
    private User validationUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь %s не существует!", userId)));
    }

    private Request validationRequest(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Запрос %s не существует!", requestId)));
    }
}
