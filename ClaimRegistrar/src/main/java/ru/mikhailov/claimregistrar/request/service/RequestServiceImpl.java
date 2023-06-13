package ru.mikhailov.claimregistrar.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mikhailov.claimregistrar.config.PageRequestOverride;
import ru.mikhailov.claimregistrar.exception.NotFoundException;
import ru.mikhailov.claimregistrar.request.dto.RequestAllDto;
import ru.mikhailov.claimregistrar.request.dto.RequestDto;
import ru.mikhailov.claimregistrar.request.mapper.RequestMapper;
import ru.mikhailov.claimregistrar.request.model.Request;
import ru.mikhailov.claimregistrar.request.model.RequestStatus;
import ru.mikhailov.claimregistrar.request.repository.RequestRepository;
import ru.mikhailov.claimregistrar.user.model.User;
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
    public List<RequestAllDto> getRequestsByUser(Long userId, Integer sort, int from, int size) {
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        if (!validationUser(userId).getId().equals(userId)) {
            throw new NotFoundException(
                    String.format("Данный пользователь %s не может смотреть чужие запросы", userId));
        }
        if (sort.equals(0)) {
            //сортировка по убыванию даты
            return requestRepository.findRequestsByUserId(userId, pageRequest)
                    .stream()
                    .sorted(Comparator.comparingInt(o -> o.getPublishedOn().getNano()))
                    .map(RequestMapper::toRequestAllDto)
                    .collect(Collectors.toList());
        } else if (sort.equals(1)) {
            //сортировка по возрастанию даты
            return requestRepository.findRequestsByUserId(userId, pageRequest)
                    .stream()
                    .sorted((o1, o2) -> o2.getPublishedOn().getNano() - o1.getPublishedOn().getNano())
                    .map(RequestMapper::toRequestAllDto)
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException("Сортировка возможна только по возрастанию или убыванию");
        }
    }

    @Override
    @Transactional
    public RequestDto createRequest(RequestDto requestDto, Long userId) {
        User user = validationUser(userId);
//        if (user.getAdmin() == true ||
//                user.getOperator() == true) {
//            throw new NotFoundException(
//                    String.format("Данный пользователь %s не может создавать запрос," +
//                            " т.к. является оператором/админом.", userId));
//        }
        Request request = RequestMapper.toRequest(requestDto);
        request.setPublishedOn(LocalDateTime.now());
        request.setUser(user);
        request.setStatus(RequestStatus.ЧЕРНОВИК);
        Request requestSave = requestRepository.save(request);
        return RequestMapper.toRequestDto(requestSave);
    }

    @Override
    @Transactional
    public RequestDto sendRequest(Long userId, Long requestId) {
        Request request = validationRequest(requestId);
        if (!request.getUser().getId().equals(userId)) {
            throw new NotFoundException("Пользователь не может отправить чужую заявку!");
        }
//        if (validationUser(userId).getAdmin() == true ||
//                validationUser(userId).getOperator() == true) {
//            throw new NotFoundException(
//                    String.format("Данный пользователь %s не может изменять статус запроса на " +
//                            RequestStatus.ОТПРАВЛЕНО + " т.к. является оператором/админом.", userId));
//        }
        if (request.getStatus().equals(RequestStatus.ЧЕРНОВИК)) {
            request.setStatus(RequestStatus.ОТПРАВЛЕНО);
            Request requestUpdate = requestRepository.save(request);
            return RequestMapper.toRequestDto(requestUpdate);
        } else {
            throw new NotFoundException("Заявка не имеет статус " + RequestStatus.ЧЕРНОВИК);
        }
    }

    @Override
    public RequestDto updateRequest(Long userId, RequestDto requestDto) {
        Request request = validationRequest(requestDto.getId());
        if (!request.getUser().getId().equals(userId)) {
            throw new NotFoundException("Пользователь не может редактировать чужую заявку!");
        }
        if (!request.getStatus().equals(RequestStatus.ЧЕРНОВИК)) {
            throw new NotFoundException("Статус заявки не позволяет ее редактировать. Должен быть статус - черновик!");
        }
//        if (validationUser(userId).getAdmin() == true ||
//                validationUser(userId).getOperator() == true) {
//            throw new NotFoundException(
//                    String.format("Данный пользователь %s не может обновлять запрос," +
//                            " т.к. является оператором/админом.", userId));
//        }

        request.setText(requestDto.getText());
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
                    .filter(request -> request.getStatus().equals(RequestStatus.ОТПРАВЛЕНО))
                    .sorted(Comparator.comparingInt(o -> o.getPublishedOn().getNano()))
                    .map(RequestMapper::toRequestAllDto)
                    .collect(Collectors.toList());
        } else if (sort.equals(1)) {
            //сортировка по возрастанию даты
            return requestRepository.findAll(pageRequest)
                    .stream()
                    .filter(request -> request.getStatus().equals(RequestStatus.ОТПРАВЛЕНО))
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
                    .filter(request -> request.getStatus().equals(RequestStatus.ОТПРАВЛЕНО))
                    .filter(request -> request.getUser().getId().equals(userId))
                    .sorted(Comparator.comparingInt(o -> o.getPublishedOn().getNano()))
                    .map(RequestMapper::toRequestDto)
                    .collect(Collectors.toList());
        } else if (sort.equals(1)) {
            //сортировка по возрастанию даты
            return requestRepository.findAll(pageRequest)
                    .stream()
                    .filter(request -> request.getStatus().equals(RequestStatus.ОТПРАВЛЕНО))
                    .filter(request -> request.getUser().getId().equals(userId))
                    .sorted((o1, o2) -> o2.getPublishedOn().getNano() - o1.getPublishedOn().getNano())
                    .map(RequestMapper::toRequestDto)
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException("Сортировка возможна только по возрастанию или убыванию");
        }
    }


    //TODO добавить ограничение на принятие заявки пользователем
    @Override
    @Transactional
    public RequestAllDto acceptRequest(Long requestId) {
        Request request = validationRequest(requestId);

        if (request.getStatus().equals(RequestStatus.ОТПРАВЛЕНО)) {
            request.setStatus(RequestStatus.ПРИНЯТО);
            Request requestUpdate = requestRepository.save(request);
            return RequestMapper.toRequestAllDto(requestUpdate);
        } else {
            throw new NotFoundException("Заявка не имеет статус " + RequestStatus.ОТПРАВЛЕНО);
        }
    }

    //TODO добавить ограничение на отклонение заявки пользователем
    @Override
    @Transactional
    public RequestAllDto rejectRequest(Long requestId) {
        Request request = validationRequest(requestId);

        if (request.getStatus().equals(RequestStatus.ОТПРАВЛЕНО) ||
                request.getStatus().equals(RequestStatus.ПРИНЯТО)) {
            request.setStatus(RequestStatus.ОТКЛОНЕНО);
            Request requestUpdate = requestRepository.save(request);
            return RequestMapper.toRequestAllDto(requestUpdate);
        } else {
            throw new NotFoundException("Заявка не имеет статус " + RequestStatus.ОТПРАВЛЕНО + " или "
                    + RequestStatus.ПРИНЯТО);
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
