package ru.mikhailov.claimregistrar.request.service;

import org.springframework.stereotype.Service;
import ru.mikhailov.claimregistrar.request.dto.RequestAllDto;
import ru.mikhailov.claimregistrar.request.dto.RequestDto;
import ru.mikhailov.claimregistrar.request.dto.RequestNewDto;
import ru.mikhailov.claimregistrar.request.dto.RequestUpdateDto;

import java.util.List;

@Service
public interface RequestService {

    //TODO Методы для пользователя
    List<RequestDto> getRequestsByUser(Long userId, Integer sort, int from, int size);

    RequestAllDto createRequest(RequestNewDto request, Long userId);

    RequestDto sendRequest(Long userId, Long requestId);

    RequestDto updateRequest(Long userId, Long requestId, RequestUpdateDto requestUprateDto);

    //TODO Методы для оператора
    List<RequestAllDto> getRequests(Integer sort, int from, int size);

    List<RequestDto> getUserRequest(String namePart, Integer sort, int from, int size);

    RequestAllDto acceptRequest(Long operatorId, Long requestId);

    RequestAllDto rejectRequest(Long operatorId, Long requestId);
}
