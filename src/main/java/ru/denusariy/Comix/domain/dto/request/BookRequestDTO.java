package ru.denusariy.Comix.domain.dto.request;

import lombok.Getter;
import lombok.Setter;
import ru.denusariy.Comix.domain.enums.Format;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
@Getter
@Setter
public class BookRequestDTO {
    @NotBlank(message = "Название книги не должно быть пустым")
    private String title;
    @Min(value = 1900, message = "Год издания должен быть больше 1900")
    private int year;
    @Enumerated(EnumType.STRING)
    private Format format;
    private boolean isAltCover;
    private boolean isAutograph;
    private String signature;
}
