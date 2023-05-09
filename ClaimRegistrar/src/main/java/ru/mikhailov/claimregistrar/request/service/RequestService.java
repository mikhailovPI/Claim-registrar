package ru.mikhailov.claimregistrar.request.service;

import com.sun.xml.bind.v2.TODO;
import org.springframework.stereotype.Service;
import ru.mikhailov.claimregistrar.request.dto.RequestAllDto;
import ru.mikhailov.claimregistrar.request.dto.RequestDto;
import ru.mikhailov.claimregistrar.request.model.Request;

import java.util.List;

@Service
public interface RequestService {

    //TODO Методы для пользователя
    List<RequestAllDto> getRequestsByUser(Long userId, Integer sort, int from, int size);

    RequestDto createRequest(RequestDto request, Long userId);

    RequestDto sendRequest(Long userId, Long requestId);

    //RequestDto updateRequest(Long userId, Long requestId);
    RequestDto updateRequest(Long userId, RequestDto requestDto);

    //TODO Методы для оператора
    List<RequestAllDto> getRequests(Integer sort, int from, int size);

    List<RequestDto> getUserRequest(Long userId, Integer sort, int from, int size);

    RequestAllDto acceptRequest(Long requestId);

    RequestAllDto rejectRequest(Long requestId);
}
