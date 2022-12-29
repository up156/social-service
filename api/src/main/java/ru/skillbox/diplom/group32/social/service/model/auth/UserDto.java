package ru.skillbox.diplom.group32.social.service.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Дто для пользователя")
public class UserDto extends BaseDto { //TODO Можно в принципе сделать RegistrationDto и не привязыватьс к userDto и как в свагере ее сделать

    @Schema(description = "Фамилия")
    private String firstName;
    @Schema(description = "Имя")
    private String lastName;
    @Schema(description = "Электронная почта пользователя")
    private String email;
    @Schema(description = "Пароль пользователя")
    private String password;
}
