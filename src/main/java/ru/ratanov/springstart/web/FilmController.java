package ru.ratanov.springstart.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ratanov.springstart.persistence.model.*;
import ru.ratanov.springstart.persistence.repo.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FilmController {

    @GetMapping("/top")
    public List<TopFilm> getTop() {
        return TopRepository.getTop();
    }

    @GetMapping("/filters")
    public List<Filter> getFilters() { return FilterRepository.Companion.getFilters(); }

    @GetMapping("/search")
    public List<SearchItem> searchFilm(
            @RequestParam("query") String query,
            @RequestParam(value = "t", required = false, defaultValue = "1") String filter,
            @RequestParam(value = "f", required = false, defaultValue = "0") String sort) {
        return SearchRepository.search(query, filter, sort);
    }

    @GetMapping("getFilm")
    public Film getFilm(@RequestParam("id") String id) {
        return FilmRepository.getFilm(id);
    }

    @GetMapping("getSame")
    public List<SameItem> getSame(@RequestParam("sameLink") String sameLink) {
        return SameRepository.getSameItems(sameLink);
    }

    @GetMapping("getSameById")
    public List<SameItem> getSameById(@RequestParam("id") String id) {
        return SameRepository.getSameById(id);
    }
}
