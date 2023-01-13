package ru.skillbox.diplom.group32.social.service.model.friend;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skillbox.diplom.group32.social.service.model.account.StatusCode;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;

import java.time.ZonedDateTime;
@Data
@AllArgsConstructor
@Schema(description = "Дто для друзей")
public class FriendDto extends BaseDto {

    @Schema(description = "Фото")
    private String photo;

    @Schema(description = "Статус код, состояние дружбы - в друзьях, заявка, отклонено итд")
    private StatusCode statusCode;

    @Schema(description = "Имя")
    private String firstName;

    @Schema(description = "Фамилия")
    private String lastName;

    @Schema(description = "Город")
    private String city;

    @Schema(description = "Страна")
    private String country;

    @Schema(description = "Дата рождения")
    private ZonedDateTime birthDate;

    @Schema(description = "Онлайн?")
    private Boolean isOnline;

    @Schema(name = "Аккаунт, от которого идет запрос")
    private Long fromAccountId;

    @Schema(name = "Аккаунт, к которому идет запрос")
    private Long toAccountId;

}