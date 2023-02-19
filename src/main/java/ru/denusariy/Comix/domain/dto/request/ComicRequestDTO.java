package ru.denusariy.Comix.domain.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class ComicRequestDTO {
    @NotBlank(message = "Название комикса не должно быть пустым")
    private String title;

    @Min(value = 1900, message = "Год выпуска должен быть больше 1900")
    private int year;
    @NotBlank(message = "Необходимо указать сценаристов")
    private String writers;
    @NotBlank(message = "Необходимо указать художников")
    private String artists;


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

    public String getWriters() {
        return writers;
    }

    public void setWriters(String writers) {
        this.writers = writers;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }
}
