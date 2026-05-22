package ipl.database.SearchOperations;

import ipl.database.Players.Player;
import ipl.database.Players.PlayerDatabase;

import java.util.HashMap;
import java.util.Map;

public class PlayerSearch {

    public static Player searchByName(String name, PlayerDatabase playerDB){
        for(Player x : playerDB.getPlayerList()){
            if(name.equalsIgnoreCase(x.getName())){
                return x;
            }
        }
        return null;
    }

    public static PlayerDatabase searchByCountry(String country, PlayerDatabase playerDB){
        boolean found = false;
        PlayerDatabase returnDB = new PlayerDatabase();
        for(Player x : playerDB.getPlayerList()){
            if(country.equalsIgnoreCase(x.getCountry())){
                returnDB.addPlayer(x);
                found = true;
            }
        }
        if(!found) return null;
        else return returnDB;
    }

    public static PlayerDatabase searchByPosition(String position, PlayerDatabase playerDB){
        boolean found = false;
        PlayerDatabase returnDB = new PlayerDatabase();
        for(Player x : playerDB.getPlayerList()){
            if(position.equalsIgnoreCase(x.getPosition())){
                found = true;
                returnDB.addPlayer(x);
            }
        }
        if(!found) return null;
        else return returnDB;
    }

    public static PlayerDatabase searchBySalaryRange(double min, double max, PlayerDatabase playerDB){
        boolean found = false;
        PlayerDatabase returnDB = new PlayerDatabase();
        for(Player x : playerDB.getPlayerList()){
            if(min <= x.getWeeklySalary() && max >= x.getWeeklySalary()){
                found = true;
                returnDB.addPlayer(x);
            }
        }
        if(!found) return  null;
        else return returnDB;
    }

    public static Map<String, Integer> countryWisePlayerCount(PlayerDatabase playerDB){
        Map<String, Integer> countMap = new HashMap<>();
        for(Player x : playerDB.getPlayerList()){
            countMap.put(x.getCountry(), countMap.getOrDefault(x.getCountry(), 0) + 1);
        }
        return countMap;
    }
}
