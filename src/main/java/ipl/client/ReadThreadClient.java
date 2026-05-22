package ipl.client;

import ipl.database.Clubs.Club;
import ipl.database.Players.Player;
import ipl.database.Players.PlayerDatabase;
import ipl.server.Server;
import ipl.utility.LoginDTO;
import ipl.utility.PlayerDTO;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.IOException;

public class ReadThreadClient implements Runnable{
    private final Thread thr;
    private final ClientApp clientApp;

    public ReadThreadClient(ClientApp clientApp) {
        this.clientApp = clientApp;
        this.thr = new Thread(this);
        thr.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object o = clientApp.getSocketWrapper().read();
                if (o != null) {
                    if (o instanceof LoginDTO) {
                        LoginDTO loginDTO = (LoginDTO) o;
                        System.out.println(loginDTO.getUserName());
                        System.out.println(loginDTO.isStatus());
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (loginDTO.isStatus()) {
                                    try {
                                        clientApp.showHomePage();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    clientApp.showIncorrectAlert();
                                }

                            }
                        });
                    } else if (o.equals("clientExists")) {
                        System.out.println("Client already exists");
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                clientApp.showExistsAlert();
                            }
                        });
                    } else if (o instanceof Club) {
                        clientApp.setClub((Club) o);
                    } else if (o instanceof PlayerDatabase) {
                        clientApp.setTransferDatabase((PlayerDatabase) o);
                        if(clientApp.isOnTransferMarket()){
                            Platform.runLater(() -> {
                                clientApp.getTransferMarketController().dynamicLoading((PlayerDatabase) o);
                            });
                        }
                    } else if (o.equals("logout")) {
                        clientApp.setClub(null);
                    } else if (o instanceof PlayerDTO) {
                        if(((PlayerDTO) o).getMessage().equals("add") || ((PlayerDTO) o).getMessage().equals("don't add")){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    if (((PlayerDTO) o).getMessage().equals("add")) {
                                        clientApp.getPlayerDatabase().addPlayer(((PlayerDTO) o).getPlayer());
                                        clientApp.getAddPlayerController().getStatus_Text().setText("Player has been added successfully");
                                    } else {
                                        clientApp.getAddPlayerController().getStatus_Text().setText("Player already exists");
                                    }
                                }
                            });
                        } else if (((PlayerDTO) o).getMessage().equals("remove")) {
                            if(clientApp.isOnHomePage()){
                                Platform.runLater(() -> {
                                    ObservableList<Player> data = clientApp.getHomeController().getData();
                                    data.remove(((PlayerDTO) o).getPlayer());
                                    clientApp.getHomeController().showTable(data);
                                });
                            }
                        }
                    } else if (o.equals("current password wrong")) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                clientApp.getChangePassController().getMessage_Text().setText("Enter the correct current password");
                            }
                        });
                    } else if (o.equals("password changed")) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                clientApp.getChangePassController().getMessage_Text().setText("Password is now changed");
                            }
                        });
                    } else if (o instanceof String && ((String) o).startsWith("Registration success,")) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                String clubName = ((String) o).substring(21);
                                String message = clubName + " has successfully registered.";
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Registration Success");
                                alert.setHeaderText("Congratulations");
                                alert.setContentText(message);
                                alert.showAndWait();
                                clientApp.getRegisterController().closeRegisterPage();
                            }
                        });
                    } else if (o instanceof String && ((String) o).startsWith("Registration failed,")) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                clientApp.getRegisterController().getMessage_Text().setText(((String) o).substring(20) + " is already registered");
                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                clientApp.getSocketWrapper().closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
