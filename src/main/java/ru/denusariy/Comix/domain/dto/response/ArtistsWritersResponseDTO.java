package ru.denusariy.Comix.domain.dto.response;

import lombok.Getter;
import lombok.Setter;
import ru.denusariy.Comix.domain.entity.Artist;
import ru.denusariy.Comix.domain.entity.Writer;

import java.util.List;
@Getter
@Setter
public class ArtistsWritersResponseDTO {
    private List<Writer> writers;
    private List<Artist> artists;
}
