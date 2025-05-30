package TCP;

import Utils.Desempacotamento;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import model.Clube;
import model.Partidas;
import model.SerieA;
import model.SerieB;

/**
 * Classe TCP.ServidorClube - Implementa um servidor para o sistema de clubes
 * Recebe conexões de Clubes e processa requisições
 */
public class ServidorClube {
    private static final int PORTA = 12345;
    private List<Clube> clubes;
    private Partidas.Confederacao confederacao;
    
    /**
     * Construtor da classe TCP.ServidorClube
     */
    public ServidorClube() {
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
        confederacao.adicionarClube(serieB);
        
        String[] paises = {"Brasil", "Argentina", "Uruguai", "Paraguai", "Chile", "Colômbia", "Equador", "Venezuela", "Bolívia", "Peru"};
        SerieA.Libertadores libertadores = new SerieA.Libertadores("Copa model.SerieA.Libertadores", "Assunção", 1960,
                                                   "Diversos", 64, 2023, 32, paises, "Fluminense", 
                                                   "Boca Juniors", "Final");
        clubes.add(libertadores);
        confederacao.adicionarClube(libertadores);
    }

    /**
     * Método principal
     * @param args Argumentos de linha de comando
     */
    public static void main(String args[]) {
        try {
            System.out.println("Servidor iniciado");
            int serverPort = 7896; // the server port
            ServerSocket listenSocket = new ServerSocket(serverPort);
            while (true) {
                Socket clientSocket = listenSocket.accept();
                System.out.println(clientSocket.getInetAddress());
                System.out.println("conexão estabelecida");
                Connection c = new Connection(clientSocket);
            }
        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        }
    }

}

class Connection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;

    public Connection(Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    public void run() {
        try {
            // 1. Ler o TAMANHO da mensagem
            int length = in.readInt();
            System.out.println("Servidor: Recebendo " + length + " bytes...");

            if (length > 0) {
                // 2. Ler EXATAMENTE essa quantidade de bytes
                byte[] dadosRecebidos = new byte[length];
                in.readFully(dadosRecebidos, 0, dadosRecebidos.length);

                // 3. USAR O NOVO MÉTODO para desempacotar os bytes em objetos
                System.out.println("Servidor: Desempacotando os objetos...");
                ArrayList<Object> listaDeClubes = Desempacotamento.lerArrayDeBytes(dadosRecebidos);

                // 4. Processar os dados e enviar a resposta
                System.out.println("Servidor: " + listaDeClubes.size() + " Clubes recebidos com sucesso.");
                // Exemplo: Imprimir o nome do primeiro Clube
                if (!listaDeClubes.isEmpty() && listaDeClubes.getFirst() instanceof Clube primeiroClube) {
                  System.out.println("Nome do primeiro Clube: " + primeiroClube.getNome());
                }
                // 5. Enviar a resposta de volta ao cliente
                System.out.println("Servidor: Enviando resposta ao cliente...");
                // Resposta personalizada
                // 6. Enviar lista de competições que o clube participa, Exemplo: Falemnego participa da Série A, Copa do Brasil
                StringBuilder sb = new StringBuilder();
                for (Object obj : listaDeClubes) {
                    if (obj instanceof Clube clube) {
                        sb.append(clube.getNome()).append(" participa de: ");
                        if (clube instanceof SerieA) {
                            sb.append("Série A, ");
                        }
                        if (clube instanceof SerieB) {
                            sb.append("Série B, ");
                        }
                        if (clube instanceof SerieA.Libertadores) {
                            sb.append("Copa Libertadores, ");
                        }
                        sb.setLength(sb.length() - 2); // Remove a última vírgula e espaço
                        sb.append(".\n");
                    }
                }
                out.writeUTF(sb.toString());


                String resposta = "Servidor processou " + listaDeClubes.size() + " objetos.";
                out.writeUTF(resposta.toUpperCase());
            }

        } catch (IOException e) {
            System.out.println("Erro na conexão: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) { /* Ignorar */}
        }
    }
}
