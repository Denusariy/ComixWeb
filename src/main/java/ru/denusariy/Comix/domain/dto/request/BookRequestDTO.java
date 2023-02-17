package ru.denusariy.Comix.domain.dto.request;

import ru.denusariy.Comix.domain.enums.Format;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public boolean isAltCover() {
        return isAltCover;
    }

    public void setAltCover(boolean altCover) {
        isAltCover = altCover;
    }

    public boolean isAutograph() {
        return isAutograph;
    }

    public void setAutograph(boolean autograph) {
        isAutograph = autograph;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
