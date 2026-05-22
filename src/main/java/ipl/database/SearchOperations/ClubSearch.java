package ipl.database.SearchOperations;

import ipl.database.Players.Player;
import ipl.database.Players.PlayerDatabase;

public class ClubSearch {
    public static PlayerDatabase MaxSalaryPlayers(String name, PlayerDatabase playerDB){
        PlayerDatabase returnDB = new PlayerDatabase();
        int maxSalary = Integer.MIN_VALUE;
        for(Player p : playerDB.getPlayerList()){
            if(p.getWeeklySalary() >= maxSalary) maxSalary = p.getWeeklySalary();
        }
        for(Player p : playerDB.getPlayerList()){
            if(p.getWeeklySalary() == maxSalary) returnDB.addPlayer(p);
        }
        return returnDB;
    }

    public static PlayerDatabase MaxAgePlayers(String name, PlayerDatabase playerDB){
        PlayerDatabase returnDB = new PlayerDatabase();
        int maxAge = Integer.MIN_VALUE;
        for(Player p : playerDB.getPlayerList()){
            if(p.getAge() >= maxAge) maxAge = p.getAge();
        }
        for(Player p : playerDB.getPlayerList()){
            if(p.getAge() == maxAge) returnDB.addPlayer(p);
        }
        return returnDB;
    }

    public static PlayerDatabase MaxHeightPlayers(String name, PlayerDatabase playerDB){
        PlayerDatabase returnDB = new PlayerDatabase();
        double maxHeight = Double.MIN_VALUE;
        for(Player p : playerDB.getPlayerList()){
            if(p.getHeight() >= maxHeight) maxHeight = p.getHeight();
        }
        for(Player p : playerDB.getPlayerList()){
            if(p.getHeight() == maxHeight) returnDB.addPlayer(p);
        }
        return returnDB;
    }

}
