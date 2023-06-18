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
@RequestMapping(path = RequestExecutorController.URL_EXECUTOR)
@Slf4j
public class RequestExecutorController {

    public final static String URL_EXECUTOR = "/request/executor";
    private final RequestService requestService;

    //Получение всех выполненных заявок с возможностью сортировки по дате и пагинацией
    @GetMapping(path = "/done/{sort}")
    public List<RequestAllDto> getDoneRequest(
            @PathVariable Integer sort,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        log.info("URL: /request/executor/done/{sort}. " +
                "GetMapping/Получение всех выполненных заявок/getRejectRequest");
        return requestService.getDoneRequest(sort, from, size);
    }


    //Назначение статуса заявки выполнено
    @PatchMapping(path = "/{userId}/request/{requestId}")
    public RequestAllDto doneRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId) {
        log.info("URL: /request/executor/{userId}/request/{requestId}. " +
                "PatchMapping/Назначение статуса заявки выполнено/doneRequest");
        return requestService.doneRequest(userId, requestId);
    }
}
