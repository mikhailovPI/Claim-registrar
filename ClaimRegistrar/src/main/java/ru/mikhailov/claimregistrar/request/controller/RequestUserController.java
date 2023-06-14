package ru.mikhailov.claimregistrar.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.mikhailov.claimregistrar.request.dto.RequestAllDto;
import ru.mikhailov.claimregistrar.request.dto.RequestDto;
import ru.mikhailov.claimregistrar.request.dto.RequestNewDto;
import ru.mikhailov.claimregistrar.request.dto.RequestUpdateDto;
import ru.mikhailov.claimregistrar.request.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = RequestUserController.URL_USER)
@Slf4j
public class RequestUserController {

    public final static String URL_USER = "/request/users";
    private final RequestService requestService;

    //Просмотр заявок пользователя с возможностью сортировки по дате и пагинацией
    @GetMapping(path = "/{userId}/{sort}")
    public List<RequestDto> getRequestsByUser(
            @PathVariable Long userId,
            @PathVariable Integer sort,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        log.info("URL: /request/users/{userId}. GetMapping/Просмотр всех заявок пользователя/createRequest");
        return requestService.getRequestsByUser(userId, sort, from, size);
    }

    //Создание заявки
    @PostMapping(path = "/{userId}")
    public RequestAllDto createRequest(
            @RequestBody RequestNewDto request,
            @PathVariable Long userId) {
        log.info("URL: /request/users/{userId}. PostMapping/Создание заявки/createRequest");
        return requestService.createRequest(request, userId);
    }

    //Отправка заявки на рассмотрение
    @PatchMapping(path = "/{userId}/request/{requestId}")
    public RequestDto sendRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId) {
        log.info("URL: /request/users/{userId}/request/{requestId}. PatchMapping/Отправка заявки/sendRequest");
        return requestService.sendRequest(userId, requestId);
    }

    //Редактирование заявки
    @PatchMapping(path = "update/{userId}/request/{requestId}")
    public RequestDto updateRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId,
            @RequestBody RequestUpdateDto requestUpdateDto) {
        log.info("URL: /request/users/{userId}/request/{requestId}. PatchMapping/Редактирование заявки/updateRequest");
        return requestService.updateRequest(userId, requestId, requestUpdateDto);
    }
}
