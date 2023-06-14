package ru.mikhailov.claimregistrar.config;

import ru.mikhailov.claimregistrar.exception.ValidationException;
import ru.mikhailov.claimregistrar.user.model.User;

public class Validation {

    public static void validationBodyUser(User user) {
        if (user.getEmail() == null) {
            throw new ValidationException("E-mail не должен быть пустым.");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Введен некорректный e-mail.");
        }
        if (user.getName() == null) {
            throw new ValidationException("Name не должен быть пустым.");
        }
    }
}
