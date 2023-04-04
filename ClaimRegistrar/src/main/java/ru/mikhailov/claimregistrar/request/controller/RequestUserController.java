package ru.mikhailov.claimregistrar.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.mikhailov.claimregistrar.request.dto.RequestDto;
import ru.mikhailov.claimregistrar.request.model.Request;
import ru.mikhailov.claimregistrar.request.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/request/user")
@Slf4j
public class RequestUserController {

    private final RequestService requestService;

    //Просмотр заявок пользователя с возможностью сортировки по дате и пагинацией
    @GetMapping(path = "/{userId}")
    public List<RequestDto> getRequestsByUser (@PathVariable Long userId) {
        log.info("URL: /request/user/{userId}. GetMapping/Просмотр всех заявок пользователя/createRequest");
        return requestService.getRequestsByUser(userId);
    }

    //Создание заявки
    @PostMapping(path = "/{userId}")
    public RequestDto createRequest(
            @RequestBody RequestDto request,
            @PathVariable Long userId) {
        log.info("URL: /request. PostMapping/Создание заявки/createRequest");
        return requestService.createRequest(request, userId);
    }

    //Отправка заявки на рассмотрение
    @PatchMapping(path = "/{userId}/request/{requestId}")
    public RequestDto sendRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId) {
        log.info("URL: /request/{userId}/request/{requestId}. PatchMapping/Отправка заявки/sendRequest");
        return requestService.sendRequest(userId, requestId);
    }

    //Редактирование заявки
/*    @PatchMapping(path = "/{userId}/request/{requestId}")
    public RequestDto updateRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId) {
        log.info("URL: /request/{userId}/request/{requestId}. PatchMapping/Редактирование заявки/updateRequest");
        return requestService.updateRequest(userId, requestId);
    }*/

    @PatchMapping(path = "/{userId}/request")
    public RequestDto updateRequest(
            @PathVariable Long userId,
            @RequestBody RequestDto requestDto) {
        log.info("URL: /request/{userId}/request. PatchMapping/Редактирование заявки/updateRequest");
        return requestService.updateRequest(userId, requestDto);
    }
}
