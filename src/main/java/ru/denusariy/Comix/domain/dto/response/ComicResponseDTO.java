package ru.denusariy.Comix.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import ru.denusariy.Comix.domain.entity.Artist;
import ru.denusariy.Comix.domain.entity.Book;
import ru.denusariy.Comix.domain.entity.Writer;
import java.util.List;
@Getter
@Setter
public class ComicResponseDTO {
    private int id;
    private String title;
    private int year;
    @JsonIgnore
    private Book book;
    private List<Writer> writers;
    private List<Artist> artists;

}
