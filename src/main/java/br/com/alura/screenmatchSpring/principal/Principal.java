package br.com.alura.screenmatchSpring.principal;

import br.com.alura.screenmatchSpring.Model.DadosEpisodio;
import br.com.alura.screenmatchSpring.Model.DadosSerie;
import br.com.alura.screenmatchSpring.Model.DadosTemporada;
import br.com.alura.screenmatchSpring.Model.Episodio;
import br.com.alura.screenmatchSpring.Service.ConsumoApi;
import br.com.alura.screenmatchSpring.Service.ConverteDados;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private ConsumoApi consumo = new ConsumoApi();

    private Scanner leitura = new Scanner(System.in);

    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";

    private final String API_KEY = "&apikey=6fad4aa1";

    public void exibeMenu() {
        var menu = """
                1- Buscar Série
                2 - Buscar episódio
                
                0 - sair
                """;
        System.out.println(menu);
        var opcao = leitura.nextInt();
        leitura.nextLine();

        switch (opcao) {
            case 1:
                buscarSerieWEB();
                break;
            case 2:
                buscarEpisodioPorSerie();
                break;
            case 0:
                System.out.println("Saindo...");
                break;
            default:
                System.out.println("Opção invalida");
        }
    }

    private void buscarSerieWEB(){
        DadosSerie dados = getDadosSerie();
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie(){
        System.out.println("Digite o nome da Serie para buscar");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        DadosSerie dadosSerie =  getDadosSerie();
        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i < dadosSerie.totalTemporadas(); i++) {
            var json = consumo.obterDados(ENDERECO + dadosSerie.titulo().replace(" ", "+")+ "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);
    }
}
