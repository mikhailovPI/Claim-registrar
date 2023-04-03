package ru.mikhailov.claimregistrar.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.mikhailov.claimregistrar.request.dto.RequestDto;
import ru.mikhailov.claimregistrar.request.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/request")
@Slf4j
public class RequestController {

    private final RequestService requestService;

    @GetMapping
    public List<RequestDto> getRequests(
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        log.info("URL: /request. GetMapping/Получение всех заявок/getRequest");
        return requestService.getRequests(from, size);
    }

    @PostMapping
    public RequestDto createRequest(@RequestBody RequestDto requestDto) {
        log.info("URL: /request. PostMapping/Создание заявки/createRequest");
        return requestService.createRequest(requestDto);
    }
}
