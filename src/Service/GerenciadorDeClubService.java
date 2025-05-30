package Service;
import model.Clube;
import model.Partidas;
import model.SerieA;
import model.SerieB;
import java.util.ArrayList;
import java.util.List;


public class GerenciadorDeClubService {
    
    // Atributos do gerenciador de clubes
    private List<Clube> clubes;
    private Partidas.Confederacao confederacao;

    /**
     * Construtor da classe GerenciadorDeClubService
     */
    public GerenciadorDeClubService() {
        this.clubes = new ArrayList<>();
        this.confederacao = new Partidas.Confederacao("CBF", "Brasil", 1914, "Ednaldo Rodrigues");
        inicializarDados();
    }

    /**
     * Método para inicializar dados de exemplo
     */
    private void inicializarDados() {
        // Adiciona alguns clubes de exemplo
        Clube flamengo = new Clube("Flamengo", "Rio de Janeiro", 1895, "Maracanã", 45);
        Clube palmeiras = new Clube("Palmeiras", "São Paulo", 1914, "Allianz Parque", 42);
        Clube gremio = new Clube("Grêmio", "Porto Alegre", 1903, "Arena do Grêmio", 39);

        clubes.add(flamengo);
        clubes.add(palmeiras);
        clubes.add(gremio);

        // Adiciona os clubes à confederação
        confederacao.adicionarClube(flamengo);
        confederacao.adicionarClube(palmeiras);
        confederacao.adicionarClube(gremio);

        // Adiciona campeonatos
        SerieA serieA = new SerieA("Campeonato Brasileiro Série A", "Rio de Janeiro", 1971,
                                   "Diversos", 51, 2023, 20, "Palmeiras", "Botafogo", 38);
        clubes.add(serieA);
        confederacao.adicionarClube(serieA);

        String[] promovidos = {"Vitória", "Juventude", "Criciúma", "Atlético-GO"};
        String[] rebaixados = {"Londrina", "Tombense", "Chapecoense", "ABC"};
        SerieB serieB = new SerieB("Campeonato Brasileiro Série B", "Rio de Janeiro", 1971,
                                   "Diversos", 51, 2023, 20, promovidos, rebaixados, 38);
        clubes.add(serieB);
    }
}
