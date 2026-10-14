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

    
