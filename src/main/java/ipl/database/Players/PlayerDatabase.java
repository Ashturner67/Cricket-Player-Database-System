package ipl.database.Players;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayerDatabase implements Serializable {
    List<Player> playerList;

    public PlayerDatabase() {
        playerList = new ArrayList<>();
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void addPlayer(Player p) {
        playerList.add(p);
    }

    public void removePlayer(Player p) {
        playerList.remove(p);
    }

    public boolean playerExists(String name) {
        for (Player x : playerList) {
            if (x.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return playerList.isEmpty();
    }

    public int size() {
        if (playerList.size() == 1 && playerList.getFirst() == null) return 0;
        return playerList.size();
    }

    public void clear() {
        playerList.clear();
    }

    public void printList() {
        System.out.println();
        for (Player p : getPlayerList()) System.out.println(p);
        System.out.println();
    }

}
