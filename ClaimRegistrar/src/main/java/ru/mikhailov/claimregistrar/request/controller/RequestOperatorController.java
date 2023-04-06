package ru.mikhailov.claimregistrar.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.mikhailov.claimregistrar.request.model.Request;
import ru.mikhailov.claimregistrar.request.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/request/operator")
@Slf4j
public class RequestOperatorController {

    private final RequestService requestService;

    //Получение всех заявок с возможностью сортировки по дате и пагинацией
    @GetMapping
    public List<Request> getRequests(
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        log.info("URL: /request/operator. GetMapping/Получение всех заявок/getRequest");
        return requestService.getRequests(from, size);
    }

    //Получение всех заявок пользователя с возможностью сортировки по дате и пагинацией
    @GetMapping(path = "users/{userId}")
    public  List<Request> getUserRequest (@PathVariable Long userId) {
        log.info("URL: /request/operator/user/{userId}. GetMapping/Получение всех заявок пользователя/getUserRequest");
        return requestService.getUserRequest(userId);
    }

    //Принятие заявки
    @PatchMapping(path = "/accept/{requestId}")
    public Request acceptRequest (@PathVariable Long requestId) {
        log.info("URL: /request/operator/{requestId}. PatchMapping/Принятие заявки/acceptRequest");
        return requestService.acceptRequest(requestId);
    }

    //Отклонение заявки
    @PatchMapping(path = "/reject/{requestId}")
    public Request rejectRequest (@PathVariable Long requestId) {
        log.info("URL: /request/operator/{requestId}. PatchMapping/Отклонение заявки/rejectRequest");
        return requestService.rejectRequest(requestId);
    }
}
