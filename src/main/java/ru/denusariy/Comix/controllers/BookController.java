package ru.denusariy.Comix.controllers;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.denusariy.Comix.domain.dto.request.BookRequestDTO;
import ru.denusariy.Comix.domain.dto.response.BookPageResponseDTO;
import ru.denusariy.Comix.domain.dto.response.BookResponseDTO;
import ru.denusariy.Comix.services.BookService;
import ru.denusariy.Comix.exception.BookNotFoundException;
import ru.denusariy.Comix.services.DeleteService;

import javax.validation.Valid;
import java.io.IOException;


@Controller
@RequestMapping("/comix")
@RequiredArgsConstructor
public class BookController {
    private static final String BOOKS_PER_PAGE = "25";
    private final BookService bookService;
    private final DeleteService deleteService;

    @GetMapping()
    @Operation(summary = "Открытие главной страницы. Здесь список всех книг, возможность добавить новую книгу. " +
            "Работает пагинация")
    public String index(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = BOOKS_PER_PAGE) Integer size) {
        BookPageResponseDTO bookPage = bookService.findAllWithPagination(page, size);
        model.addAttribute("bookPage", bookPage.getBooks());
        model.addAttribute("pageNumbers", bookPage.getPageNumbers());
        return "books/main";
    }

    @GetMapping("/{id}")
    @Operation(summary = "Отображение книги по указанному id. Отображается информация о книге и её содержимом. Имеется " +
            "возможность редактирования и удаления книги, добавление нового комикса в содержимое, редактирование комикса")
    public String show(@PathVariable("id") int id, Model model) {
        try{
            model.addAttribute("book", bookService.findOne(id));
            model.addAttribute("image", bookService.findOne(id).getImage());
        } catch(BookNotFoundException e) {
            return "book_not_found";
        }
        return "books/show";
    }

    @GetMapping("/new")
    @Operation(summary = "Получение формы для создания новой книги")
    public String newBook(@ModelAttribute("book") BookRequestDTO book) {
        return "books/new";
    }

    @PostMapping()
    @Operation(summary = "POST-запрос для создания новой книги, имеется валидация")
    public String create(@ModelAttribute("book") @Valid BookRequestDTO bookDTO, //TODO не работает валидация
                         @RequestParam("image") MultipartFile file,
                         BindingResult bindingResult) throws IOException {
        if(bindingResult.hasErrors())
            return "books/new";
        BookResponseDTO book = bookService.save(bookDTO, file);
        return "redirect:/comix/" + book.getId();
    }

    @GetMapping("/{id}/edit")
    @Operation(summary = "Получение формы для редактирования книги с указанным id")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.findOneForUpdate(id));
        model.addAttribute("id", id);
        return "books/edit";
    }

    @PutMapping("/{id}")
    @Operation(summary = "PUT-запрос для редактирования книги")
    public String update(@PathVariable("id") int id, @Valid BookRequestDTO bookDTO,
                         @RequestParam("image") MultipartFile file,
                         BindingResult bindingResult) throws IOException {
        if(bindingResult.hasErrors())
            return "books/edit"; //TODO не работает валидация
        bookService.update(id, bookDTO, file);
        return "redirect:/comix/" + id;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "DELETE-запрос для удаления книги с указанным id")
    public String delete(@PathVariable("id") int id) {
        deleteService.deleteBook(id);
        return "redirect:/comix";
    }
}
