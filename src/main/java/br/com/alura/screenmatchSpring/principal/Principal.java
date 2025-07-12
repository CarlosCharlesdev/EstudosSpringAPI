package br.com.alura.screenmatchSpring.principal;

import br.com.alura.screenmatchSpring.Model.DadosEpisodio;
import br.com.alura.screenmatchSpring.Model.DadosSerie;
import br.com.alura.screenmatchSpring.Model.DadosTemporada;
import br.com.alura.screenmatchSpring.Service.ConsumoApi;
import br.com.alura.screenmatchSpring.Service.ConverteDados;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private ConsumoApi consumo = new ConsumoApi();

    private Scanner leitura = new Scanner(System.in);

    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";

    private final String API_KEY = "&apikey=6fad4aa1";

    public void exibeMenu(){
        System.out.print("Digite o nome da serie para buscar: ");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> todasTemporadas = new ArrayList<>();

        for (int i = 1; i <= dados.totalTemporadas() ; i++) {
            json =consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") +"&season="+ i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            todasTemporadas.add(dadosTemporada);
        }
        todasTemporadas.forEach(System.out::println);

//        for (int i = 0; i < dados.totalTemporadas(); i++){
//            List<DadosEpisodio> episodiosTemporada = todasTemporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++){
//              System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }

        todasTemporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        //streams
        List <String> nomes = Arrays.asList("Jacque", "Iasmin", "Paulo", "Rodrigo", "Nico");

        nomes.stream()
                .sorted() //Ordena
                .limit(3) //Limita a 3
                .filter(n -> n.startsWith("N")) //Pega os nome que começa com N
                .map(n -> n.toUpperCase()) //Transforma o nome em letra maiuscula
                .forEach(System.out::println);


    }
}
