package ru.denusariy.Comix.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookPageResponseDTO {

    private Page<BookResponseDTO> books;
    private List<Integer> pageNumbers;
}
