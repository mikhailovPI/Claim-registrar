package ru.mikhailov.claimregistrar.request.mapper;

import ru.mikhailov.claimregistrar.request.dto.RequestAllDto;
import ru.mikhailov.claimregistrar.request.dto.RequestDto;
import ru.mikhailov.claimregistrar.request.model.Request;

import java.time.LocalDateTime;

public class RequestMapper {

    public static Request toRequest (RequestDto requestDto) {
        return new Request(
                requestDto.getId(),
                requestDto.getText(),
                //requestDto.getPublishedOn(),
                LocalDateTime.now(),
                null,
                requestDto.getStatus()
        );
    }

    public static RequestDto toRequestDto (Request request) {
        return new RequestDto(
                request.getId(),
                request.getText(),
                //request.getPublishedOn(),
                //request.getUser().getId(),
                request.getStatus()
        );
    }

    public static RequestAllDto toRequestAllDto (Request request) {
        return new RequestAllDto(
                request.getId(),
                request.getText(),
                request.getPublishedOn(),
                request.getUser(),
                request.getStatus()
        );
    }

    public static Request toRequestAll (RequestAllDto requestAllDto) {
        return new Request(
                requestAllDto.getId(),
                requestAllDto.getText(),
                requestAllDto.getPublishedOn(),
                requestAllDto.getUser(),
                requestAllDto.getStatus()
        );
    }
}
