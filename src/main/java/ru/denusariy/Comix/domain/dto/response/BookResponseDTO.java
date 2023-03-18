package ru.denusariy.Comix.domain.dto.response;

import lombok.Getter;
import lombok.Setter;
import ru.denusariy.Comix.domain.entity.Image;
import ru.denusariy.Comix.domain.enums.Format;
import java.util.List;
@Getter
@Setter
public class BookResponseDTO {
    private int id;
    private String title;
    private int year;
    private Format format;
    private boolean isAltCover;
    private boolean isAutograph;
    private String signature;
    private List<ComicResponseDTO> comicsList;
    private Image image;
}
