package ru.denusariy.Comix.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.denusariy.Comix.domain.dto.response.ArtistResponseDTO;
import ru.denusariy.Comix.domain.dto.response.BookPageResponseDTO;
import ru.denusariy.Comix.domain.dto.response.WriterResponseDTO;
import ru.denusariy.Comix.services.SearchService;

@Controller
@RequestMapping("/comix/search")
public class SearchController {
    private final SearchService searchService;
    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping()
    @Operation(summary = "GET-запрос на получение страницы поиска. Получение списков художников и сценаристов " +
            "для выпадающих списков")
    public String searchPage(Model model) {
        model.addAttribute("list", searchService.getArtistWriterResponseDTO());
        return "search/search";
    }

    @GetMapping("/title")
    @Operation(summary = "GET-запрос для поиска книги по части строки в названии, работает пагинация и сортировка " +
            "по названию")
    public String searchByTitle(Model model, @RequestParam("query") String query,
                                @RequestParam(value = "page", defaultValue = "0") Integer page,
                                @RequestParam(value = "size", defaultValue = "12") Integer size) {
        BookPageResponseDTO bookPage = searchService.findBooksByTitle(query, page, size);
        model.addAttribute("bookPage", bookPage.getBooks());
        model.addAttribute("pageNumbers", bookPage.getPageNumbers());
        model.addAttribute("query", query);
        return "search/title";
    }

    @GetMapping("/altcover")
    @Operation(summary = "GET-запрос для поиска книг с альтернативной обложкой, работает пагинация и сортировка " +
            "по названию")
    public String searchAltCover(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page,
                                 @RequestParam(value = "size", defaultValue = "12") Integer size) {
        BookPageResponseDTO bookPage = searchService.findBooksWithAltCover(page, size);
        model.addAttribute("bookPage", bookPage.getBooks());
        model.addAttribute("pageNumbers", bookPage.getPageNumbers());
        return "search/altcover";
    }

    @GetMapping("/autograph")
    @Operation(summary = "GET-запрос для поиска книг с автографом, работает пагинация и сортировка по названию")
    public String searchAutograph(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page,
                                  @RequestParam(value = "size", defaultValue = "12") Integer size) {
        BookPageResponseDTO bookPage = searchService.findBooksWithAutograph(page, size);
        model.addAttribute("bookPage", bookPage.getBooks());
        model.addAttribute("pageNumbers", bookPage.getPageNumbers());
        return "search/autograph";
    }

    @GetMapping("/writer")
    @Operation(summary = "GET-запрос для поиска комиксов по выбранному сценаристу, работает пагинация и сортировка " +
            "по названию")
    public String searchByWriter(Model model, @RequestParam("writer") String writer,
                                 @RequestParam(value = "page", defaultValue = "0") Integer page,
                                 @RequestParam(value = "size", defaultValue = "12") Integer size) {
        WriterResponseDTO comicPage = searchService.findComicsByWriter(writer, page, size);
        model.addAttribute("comicPage", comicPage.getComics());
        model.addAttribute("pageNumbers", comicPage.getPageNumbers());
        model.addAttribute("writer", writer);
        return "search/writer";
    }

    @GetMapping("/artist")
    @Operation(summary = "GET-запрос для поиска комиксов по выбранному художнику, работает пагинация и сортировка " +
            "по названию")
    public String searchByArtist(Model model, @RequestParam("artist") String artist,
                                 @RequestParam(value = "page", defaultValue = "0") Integer page,
                                 @RequestParam(value = "size", defaultValue = "12") Integer size) {
        ArtistResponseDTO comicPage = searchService.findComicsByArtist(artist, page, size);
        model.addAttribute("comicPage", comicPage.getComics());
        model.addAttribute("pageNumbers", comicPage.getPageNumbers());
        model.addAttribute("artist", artist);
        return "search/artist";
    }

}
