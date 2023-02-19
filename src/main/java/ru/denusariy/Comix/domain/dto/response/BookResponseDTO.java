package ru.denusariy.Comix.domain.dto.response;

import ru.denusariy.Comix.domain.enums.Format;
import java.util.List;

public class BookResponseDTO {
    private int id;
    private String title;
    private int year;
    private Format format;
    private boolean isAltCover;
    private boolean isAutograph;
    private String signature;
    private List<ComicResponseDTO> comicsList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public List<ComicResponseDTO> getComicsList() {
        return comicsList;
    }

    public void setComicsList(List<ComicResponseDTO> comicsList) {
        this.comicsList = comicsList;
    }
}
