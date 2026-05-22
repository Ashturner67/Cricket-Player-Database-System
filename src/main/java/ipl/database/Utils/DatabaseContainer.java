package ipl.database.Utils;

import ipl.database.Clubs.ClubDatabase;
import ipl.database.Players.PlayerDatabase;

public class DatabaseContainer {
    PlayerDatabase playerDatabase;
    ClubDatabase clubDatabase;

    public DatabaseContainer(PlayerDatabase playerDatabase, ClubDatabase clubDatabase) {
        this.playerDatabase = playerDatabase;
        this.clubDatabase = clubDatabase;
    }

    public DatabaseContainer(DatabaseContainer d) {
        this.playerDatabase = d.playerDatabase;
        this.clubDatabase = d.clubDatabase;
    }

    public PlayerDatabase getPlayerDatabase() {
        return playerDatabase;
    }

    public ClubDatabase getClubDatabase() {
        return clubDatabase;
    }
}
