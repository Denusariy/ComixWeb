package ru.denusariy.Comix.domain.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public class WriterResponseDTO {
    private String name;
    private Page<ComicResponseDTO> comics;
    private List<Integer> pageNumbers;

    public List<Integer> getPageNumbers() {
        return pageNumbers;
    }

    public void setPageNumbers(List<Integer> pageNumbers) {
        this.pageNumbers = pageNumbers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Page<ComicResponseDTO> getComics() {
        return comics;
    }

    public void setComics(Page<ComicResponseDTO> comics) {
        this.comics = comics;
    }
}
