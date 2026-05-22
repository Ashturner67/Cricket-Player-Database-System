package ipl.client;

import ipl.database.Players.Player;
import ipl.database.Players.PlayerDatabase;
import ipl.utility.PlayerDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.io.IOException;

public class TransferMarketController {

    private ClientApp clientApp;
    private boolean initialized = true;
    private boolean buyMode;
    private PlayerDatabase ownListedPlayers = new PlayerDatabase();
    private PlayerDatabase othersListedPlayers = new PlayerDatabase();
    ObservableList<Player> data;

    void setClientApp(ClientApp clientApp) {
        this.clientApp = clientApp;
    }

    @FXML
    public Label Count_Label;
    @FXML
    public Text Message_Text;
    @FXML
    public Button Action_Button;
    @FXML
    public Label Club_Label;
    @FXML
    private TableView<Player> TransferList_TableView;
    @FXML
    private ComboBox<String> MarketType_Combobox;
    @FXML
    private TableColumn<Player,String> Name_Col;
    @FXML
    private TableColumn<Player,String> Club_Col;
    @FXML
    private TableColumn<Player,String> Country_Col;
    @FXML
    private TableColumn<Player,String> Age_Col;
    @FXML
    private TableColumn<Player,String> Position_Col;
    @FXML
    private TableColumn<Player,String> Height_Col;
    @FXML
    private TableColumn<Player,String> Jersey_Col;
    @FXML
    private TableColumn<Player,String> Salary_Col;

    @FXML
    public void initialize(String s){
        Club_Label.setText(s);
        MarketType_Combobox.getItems().addAll("Available Players", "My Listed Players");
        MarketType_Combobox.setValue("Available Players");
        Action_Button.setText("Buy");
        buyMode = true;
        loadListedPlayers(clientApp.getTransferDatabase());

    }

    public void load(){
        if (initialized) {
            initializeColumns();
            initialized = false;
        }
        data = FXCollections.observableArrayList(othersListedPlayers.getPlayerList());
        Count_Label.setText("Total: " + data.size() + " players");
        TransferList_TableView.setEditable(true);
        TransferList_TableView.setItems(data);
    }

    private void initializeColumns() {
        Name_Col.setCellValueFactory(new PropertyValueFactory<>("name"));
        Country_Col.setCellValueFactory(new PropertyValueFactory<>("country"));
        Age_Col.setCellValueFactory(new PropertyValueFactory<>("age"));
        Height_Col.setCellValueFactory(new PropertyValueFactory<>("height"));
        Club_Col.setCellValueFactory(new PropertyValueFactory<>("club"));
        Position_Col.setCellValueFactory(new PropertyValueFactory<>("position"));
        Jersey_Col.setCellValueFactory(new PropertyValueFactory<>("number"));
        Salary_Col.setCellValueFactory(new PropertyValueFactory<>("weeklySalary"));
        for (TableColumn<?, ?> column : TransferList_TableView.getColumns()) {
            column.setStyle("-fx-alignment: center;");
        }
        Jersey_Col.setCellValueFactory(cellData -> {
            Player player = cellData.getValue();
            if(player == null) return new SimpleStringProperty("");
            return new SimpleStringProperty(player.getNumber() == -1 ? "Not Available" : String.valueOf(player.getNumber()));
        });
    }

    @FXML
    public void ComboBox_Action(ActionEvent actionEvent){
        if (MarketType_Combobox.getValue().equals("Available Players")) {
            Action_Button.setText("Buy");
            buyMode = true;
            showTable(othersListedPlayers);
        } else if (MarketType_Combobox.getValue().equals("My Listed Players")){
            Action_Button.setText("Unlist");
            buyMode = false;
            showTable(ownListedPlayers);
        }
    }

    private void loadListedPlayers(PlayerDatabase playerDB){
        ownListedPlayers.clear();
        othersListedPlayers.clear();
        for(Player p : playerDB.getPlayerList()){
            if(p.getClub().equals(clientApp.getName())) ownListedPlayers.addPlayer(p);
            else othersListedPlayers.addPlayer(p);
        }
    }

    public void dynamicLoading(PlayerDatabase playerDB){
        loadListedPlayers(playerDB);
        if(buyMode) showTable(othersListedPlayers);
        else showTable(ownListedPlayers);
    }

    @FXML
    public void TransferAction(ActionEvent actionEvent) {
        Player selectedPlayer =TransferList_TableView.getSelectionModel().getSelectedItem();
        if(selectedPlayer != null){
            if(!buyMode){
                try {
                    clientApp.getSocketWrapper().write(new PlayerDTO("unlist", selectedPlayer));
                    Message_Text.setText(selectedPlayer.getName() + " successfully unlisted from the market");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    clientApp.getSocketWrapper().write(new PlayerDTO("buy", selectedPlayer));
                    Message_Text.setText("Successfully bought " + selectedPlayer.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showTable(PlayerDatabase playerDB){
        Count_Label.setText("Found: " + playerDB.size() + " players");
        if(!playerDB.isEmpty()) {
            data = FXCollections.observableArrayList(playerDB.getPlayerList());
            TransferList_TableView.setEditable(true);
            TransferList_TableView.setItems(data);
        }
        else {
            TransferList_TableView.setItems(FXCollections.observableArrayList());
        }
    }

    public void returnToHomePage(ActionEvent actionEvent) {
        try {
            clientApp.showHomePage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
