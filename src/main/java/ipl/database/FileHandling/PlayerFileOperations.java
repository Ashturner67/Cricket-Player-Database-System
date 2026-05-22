package ipl.database.FileHandling;

import ipl.database.Clubs.*;
import ipl.database.Players.*;
import ipl.database.Utils.DatabaseContainer;

import java.io.*;

public class PlayerFileOperations {
    private static final String INPUT_FILE_NAME = "players.txt";
    private static final String OUTPUT_FILE_NAME = "players.txt";

    public static DatabaseContainer readPlayerFile() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_NAME));
        PlayerDatabase retPlayerList = new PlayerDatabase();
        ClubDatabase retClubsList = new ClubDatabase();
        while (true) {
            String line = br.readLine();
            if (line == null) break;
            // Add Players into the player list
            Player temp = getPlayer(line);
            retPlayerList.addPlayer(temp);
            retClubsList.addToClub(temp);
        }
        br.close();
        // Using wrapper class to return two separate kinds of objects
        return new DatabaseContainer(retPlayerList, retClubsList);
    }

    private static Player getPlayer(String line) {
        String[] playerData = line.trim().split(",");
        String name = playerData[0], country = playerData[1], club = playerData[4], position = playerData[5];
        int age = Integer.parseInt(playerData[2]), weeklySalary = Integer.parseInt(playerData[7]);
        int number = (playerData[6].isEmpty()) ? -1 : Integer.parseInt(playerData[6]);
        double height = Double.parseDouble(playerData[3]);
        return new Player(name, country, age, height, club, position, number, weeklySalary);
    }

    public static void writePlayerFile(PlayerDatabase playerDB) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME));
        for(Player x : playerDB.getPlayerList()){
            String text = x.toString();
            bw.write(text);
            bw.write(System.lineSeparator());
        }
        bw.close();
    }
}
