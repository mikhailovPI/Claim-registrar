package ru.mikhailov.claimregistrar.request.service;

import org.springframework.stereotype.Service;
import ru.mikhailov.claimregistrar.request.model.Request;

import java.util.List;

@Service
public interface RequestService {

    List<Request> getRequestsByUser(Long userId);

    Request createRequest(Request request);

    Request sendRequest(Long userId, Long requestId);

    Request updateRequest(Long userId, Long requestId);

    List<Request> getRequests(int from, int size);

    List<Request> getUserRequest(Long userId);

    Request acceptRequest(Long requestId);

    Request rejectRequest(Long requestId);
}
