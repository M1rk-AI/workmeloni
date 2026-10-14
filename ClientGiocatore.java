import java.io.*;
import java.net.*;

public class ClientGiocatore {

    private static String leggiLinea(BufferedReader input,
            String prompt) throws IOException {
        System.out.print(prompt);
        String linea = input.readLine();

        if (linea == null) {
            throw new EOFException("Input terminato.");
        }

        return linea;
    }

    public static void main(String[] args) {

        try (BufferedReader tastiera =
                new BufferedReader(new InputStreamReader(System.in))) {

            String ipServer = leggiLinea(tastiera, "IP del server: ");
            int porta = Integer.parseInt(
                    leggiLinea(tastiera, "Porta del server: "));
            String nickname = leggiLinea(tastiera, "Nickname: ");
            int livello = Integer.parseInt(
                    leggiLinea(tastiera, "Livello: "));

            // Connessione TCP al server
            try (
                Socket socket = new Socket(ipServer, porta);

                PrintWriter output = new PrintWriter(
                        socket.getOutputStream(), true);

                BufferedReader input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))
            ) {

                // Invia nickname e livello, uno per riga
                output.println(nickname);
                output.println(livello);

                System.out.println(
                        "\nRichiesta inviata. Attendo il match...");

                // Il server invia MATCH quando trova un gruppo
                String risposta = input.readLine();

                if ("ERRORE".equals(risposta)) {
                    System.out.println(
                            "Dati non validi. Riprova.");

                } else if (!"MATCH".equals(risposta)) {
                    System.out.println(
                            "Connessione chiusa senza match.");

                } else {
                    System.out.println(
                            "\nPartita trovata!");
                    System.out.println(
                            "I tuoi compagni sono:");

                    String riga;

                    // Legge le informazioni fino a END
                    while ((riga = input.readLine()) != null
                            && !riga.equals("END")) {

                        String[] dati = riga.split(";", 3);

                        if (dati.length == 3) {
                            System.out.println(
                                    "Nickname: " + dati[0]
                                    + " | IP: " + dati[1]
                                    + " | Livello: " + dati[2]);
                        }
                    }
                }
            }

        } catch (NumberFormatException e) {
            System.out.println(
                    "Porta e livello devono essere numeri.");

        } catch (IOException e) {
            System.out.println(
                    "Errore di connessione: "
                    + e.getMessage());
        }
    }
}