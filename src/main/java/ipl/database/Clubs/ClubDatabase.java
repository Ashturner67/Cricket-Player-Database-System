package ipl.database.Clubs;

import ipl.database.Players.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClubDatabase implements Serializable {
    List<Club> clubList;

    public ClubDatabase() {
        clubList = new ArrayList<>();
    }

    public List<Club> getClubList() {
        return clubList;
    }

    public void addClub(Club c){
        clubList.add(c);
    }

    public void addToClub(Player p){
        boolean clubExistence = clubExists(p.getClub());
        if (!clubExistence) {
            Club newClub = new Club(p.getClub());
            newClub.addPlayer(p);
            this.addClub(newClub);
        }
        else{
            for(Club c : clubList){
                if(c.getName().equalsIgnoreCase(p.getClub())){
                    c.addPlayer(p);
                }
            }
        }
    }

    public boolean clubExists(String name){
        for(Club c : clubList){
            if(c.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty(){
        return clubList.isEmpty();
    }
}
