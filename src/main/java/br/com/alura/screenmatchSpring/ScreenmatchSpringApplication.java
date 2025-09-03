package br.com.alura.screenmatchSpring;

import br.com.alura.screenmatchSpring.Repository.SerieRepository;
import br.com.alura.screenmatchSpring.principal.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchSpringApplication implements CommandLineRunner {

	@Autowired
	private SerieRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchSpringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repository);
		principal.exibeMenu();


	}
}
