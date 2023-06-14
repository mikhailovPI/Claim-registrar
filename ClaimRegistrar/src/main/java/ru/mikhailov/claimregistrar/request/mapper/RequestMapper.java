package ru.mikhailov.claimregistrar.request.mapper;

import ru.mikhailov.claimregistrar.request.dto.RequestAllDto;
import ru.mikhailov.claimregistrar.request.dto.RequestDto;
import ru.mikhailov.claimregistrar.request.dto.RequestNewDto;
import ru.mikhailov.claimregistrar.request.model.Request;
import ru.mikhailov.claimregistrar.request.model.RequestStatus;
import ru.mikhailov.claimregistrar.user.dto.UserRequestDro;

public class RequestMapper {

    public static Request toRequest(RequestNewDto requestDto) {
        return new Request(
                null,
                requestDto.getText(),
                null,
                null,
                RequestStatus.DRAFT
        );
    }

    public static RequestDto toRequestDto(Request request) {
        return new RequestDto(
                request.getId(),
                request.getText(),
                request.getStatus()
        );
    }

    public static RequestAllDto toRequestAllDto(Request request) {
        return new RequestAllDto(
                request.getId(),
                request.getText(),
                request.getPublishedOn(),
                new UserRequestDro(
                        request.getUser().getId(),
                        request.getUser().getName(),
                        request.getUser().getEmail()),
                request.getStatus()
        );
    }
}
