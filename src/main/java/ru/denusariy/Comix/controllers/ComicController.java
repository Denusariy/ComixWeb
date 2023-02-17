package ru.denusariy.Comix.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.denusariy.Comix.domain.dto.request.ComicRequestDTO;
import ru.denusariy.Comix.domain.entity.Comic;
import ru.denusariy.Comix.services.BookService;
import ru.denusariy.Comix.services.ComicService;
import ru.denusariy.Comix.services.WriterService;

import javax.validation.Valid;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/comix")
public class ComicController {
    private final ComicService comicService;
    private final BookService bookService;

    private final WriterService writerService;

    public ComicController(ComicService comicService, BookService bookService, WriterService writerService) {
        this.comicService = comicService;
        this.bookService = bookService;
        this.writerService = writerService;
    }

    @GetMapping("/{book_id}/new_comic")
    @Operation(summary = "Получение формы для создания нового комикса. Комикс получит id данной книги в качестве " +
            "внешнего ключа. На странице отображаются уже добавленные комиксы в данной книге")
    public String newComic(@PathVariable("book_id") int book_id, Model model,
                           @ModelAttribute("comic") ComicRequestDTO comicDTO) {
        model.addAttribute("book", bookService.findOne(book_id));
        return "comics/new_comic";
    }

    @PostMapping("/{book_id}/new_comic")
    @Operation(summary = "POST-запрос для создания нового комикса, имеется валидация")
    public String create(@PathVariable("book_id") int book_id, Model model,
                         @ModelAttribute("comic") @Valid ComicRequestDTO comicDTO,
                         BindingResult bindingResult) {
        model.addAttribute("book", bookService.findOne(book_id));
        if(bindingResult.hasErrors())
            return "comics/new_comic";
        comicService.save(book_id, comicDTO);
        return "redirect:/comix/" + book_id;
    }

    @GetMapping("/comics/{comic_id}/edit")
    @Operation(summary = "Получение формы для редактирования комикса с указанным id. На странице имеется кнопка " +
            "удаления комикса")
    public String edit(@PathVariable("comic_id") int comic_id, Model model) {
        model.addAttribute("comic", comicService.findOneForPatch(comic_id));
        model.addAttribute("comic_id", comic_id);
        return "comics/edit";
    }

    @PatchMapping("/comics/{comic_id}")
    @Operation(summary = "PATCH-запрос для редактирования комикса, имеется валидация")
    public String update(@PathVariable("comic_id") int comic_id, @Valid ComicRequestDTO requestDTO,
                         BindingResult bindingResult) {
        int book_id = comicService.findOne(comic_id).getBook().getId();
        if(bindingResult.hasErrors())
            return "redirect:/comix/comics/" + comic_id + "/edit"; //Возвращает без указания на ошибки валидации TODO
        comicService.update(comic_id, requestDTO);
        return "redirect:/comix/" + book_id;
    }
    @DeleteMapping("/comics/{comic_id}/delete")
    @Operation(summary = "DELETE-запрос для удаления комикса с указанным id")
    public String delete(@PathVariable("comic_id") int comic_id) {
        int book_id = comicService.findOne(comic_id).getBook().getId();
        comicService.delete(comic_id);
        return "redirect:/comix/" + book_id;
    }
}
