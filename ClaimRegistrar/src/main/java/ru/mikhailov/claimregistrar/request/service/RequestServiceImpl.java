package ru.mikhailov.claimregistrar.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mikhailov.claimregistrar.request.model.Request;
import ru.mikhailov.claimregistrar.request.repository.RequestRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    //TODO Методы для пользователя
    @Override
    public List<Request> getRequestsByUser(Long userId) {
        return null;
    }

    @Override
    public Request createRequest(Request request) {
        return null;
    }

    @Override
    public Request sendRequest(Long userId, Long requestId) {
        return null;
    }

    @Override
    public Request updateRequest(Long userId, Long requestId) {
        return null;
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
}
