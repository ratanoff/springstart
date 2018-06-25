package ru.ratanov.springstart.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ratanov.springstart.persistence.model.SearchItem;
import ru.ratanov.springstart.persistence.model.TopFilm;
import ru.ratanov.springstart.persistence.repo.TopRepository;
import ru.ratanov.springstart.persistence.repo.SearchRepository;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FilmController {

    @GetMapping("/top")
    public List<TopFilm> getTop() {
        return TopRepository.getTop();
    }

    @GetMapping("/search")
    public List<SearchItem> searchFilm(
            @RequestParam("query") String query,
            @RequestParam(value = "t", required = false, defaultValue = "1") String filter,
            @RequestParam(value = "f", required = false, defaultValue = "0") String sort) {
        return SearchRepository.search(query, filter, sort);
    }
}
