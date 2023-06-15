package ru.mikhailov.claimregistrar.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.mikhailov.claimregistrar.request.dto.RequestAllDto;
import ru.mikhailov.claimregistrar.request.dto.RequestDto;
import ru.mikhailov.claimregistrar.request.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = RequestOperatorController.URL_OPERATOR)
@Slf4j
public class RequestOperatorController {

    public final static String URL_OPERATOR = "/request/operator";
    private final RequestService requestService;

    //Получение всех заявок с возможностью сортировки по дате и пагинацией
    @GetMapping(path = "/{sort}")
    public List<RequestAllDto> getRequests(
            @PathVariable Integer sort,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        log.info("URL: /request/operator. GetMapping/Получение всех заявок/getRequest");
        return requestService.getRequests(sort, from, size);
    }

    //Получение всех заявок пользователя по его имени с возможностью сортировки по дате и пагинацией
    @GetMapping(path = "/users/{sort}")
    public List<RequestDto> getUserRequest(
            @RequestParam String namePart,
            @PathVariable Integer sort,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        log.info("URL: /request/operator/user/{userId}. " +
                "GetMapping/Получение всех заявок пользователя/getUserRequest");
        return requestService.getUserRequest(namePart, sort, from, size);
    }

    //Принятие заявки
    @PatchMapping(path = "/{operatorId}/accept/{requestId}")
    public RequestAllDto acceptRequest(
            @PathVariable Long operatorId,
            @PathVariable Long requestId) {
        log.info("URL: /request/operator//{operatorId}/accept/{requestId}. " +
                "PatchMapping/Принятие заявки/acceptRequest");
        return requestService.acceptRequest(operatorId, requestId);
    }

    //Отклонение заявки
    @PatchMapping(path = "/{operatorId}/reject/{requestId}")
    public RequestAllDto rejectRequest(
            @PathVariable Long operatorId,
            @PathVariable Long requestId) {
        log.info("URL: /request/operator/{operatorId}/reject/{requestId}. " +
                "PatchMapping/Отклонение заявки/rejectRequest");
        return requestService.rejectRequest(operatorId, requestId);
    }
}
