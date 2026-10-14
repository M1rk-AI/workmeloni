import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientGiocatore {

    public static void main(String[] args) {

        Scanner tastiera = new Scanner(System.in);

        try {
            System.out.print("IP del server: ");
            String ipServer = tastiera.nextLine();

            System.out.print("Porta del server: ");
            int porta = Integer.parseInt(tastiera.nextLine());

            System.out.print("Nickname: ");
            String nickname = tastiera.nextLine();

            System.out.print("Livello: ");
            int livello = Integer.parseInt(tastiera.nextLine());

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