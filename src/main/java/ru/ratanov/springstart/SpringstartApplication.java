package ru.ratanov.springstart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.ratanov.springstart.persistence.repo.BookRepository;

@SpringBootApplication
@EnableScheduling
public class SpringstartApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringstartApplication.class, args);
	}
}
