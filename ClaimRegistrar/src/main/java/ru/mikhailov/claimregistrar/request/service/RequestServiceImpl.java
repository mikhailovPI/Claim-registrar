package ru.mikhailov.claimregistrar.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mikhailov.claimregistrar.request.dto.RequestDto;
import ru.mikhailov.claimregistrar.request.repository.RequestRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    @Override
    public List<RequestDto> getRequests(int from, int size) {
        return null;
    }

    @Override
    public RequestDto createRequest(RequestDto requestDto) {
        return null;
    }
}
