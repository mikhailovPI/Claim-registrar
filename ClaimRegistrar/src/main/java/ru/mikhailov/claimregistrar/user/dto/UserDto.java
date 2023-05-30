package ru.mikhailov.claimregistrar.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.mikhailov.claimregistrar.user.model.UserRole;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    Long id;

    String name;

    String password;

    String email;

    UserRole userRole;
}
