package ipl.server;

import ipl.database.Clubs.ClubDatabase;
import ipl.database.FileHandling.PlayerFileOperations;
import ipl.database.FileHandling.PwdFileOperations;
import ipl.database.FileHandling.TransferFileOperations;
import ipl.database.Players.Player;
import ipl.database.Players.PlayerDatabase;
import ipl.database.Utils.DatabaseContainer;
import ipl.utility.SocketWrapper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
    private ServerSocket serverSocket;
    public static HashMap<String, String> credentialsMap;
    public HashMap<String, SocketWrapper> clientMap = new HashMap<>();

    public static PlayerDatabase playerDatabase;
    public static ClubDatabase clubDatabase;
    public static PlayerDatabase transferDatabase;

    Server() throws Exception {
        credentialsMap = PwdFileOperations.readPasswordFile();
        loadPlayerDatabase();
        loadTransferDatabase();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("Shutting down server...");
                PlayerFileOperations.writePlayerFile(playerDatabase);
                TransferFileOperations.writeTransferFile(transferDatabase);
                PwdFileOperations.writePasswordFile(Server.credentialsMap);
                System.out.println("All List saved successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        try {
            serverSocket = new ServerSocket(44456);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                serve(clientSocket);
            }
        } catch (Exception e) {
            System.out.println("Server starts:" + e);
        }
    }

    public void serve(Socket clientSocket) throws IOException, ClassNotFoundException {
        SocketWrapper serverSocketWrapper = new SocketWrapper(clientSocket);
        new ReadThreadServer(serverSocketWrapper, clientMap);
    }

    public static void loadPlayerDatabase() throws Exception {
        DatabaseContainer databases = PlayerFileOperations.readPlayerFile();
        playerDatabase = databases.getPlayerDatabase();
        clubDatabase = databases.getClubDatabase();
    }

    public static void loadTransferDatabase() throws Exception {
        transferDatabase = TransferFileOperations.readTransferFile();
    }

    public static void main(String[] args) throws Exception {
        new Server();
    }

}
