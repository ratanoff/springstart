package ru.ratanov.springstart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.ratanov.springstart.persistence.repo.BookRepository;

@SpringBootApplication
public class SpringstartApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringstartApplication.class, args);
	}
}
