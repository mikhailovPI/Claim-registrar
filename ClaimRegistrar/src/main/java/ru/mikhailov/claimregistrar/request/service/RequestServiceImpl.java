package ru.mikhailov.claimregistrar.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mikhailov.claimregistrar.config.PageRequestOverride;
import ru.mikhailov.claimregistrar.exception.NotFoundException;
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
    public List<RequestDto> getRequestsByUser(Long userId, Integer sort, int from, int size) {
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        if (sort.equals(0)) {
            //сортировка по убыванию даты
            return requestRepository.findRequestsByUserId(userId, pageRequest)
                    .stream()
                    .sorted(new Comparator<Request>() {
                        @Override
                        public int compare(Request o1, Request o2) {
                            return o1.getPublishedOn().getNano() - o2.getPublishedOn().getNano();
                        }
                    })
                    .map(RequestMapper::toRequestDto)
                    .collect(Collectors.toList());
        } else if (sort.equals(1)) {
            //сортировка по возрастанию даты
            return requestRepository.findRequestsByUserId(userId, pageRequest)
                    .stream()
                    .sorted(new Comparator<Request>() {
                        @Override
                        public int compare(Request o1, Request o2) {
                            return o2.getPublishedOn().getNano() - o1.getPublishedOn().getNano();
                        }
                    })
                    .map(RequestMapper::toRequestDto)
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException("Сортировка возможна только по возрастанию или убыванию");
        }
    }

    @Override
    @Transactional
    public RequestDto createRequest(RequestDto requestDto, Long userId) {
        User user = validationUser(userId);

        Request request = RequestMapper.toRequest(requestDto);
        request.setPublishedOn(LocalDateTime.now());
        request.setUser(user);
        request.setStatus(RequestStatus.ЧЕРНОВИК);
        Request requestSave = requestRepository.save(request);
        return RequestMapper.toRequestDto(requestSave);
    }

    @Override
    public RequestDto sendRequest(Long userId, Long requestId) {
        Request request = validationRequest(requestId);
        if (!request.getUser().getId().equals(userId)) {
            throw new NotFoundException("Пользователь не может отправить чужую заявку!");
        }
        request.setStatus(RequestStatus.ОТПРАВЛЕНО);
        Request requestSave = requestRepository.save(request);

        return RequestMapper.toRequestDto(requestSave);
    }

/*    @Override
    public RequestDto updateRequest(Long userId, Long requestId) {
        Request request = validationRequest(requestId);
        if (!request.getUser().equals(userId)) {
            throw new NotFoundException ("Пользователь не может редактировать чужую заявку!");
        }
        request.setText("");
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }*/

    @Override
    public RequestDto updateRequest(Long userId, RequestDto requestDto) {
        Request request = validationRequest(requestDto.getId());
        if (!request.getUser().getId().equals(userId)) {
            throw new NotFoundException("Пользователь не может редактировать чужую заявку!");
        }
        if (!request.getStatus().equals(RequestStatus.ЧЕРНОВИК)) {
            throw new NotFoundException("Статус заявки не позволяет ее редактировать. Должен быть статус - черновик!");
        }
        request.setText(requestDto.getText());
        Request requestSave = requestRepository.save(request);

        return RequestMapper.toRequestDto(requestSave);
    }

    //TODO Методы для оператора
    @Override
    public List<Request> getRequests(int from, int size) {
        return null;
    }

    @Override
    public List<Request> getUserRequest(Long userId) {
        return null;
    }

    @Override
    public Request acceptRequest(Long requestId) {
        return null;
    }

    @Override
    public Request rejectRequest(Long requestId) {
        return null;
    }

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
