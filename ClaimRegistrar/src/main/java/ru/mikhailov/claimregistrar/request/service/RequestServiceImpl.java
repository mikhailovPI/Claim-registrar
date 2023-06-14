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
import ru.mikhailov.claimregistrar.user.model.User;
import ru.mikhailov.claimregistrar.user.model.UserRole;
import ru.mikhailov.claimregistrar.user.repository.RoleRepository;
import ru.mikhailov.claimregistrar.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    //TODO Методы для пользователя
    @Override
    public List<RequestDto> getRequestsByUser(Long userId, Integer sort, int from, int size) {
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        User user = validationUser(userId);
        if (!user.getId().equals(userId)) {
            throw new NotFoundException(
                    String.format("Пользователь %s не может смотреть чужие запросы", user.getName()));
        }
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
                            String.format("Пользователь %s не может создавать заявку, " +
                                            "т.к. не является %d.",
                                    user.getName(),
                                    String.valueOf(UserRole.USER)));
                });
        Request request = RequestMapper.toRequest(requestDto);
        request.setPublishedOn(LocalDateTime.now());
        request.setUser(user);
        Request requestSave = requestRepository.save(request);
        return RequestMapper.toRequestAllDto(requestSave);
    }

    @Override
    @Transactional
    public RequestDto sendRequest(Long userId, Long requestId) {
        Request request = validationRequest(requestId);
        User user = validationUser(userId);
        if (!request.getUser().getId().equals(userId)) {
            throw new NotFoundException("Пользователь не может отправить чужую заявку!");
        }
        user.getUserRole()
                .stream()
                .filter(role -> !role.getName().equals(String.valueOf(UserRole.USER)))
                .forEach(role -> {
                    throw new NotFoundException(
                            String.format("Пользователь %s не может отправить заявку," +
                                            " т.к. не является %d.",
                                    user.getName(),
                                    String.valueOf(UserRole.USER)));
                });
        if (request.getStatus().equals(RequestStatus.DRAFT)) {
            request.setStatus(RequestStatus.SHIPPED);
            Request requestUpdate = requestRepository.save(request);
            return RequestMapper.toRequestDto(requestUpdate);
        } else {
            throw new NotFoundException(
                    String.format("Заявка имеет статус %s, а должна иметь статус '" +
                            RequestStatus.DRAFT + "'!", request.getStatus()));
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
                            String.format("Пользователь %s не может редактировать заявку, " +
                                            "т.к. не является %d.",
                                    user.getName(),
                                    String.valueOf(UserRole.USER)));
                });
        if (!request.getUser().getId().equals(userId)) {
            throw new NotFoundException("Пользователь не может редактировать чужую заявку!");
        }
        if (!request.getStatus().equals(RequestStatus.DRAFT)) {
            throw new NotFoundException("Статус заявки не позволяет ее редактировать. Должен быть статус - '"
                    + RequestStatus.DRAFT + "'!");
        }
        request.setText(requestUprateDto.getText());
        Request requestSave = requestRepository.save(request);
        return RequestMapper.toRequestDto(requestSave);
    }

    //TODO Методы для оператора
    @Override
    public List<RequestAllDto> getRequests(Integer sort, int from, int size) {
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        if (sort.equals(0)) {
            //сортировка по убыванию даты
            return requestRepository.findAll(pageRequest)
                    .stream()
                    .filter(request -> request.getStatus().equals(RequestStatus.SHIPPED))
                    .sorted(Comparator.comparingInt(o -> o.getPublishedOn().getNano()))
                    .map(RequestMapper::toRequestAllDto)
                    .collect(Collectors.toList());
        } else if (sort.equals(1)) {
            //сортировка по возрастанию даты
            return requestRepository.findAll(pageRequest)
                    .stream()
                    .filter(request -> request.getStatus().equals(RequestStatus.SHIPPED))
                    .sorted((o1, o2) -> o2.getPublishedOn().getNano() - o1.getPublishedOn().getNano())
                    .map(RequestMapper::toRequestAllDto)
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException("Сортировка возможна только по возрастанию или убыванию");
        }
    }

    //TODO добавить просмотр заявок по имени
    @Override
    public List<RequestDto> getUserRequest(Long userId, Integer sort, int from, int size) {
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        if (sort.equals(0)) {
            //сортировка по убыванию даты
            return requestRepository.findAll(pageRequest)
                    .stream()
                    .filter(request -> request.getStatus().equals(RequestStatus.SHIPPED))
                    .filter(request -> request.getUser().getId().equals(userId))
                    .sorted(Comparator.comparingInt(o -> o.getPublishedOn().getNano()))
                    .map(RequestMapper::toRequestDto)
                    .collect(Collectors.toList());
        } else if (sort.equals(1)) {
            //сортировка по возрастанию даты
            return requestRepository.findAll(pageRequest)
                    .stream()
                    .filter(request -> request.getStatus().equals(RequestStatus.SHIPPED))
                    .filter(request -> request.getUser().getId().equals(userId))
                    .sorted((o1, o2) -> o2.getPublishedOn().getNano() - o1.getPublishedOn().getNano())
                    .map(RequestMapper::toRequestDto)
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException("Сортировка возможна только по возрастанию или убыванию");
        }
    }


    @Override
    @Transactional
    public RequestAllDto acceptRequest(Long requestId) {
        Request request = validationRequest(requestId);
        User user = validationUser(request.getUser().getId());
        user.getUserRole()
                .stream()
                .filter(role -> !role.getName().equals(String.valueOf(UserRole.OPERATOR)))
                .forEach(role -> {
                    throw new NotFoundException(
                            String.format("Пользователь %s не может принимать заявку, " +
                                            "т.к. не является %d.",
                                    user.getName(),
                                    String.valueOf(UserRole.OPERATOR)));
                });
        if (request.getStatus().equals(RequestStatus.SHIPPED)) {
            request.setStatus(RequestStatus.ACCEPTED);
            Request requestUpdate = requestRepository.save(request);
            return RequestMapper.toRequestAllDto(requestUpdate);
        } else {
            throw new NotFoundException("Заявка не имеет статус " + RequestStatus.SHIPPED);
        }
    }

    @Override
    @Transactional
    public RequestAllDto rejectRequest(Long requestId) {
        Request request = validationRequest(requestId);
        User user = validationUser(request.getUser().getId());
        user.getUserRole()
                .stream()
                .filter(role -> !role.getName().equals(String.valueOf(UserRole.OPERATOR)))
                .forEach(role -> {
                    throw new NotFoundException(
                            String.format("Пользователь %s не может отклонять заявку, " +
                                            "т.к. не является %d.",
                                    user.getName(),
                                    String.valueOf(UserRole.OPERATOR)));
                });
        if (request.getStatus().equals(RequestStatus.SHIPPED) ||
                request.getStatus().equals(RequestStatus.ACCEPTED)) {
            request.setStatus(RequestStatus.REJECTED);
            Request requestUpdate = requestRepository.save(request);
            return RequestMapper.toRequestAllDto(requestUpdate);
        } else {
            throw new NotFoundException("Заявка не имеет статус " + RequestStatus.SHIPPED + " или "
                    + RequestStatus.ACCEPTED);
        }
    }

    //TODO методы валидации
    private User validationUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь %s не существует.", userId)));
    }

    private Request validationRequest(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Запрос %s не существует.", requestId)));
    }
}
