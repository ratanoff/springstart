package ru.ratanov.springstart.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ratanov.springstart.persistence.model.TopFilm;
import ru.ratanov.springstart.persistence.repo.FilmRepository;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FilmController {

    @GetMapping("/top")
    public List<TopFilm> getTop() {
        return FilmRepository.getTop();
    }
}
