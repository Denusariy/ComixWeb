package ru.denusariy.Comix.domain.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public class BookPageResponseDTO {

    private Page<BookResponseDTO> books;
    private List<Integer> pageNumbers;

    public BookPageResponseDTO( Page<BookResponseDTO> books, List<Integer> pageNumbers) {
        this.books = books;
        this.pageNumbers = pageNumbers;
    }

    public BookPageResponseDTO() {
    }

    public  Page<BookResponseDTO> getBooks() {
        return books;
    }

    public void setBooks( Page<BookResponseDTO> books) {
        this.books = books;
    }

    public List<Integer> getPageNumbers() {
        return pageNumbers;
    }

    public void setPageNumbers(List<Integer> pageNumbers) {
        this.pageNumbers = pageNumbers;
    }
}
