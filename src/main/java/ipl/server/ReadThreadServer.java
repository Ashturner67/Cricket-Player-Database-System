package ipl.server;

import ipl.database.Clubs.*;
import ipl.database.FileHandling.PlayerFileOperations;
import ipl.database.FileHandling.PwdFileOperations;
import ipl.database.Players.Player;
import ipl.utility.*;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class ReadThreadServer implements Runnable {
    private Thread thr;
    private SocketWrapper serverSocketWrapper;
    private HashMap<String, SocketWrapper> clientMap;

    public ReadThreadServer(SocketWrapper serverSocketWrapper, HashMap<String, SocketWrapper> clientMap) {
        this.serverSocketWrapper = serverSocketWrapper;
        this.clientMap = clientMap;
        this.thr = new Thread(this);
        thr.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object o = serverSocketWrapper.read();
                if (o != null) {
                    if (o instanceof LoginDTO) {
                        boolean clientExists = false;
                        LoginDTO loginDTO = (LoginDTO) o;
                        String password = Server.credentialsMap.get(loginDTO.getUserName());
                        loginDTO.setStatus(loginDTO.getPassword().equals(password));
                        if (loginDTO.isStatus()) {
                            String clientName = loginDTO.getUserName();
                            if(clientMap.containsKey(clientName)) clientExists = true;
                            else {
                                clientMap.put(clientName, serverSocketWrapper);
                                boolean hasPlayer = false;
                                for(Club c : Server.clubDatabase.getClubList()){
                                    if(c.getName().equals(clientName)) {
                                        hasPlayer = true;
                                        serverSocketWrapper.write(c);
                                        System.out.println(c.getName() + " logged in");
                                        break;
                                    }
                                }
                                if(!hasPlayer){
                                    serverSocketWrapper.write(new Club(clientName));
                                }
                            }
                        }
                        if(!clientExists) {
                            serverSocketWrapper.write(loginDTO);
                            serverSocketWrapper.write(Server.transferDatabase);
                        }
                        else serverSocketWrapper.write("clientExists");
                    } else if (o instanceof RegisterDTO) {
                        RegisterDTO registerDTO = (RegisterDTO) o;
                        String clubName = registerDTO.getClubName();
                        if (!Server.credentialsMap.containsKey(clubName)) {
                            Server.credentialsMap.put(clubName, registerDTO.getPassword());
                            Server.clubDatabase.addClub(new Club(clubName));
                            System.out.println("New club registered: " + clubName);
                            serverSocketWrapper.write("Registration success," + clubName);
                        }
                        else serverSocketWrapper.write("Registration failed," + clubName);
                    } else if (o instanceof String && ((String) o).startsWith("logout,")) {
                        serverSocketWrapper.write("logout");
                        clientMap.remove(((String) o).substring(7));
                        System.out.println(((String) o).substring(7) + " logged out");
                    } else if (o instanceof PlayerDTO && ((PlayerDTO) o).getMessage().equals("add")) {
                        Player player = ((PlayerDTO) o).getPlayer();
                        if (Server.playerDatabase.playerExists(player.getName())) {
                            serverSocketWrapper.write(new PlayerDTO("don't add", player));
                            System.out.println("don't add");
                        }
                        else {
                            serverSocketWrapper.write(new PlayerDTO("add", player));
                            Server.playerDatabase.addPlayer(player);
                            for(Club c : Server.clubDatabase.getClubList()){
                                if(c.getName().equals(player.getClub())) {
                                    c.getPlayerDatabase().addPlayer(player);
                                    break;
                                }
                            }
                            System.out.println("add");
                        }
                    } else if (o instanceof PlayerDTO && ((PlayerDTO) o).getMessage().equals("sell")) {
                        Server.transferDatabase.addPlayer(((PlayerDTO) o).getPlayer());
                        for (SocketWrapper s : clientMap.values()) {
                            s.reset();
                            s.write(Server.transferDatabase);
                        }
                    } else if (o instanceof ChangePassDTO) {
                        ChangePassDTO changePassDTO = (ChangePassDTO) o;
                        if(!changePassDTO.getCurrentPass().equals(Server.credentialsMap.get(changePassDTO.getClubName())))
                            serverSocketWrapper.write("current password wrong");
                        else{
                            Server.credentialsMap.put(changePassDTO.getClubName(), changePassDTO.getNewPass());
                            PwdFileOperations.writePasswordFile(Server.credentialsMap);
                            serverSocketWrapper.write("password changed");
                        }
                    } else if (o instanceof PlayerDTO && ((PlayerDTO) o).getMessage().equals("unlist")) {
                        Server.transferDatabase.removePlayer(((PlayerDTO) o).getPlayer());
                        for (SocketWrapper s : clientMap.values()) {
                            s.reset();
                            s.write(Server.transferDatabase);
                        }
                    } else if (o instanceof PlayerDTO && ((PlayerDTO) o).getMessage().equals("buy")) {
                        Server.transferDatabase.removePlayer(((PlayerDTO) o).getPlayer());
                        Player player = ((PlayerDTO) o).getPlayer();
                        if(player.getClub() != null){
                            Server.playerDatabase.removePlayer(player);
                            for(Club c : Server.clubDatabase.getClubList()){
                                if(c.getName().equals(player.getClub())) {
                                    c.getPlayerDatabase().removePlayer(player);
                                    String key = c.getName();
                                    clientMap.get(key).write(c);
                                    clientMap.get(key).write(new PlayerDTO("remove", player));
                                    break;
                                }
                            }
                        }
                        for (Map.Entry<String, SocketWrapper> entry : clientMap.entrySet()) {
                            SocketWrapper s = entry.getValue();
                            s.reset();
                            s.write(Server.transferDatabase);
                            if (s.equals(serverSocketWrapper)) {
                                player.setClub(entry.getKey());
                                Server.playerDatabase.addPlayer(player);
                                serverSocketWrapper.write(new PlayerDTO("add", player));
                                for(Club c : Server.clubDatabase.getClubList()){
                                    if(c.getName().equals(player.getClub())) {
                                        c.getPlayerDatabase().addPlayer(player);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            System.out.println("Client disconnected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
        }finally {
                try {
                    serverSocketWrapper.closeConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }
}
