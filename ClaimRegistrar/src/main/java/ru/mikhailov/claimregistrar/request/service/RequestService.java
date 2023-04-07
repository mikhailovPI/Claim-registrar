package ru.mikhailov.claimregistrar.request.service;

import org.springframework.stereotype.Service;
import ru.mikhailov.claimregistrar.request.dto.RequestAllDto;
import ru.mikhailov.claimregistrar.request.dto.RequestDto;
import ru.mikhailov.claimregistrar.request.model.Request;

import java.util.List;

@Service
public interface RequestService {

    List<RequestAllDto> getRequestsByUser(Long userId, Integer sort, int from, int size);

    RequestDto createRequest(RequestDto request, Long userId);

    RequestDto sendRequest(Long userId, Long requestId);

    //RequestDto updateRequest(Long userId, Long requestId);
    RequestDto updateRequest(Long userId, RequestDto requestDto);

    List<Request> getRequests(int from, int size);

    List<Request> getUserRequest(Long userId);

    Request acceptRequest(Long requestId);

    Request rejectRequest(Long requestId);
}
