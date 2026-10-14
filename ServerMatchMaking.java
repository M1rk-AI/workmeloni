import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class ServerMatchMaking {

    //parametri di configurazione

    private static final int PORTA = 5000; 
    private static final int NGG = 3;
    private static final int DML = 2;

    //Giocatori in attesa di match
    private static final List<Giocatore>InAttesa = new ArrayList<>();

        // Protegge il pool quando più thread lo utilizzano
    private static final Object lock = new Object();

    public static void main(String[] args) {

       ServerSocket server = null;

       try {
        server = new ServerSocket(PORTA);

        System.out.println("Giocatori per gruppo: " + NGG);

       System.out.println("Differenza massima: " + DML);

       System.out.println("Scrivi stop per terminare.");

         // Thread dedicato al comando dell'amministratore
            final ServerSocket serverAdmin = server;

                       Thread amministratore = new Thread(() -> {
                Scanner tastiera = new Scanner(System.in);

                 while (tastiera.hasNextLine()) {
                    String comando = tastiera.nextLine();

                        if (comando.trim().equalsIgnoreCase("stop")) {
                        try {
                            serverAdmin.close();
                        } catch (IOException e) {

                            System.out.println(
                                    "Errore durante la chiusura.");
                        }

                        break;
                    }
                }
            });

            amministratore.setDaemon(true);
            amministratore.start();

            // Il server continua ad accettare connessioni
            while (true) {

                try {
                    Socket socket = server.accept();

                    System.out.println("Nuovo client: "
                            + socket.getInetAddress()
                                    .getHostAddress());

                    // Un thread indipendente per ogni client
                    Thread thread =
                            new Thread(new GestoreClient(socket));

                            thread.setDaemon(true);
                    thread.start();

                } catch (SocketException e) {
                    // La chiusura del server interrompe accept()
                    if (server.isClosed()) {
                        break;
                    }

                    throw e;
                }
            }

               } catch (IOException e) {
            System.out.println("Errore server: "
                    + e.getMessage());

        } finally {
            if (server != null && !server.isClosed()) {
                try {
                    server.close();
                } catch (IOException ignored) {
                }
            }
        }

        System.out.println("Server terminato.");
    }

    // Rappresenta un giocatore
    static class Giocatore {

        String nickname;
        String ip;
        int livello;
        PrintWriter output;

         // Il thread resta in attesa finché non viene abbinato
        CountDownLatch attesaMatch = new CountDownLatch(1);

         Giocatore(String nickname, String ip,
                  int livello, PrintWriter output) {
            this.nickname = nickname;
            this.ip = ip;
            this.livello = livello;
            this.output = output;
        }
    }

    // Gestisce la singola connessione
    static class GestoreClient implements Runnable {

        private final Socket socket;

        GestoreClient(Socket socket) {
            this.socket = socket;
        }

         @Override
        public void run() {

            try (
                Socket s = socket;
                BufferedReader input = new BufferedReader(
                        new InputStreamReader(s.getInputStream()));
                PrintWriter output = new PrintWriter(
                        s.getOutputStream(), true)
            ) {




       }