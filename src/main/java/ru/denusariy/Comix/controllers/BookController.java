package ru.denusariy.Comix.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.denusariy.Comix.domain.dto.request.BookRequestDTO;
import ru.denusariy.Comix.domain.dto.response.BookPageResponseDTO;
import ru.denusariy.Comix.domain.dto.response.BookResponseDTO;
import ru.denusariy.Comix.services.BookService;
import ru.denusariy.Comix.exception.BookNotFoundException;
import ru.denusariy.Comix.services.DeleteService;

import javax.validation.Valid;


@Controller
@RequestMapping("/comix")
public class BookController {
    private final BookService bookService;
    private final DeleteService deleteService;
    @Autowired
    public BookController(BookService bookService, DeleteService deleteService) {
        this.bookService = bookService;
        this.deleteService = deleteService;
    }

    @GetMapping()
    @Operation(summary = "Открытие главной страницы. Здесь список всех книг, возможность добавить новую книгу. " +
            "Работает пагинация")
    public String index(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "20") Integer size) {
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
    public String create(@ModelAttribute("book") @Valid BookRequestDTO bookDTO,
                         BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "books/new";
        BookResponseDTO book = bookService.save(bookDTO);
        return "redirect:/comix/" + book.getId();
    }

    @GetMapping("/{id}/edit")
    @Operation(summary = "Получение формы для редактирования книги с указанным id")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.findOne(id));
        return "books/edit";
    }

    @PutMapping("/{id}")
    @Operation(summary = "PUT-запрос для редактирования книги")
    public String update(@PathVariable("id") int id, @Valid BookRequestDTO bookDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "redirect:/comix/" + id + "/edit"; //Возвращает без указания на ошибки валидации TODO
        bookService.update(id, bookDTO);
        return "redirect:/comix/" + id;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "DELETE-запрос для удаления книги с указанным id")
    public String delete(@PathVariable("id") int id) {
        deleteService.deleteBook(id);
        return "redirect:/comix";
    }
}
