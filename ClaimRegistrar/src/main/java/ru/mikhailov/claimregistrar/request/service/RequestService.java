package ru.mikhailov.claimregistrar.request.service;

import org.springframework.stereotype.Service;
import ru.mikhailov.claimregistrar.request.dto.RequestDto;

import java.util.List;

@Service
public interface RequestService {
    List<RequestDto> getRequests(int from, int size);

    RequestDto createRequest(RequestDto requestDto);
}
