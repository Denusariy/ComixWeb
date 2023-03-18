package ru.denusariy.Comix.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
@Getter
@Setter
public class ComicRequestDTO {
    @NotBlank(message = "Название комикса не должно быть пустым")
    private String title;
    @Min(value = 1900, message = "Год выпуска должен быть больше 1900")
    private int year;
    @NotBlank(message = "Необходимо указать сценаристов")
    private String writers;
    @NotBlank(message = "Необходимо указать художников")
    private String artists;

}
