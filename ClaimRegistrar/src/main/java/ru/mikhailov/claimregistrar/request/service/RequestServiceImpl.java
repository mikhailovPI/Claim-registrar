package ru.mikhailov.claimregistrar.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mikhailov.claimregistrar.request.dto.RequestDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    @Override
    public List<RequestDto> getRequests(int from, int size) {
        return null;
    }

    @Override
    public RequestDto createRequest(RequestDto requestDto) {
        return null;
    }
}
