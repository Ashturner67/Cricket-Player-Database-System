package ipl.utility;

import ipl.database.Players.Player;

import java.io.Serializable;

public class PlayerDTO implements Serializable {

    private String message;
    private Player player;

    public PlayerDTO(String message, Player player) {
        this.message = message;
        this.player = player;
    }

    public String getMessage() {
        return message;
    }

    public Player getPlayer() {
        return player;
    }
}
