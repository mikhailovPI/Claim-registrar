package ru.mikhailov.claimregistrar.exception;

public class ConflictingRequestException extends RuntimeException {
    public ConflictingRequestException(String message) {
        super(message);
    }
}
