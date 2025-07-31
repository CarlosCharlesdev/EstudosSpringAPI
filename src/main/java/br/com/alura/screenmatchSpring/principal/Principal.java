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
        System.out.print("Digite o nome da serie para buscar: ");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> todasTemporadas = new ArrayList<>();

        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
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

//        //streams
//        List <String> nomes = Arrays.asList("Jacque", "Iasmin", "Paulo", "Rodrigo", "Nico");
//
//        nomes.stream()
//                .sorted() //Ordena
//                .limit(3) //Limita a 3
//                .filter(n -> n.startsWith("N")) //Pega os nome que começa com N
//                .map(n -> n.toUpperCase()) //Transforma o nome em letra maiuscula
//                .forEach(System.out::println);

//        List<DadosEpisodio> dadosEpisodios = todasTemporadas.stream()
//                .flatMap(t -> t.episodios().stream()) //querer usar uma lista dentro de outra lista e querer puxar todas as informações
//                .toList();  //Coleta para uma lista. toList = lista imultavel nao permite adicionar outras coisas, ja o collector permite.
//
//
//        System.out.println("Top 10 Series");
//        dadosEpisodios.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A")) //Filtra todos que não seja N/A.
//                .peek(e -> System.out.println("primeiro filtro(N/A)" + e))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed()) // compara dados episorios pela avaliação, reversed decrescente.
//                .peek(e -> System.out.println("Ordenação " + e))
//                .limit(10)
//                .peek(e -> System.out.println("Limite " + e))
//                .map(e -> e.titulo().toUpperCase())
//                .peek(e -> System.out.println("Mapeamento " + e))
//                .forEach(System.out::println);



        List<Episodio> episodios = todasTemporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toUnmodifiableList());

        episodios.forEach(System.out::println);

//        System.out.print("Digite o nome do titulo/trecho do episodio: ");
//        var trechoTitulo = leitura.nextLine();
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                .findFirst();
//
//        if (episodioBuscado.isPresent()){
//            System.out.println("Episodio encontrado!");
//            System.out.print("Temporada: " + episodioBuscado.get().getTemporada());
//        } else {
//            System.out.println("Não encontrado! ");
//        }
//
//        System.out.print("A partir de que ano você deseja ver os episodios? ");
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1 ,1);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                                " Episodio: " + e.getTitulo() +
//                                " Data Lancamento: " + e.getDataLancamento().format(formatter)
//                ));

        Map<Integer, Double> avaliacoesTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacoesTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao)); //Coleciona algumas estatisticas
        System.out.println("Media: " + est.getAverage());
        System.out.println("Melhor ep: " + est.getMax());
        System.out.println("Pior ep: " + est.getMin());
        System.out.println("Quantidade " + est.getCount());
    }
}
