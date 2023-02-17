package ru.denusariy.Comix.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.denusariy.Comix.dao.BookDAO;
import ru.denusariy.Comix.services.BookService;
import ru.denusariy.Comix.services.ComicService;

@Controller
@RequestMapping("/comix/search")
public class SearchController {
    private final ComicService comicService;
    private final BookService bookService;
    private final BookDAO bookDAO;

    @Autowired
    public SearchController(ComicService comicService, BookService bookService, BookDAO bookDAO) {
        this.comicService = comicService;
        this.bookService = bookService;
        this.bookDAO = bookDAO;
    }

//    @PostMapping("/title")
//    @Operation(summary = "POST-запрос для поиска книги по части строки в названии")
//    public String searchByTitle(Model model, @RequestParam("query") String query) {
//        model.addAttribute("books", bookService.searchByTitle(query));
//        model.addAttribute("query", query);
//        return "search/title";
//    }
//
//    @PostMapping("/altcover")
//    @Operation(summary = "POST-запрос для поиска книг с альтернативной обложкой")
//    public String searchAltCover(Model model) {
//        model.addAttribute("books", bookDAO.altCover());
//        return "search/altcover";
//    }
//
//    @PostMapping("/autograph")
//    @Operation(summary = "POST-запрос для поиска книг с автографом")
//    public String searchAutograph(Model model) {
//        model.addAttribute("books", bookDAO.autograph());
//        return "search/autograph";
//    }
//
//    @PostMapping("/writer")
//    @Operation(summary = "POST-запрос для поиска комиксов по выбранному сценаристу")
//    public String searchByWriter(Model model, @RequestParam("writer") String writer) {
//        model.addAttribute("comics", comicService.findByWriter(writer));
//        model.addAttribute("query", writer);
//        return "search/writer";
//    }
//
//    @PostMapping("/artist")
//    @Operation(summary = "POST-запрос для поиска комиксов по выбранному художнику")
//    public String searchByArtist(Model model, @RequestParam("artist") String artist) {
//        model.addAttribute("comics", comicService.findByArtist(artist));
//        model.addAttribute("query", artist);
//        return "search/artist";
//    }
}
