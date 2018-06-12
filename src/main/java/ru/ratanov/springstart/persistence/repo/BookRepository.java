package ru.ratanov.springstart.persistence.repo;

import org.springframework.data.repository.CrudRepository;
import ru.ratanov.springstart.persistence.model.Book;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findByTitle(String title);


}
