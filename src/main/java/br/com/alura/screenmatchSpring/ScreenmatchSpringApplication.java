package br.com.alura.screenmatchSpring;

import br.com.alura.screenmatchSpring.Model.DadosEpisodio;
import br.com.alura.screenmatchSpring.Model.DadosSerie;
import br.com.alura.screenmatchSpring.Model.DadosTemporada;
import br.com.alura.screenmatchSpring.Service.ConsumoApi;
import br.com.alura.screenmatchSpring.Service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchSpringApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchSpringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=6fad4aa1");
//		System.out.println(json);
//		json = consumoApi.obterDados("https://coffee.alexflipnote.dev/random.json");
//		System.out.println(json);

		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);

		json =consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=6fad4aa1");

		DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);
		System.out.println(dadosEpisodio);

		List<DadosTemporada> todasTemporadas = new ArrayList<>();

		for (int i = 1; i <= dados.totalTemporadas() ; i++) {
			json =consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&season=" + i + "&apikey=6fad4aa1");
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			todasTemporadas.add(dadosTemporada);
		}
		todasTemporadas.forEach(System.out::println);
	}
}
