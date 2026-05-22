package ipl.database.Clubs;

import ipl.database.Players.Player;
import ipl.database.Players.PlayerDatabase;

import java.io.Serializable;
import java.util.List;

public class Club implements Serializable {
    String name;
    int totalSalary;
    PlayerDatabase playerDatabase;

    public Club(String name) {
        this.name = name;
        playerDatabase = new PlayerDatabase();
    }

    public String getName() {
        return name;
    }

    public List<Player> getPlayerList() {
        return playerDatabase.getPlayerList();
    }

    public PlayerDatabase getPlayerDatabase() {
        return playerDatabase;
    }

    public int getTotalSalary() {
        return totalSalary;
    }

    public void addPlayer(Player p) {
        playerDatabase.addPlayer(p);
        totalSalary += p.getWeeklySalary();
    }
}
