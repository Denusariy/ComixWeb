package ru.denusariy.Comix.domain.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;
@Getter
@Setter
public class ArtistResponseDTO {
    private String name;
    private Page<ComicResponseDTO> comics;
    private List<Integer> pageNumbers;
}
