package ru.mikhailov.claimregistrar.request.mapper;

import ru.mikhailov.claimregistrar.request.dto.RequestDto;
import ru.mikhailov.claimregistrar.request.model.Request;

public class RequestMapper {

    public static Request toRequest (RequestDto requestDto) {
        return new Request(
                requestDto.getId(),
                requestDto.getText(),
                requestDto.getPublishedOn(),
                null,
                requestDto.getStatus()
        );
    }

    public static RequestDto toRequestDto (Request request) {
        return new RequestDto(
                request.getId(),
                request.getText(),
                request.getPublishedOn(),
                request.getUser().getId(),
                request.getStatus()
        );
    }
}
