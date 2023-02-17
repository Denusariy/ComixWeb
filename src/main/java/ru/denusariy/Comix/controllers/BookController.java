package ru.denusariy.Comix.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.denusariy.Comix.domain.dto.request.BookRequestDTO;
import ru.denusariy.Comix.domain.dto.response.BookResponseDTO;
import ru.denusariy.Comix.domain.entity.Book;
import ru.denusariy.Comix.services.BookService;
import ru.denusariy.Comix.services.ComicService;
import ru.denusariy.Comix.exception.BookNotFoundException;
import javax.validation.Valid;
import java.util.Optional;


@Controller
@RequestMapping("/comix")
public class BookController {
    private final BookService bookService;
    private final ComicService comicService;
    @Autowired
    public BookController(BookService bookService, ComicService comicService) {
        this.bookService = bookService;
        this.comicService = comicService;
    }

//    @GetMapping()
//    @Operation(summary = "Открытие главной страницы. Здесь список всех книг, возможность добавить новую книгу и " +
//            "различные варианты поиска. Работает пагинация")
//    public String index(Model model, @RequestParam("page") Optional<Integer> page,
//                        @RequestParam("size") Optional<Integer> size) {
//        Page<Book> bookPage = bookService.findWithPagination(page, size);
//        model.addAttribute("bookPage", bookPage);
//        model.addAttribute("pageNumbers", bookService.getPageNumbers(bookPage));
//        model.addAttribute("writers", comicService.allWriters());
//        model.addAttribute("artists", comicService.allArtists());
//        return "books/main";
//    }
//
//

    @GetMapping()
    @Operation(summary = "Открытие главной страницы. Здесь список всех книг, возможность добавить новую книгу. " +
            "Работает пагинация")
    public String index(Model model, @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
        Page<BookResponseDTO> bookPage = bookService.findWithPagination(page, size);
        model.addAttribute("bookPage", bookPage);
        model.addAttribute("pageNumbers", bookService.getPageNumbers(bookPage));
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
//        model.addAttribute("book_id", id);
        return "books/edit";
    }

    @PutMapping("/{id}")
    @Operation(summary = "PUT-запрос для редактирования книги, имеется валидация")
    public String update(@PathVariable("id") int id, @Valid BookRequestDTO bookDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "redirect:/comix/" + id + "/edit"; //Возвращает без указания на ошибки валидации TODO
        bookService.update(id, bookDTO);
        return "redirect:/comix/" + id;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "DELETE-запрос для удаления книги с указанным id")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/comix";
    }
}
