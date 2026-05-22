package ipl.client;

import ipl.database.Players.Player;
import ipl.database.Players.PlayerDatabase;
import ipl.database.SearchOperations.ClubSearch;
import ipl.database.SearchOperations.PlayerSearch;
import ipl.utility.InputHandler;
import ipl.utility.PlayerDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Map;

public class HomePageController {

    private ClientApp clientApp;
    private boolean initialized = true;
    private int selectedOption;
    private ObservableList<Player> data;

    void setClientApp(ClientApp clientApp) {
        this.clientApp = clientApp;
    }

    public ObservableList<Player> getData() {
        return data;
    }

    public void setData(ObservableList<Player> data) {
        this.data = data;
    }

    @FXML
    private Label name;
    @FXML
    private TableView<Player> PlayerList_TableView;
    @FXML
    private ComboBox<String> PlayerSearch_ComboBox;
    @FXML
    private ComboBox<String> Position_ComboBox;
    @FXML
    private ListView<String> CPWC_ListView;
    @FXML
    private TextField InputField2;
    @FXML
    private TextField InputField1;
    @FXML
    private Text SearchBox_Text;
    @FXML
    private Label Query_Label;
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
    private Text SellMessage_Text;
    @FXML
    private Text SelectedPlayer_Text;

    @FXML
    public void initialize(String s){
        name.setText(s);
        Position_ComboBox.setVisible(false);
        PlayerSearch_ComboBox.getItems().addAll("Name", "Country", "Position", "Salary Range", "Maximum Salary", "Maximum Age", "Maximum Height");
        Position_ComboBox.getItems().addAll("Batsman", "Bowler", "Allrounder", "Wicketkeeper");
        PlayerSearch_ComboBox.setValue("Name");
        selectedOption = 1;
        InputField1.setPromptText("Enter name");
        InputField1.setVisible(true);
        InputField2.setVisible(false);
        SelectedPlayer_Text.setText("None is selected");
        SellMessage_Text.setText("");
        PlayerList_TableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            SellMessage_Text.setText("");
            if (newValue != null) {
                Player selectedPlayer = PlayerList_TableView.getSelectionModel().getSelectedItem();
                SelectedPlayer_Text.setText("Selected Player: " + newValue.getName());
                if(clientApp.getTransferDatabase().playerExists(selectedPlayer.getName())) SellMessage_Text.setText("This player is listed for transfer");
                else SellMessage_Text.setText("This player is not listed for transfer");
            } else {
                SelectedPlayer_Text.setText("None is selected");
            }
        });
        PlayerSearch_ComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Position".equals(newValue)) {
                Position_ComboBox.setVisible(true);
                Position_ComboBox.setValue("Batsman");
            } else {
                Position_ComboBox.setVisible(false);
                Position_ComboBox.getSelectionModel().clearSelection();
            }
        });
    }

    public void load() {
        if (initialized) {
            initializeColumns();
            initialized = false;
        }
        data = FXCollections.observableArrayList(clientApp.getPlayerDatabase().getPlayerList());
        Query_Label.setText("Total: " + data.size() + " players");
        PlayerList_TableView.setEditable(true);
        PlayerList_TableView.setItems(data);
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
        for (TableColumn<?, ?> column : PlayerList_TableView.getColumns()) {
            column.setStyle("-fx-alignment: center;");
        }
        Jersey_Col.setCellValueFactory(cellData -> {
            Player player = cellData.getValue();
            if(player == null) return new SimpleStringProperty("");
            return new SimpleStringProperty(player.getNumber() == -1 ? "Not Available" : String.valueOf(player.getNumber()));
        });
    }

    @FXML
    public void ComboBox_Action(ActionEvent actionEvent) {
        if(PlayerSearch_ComboBox.getValue().equals("Name")) {
            selectedOption = 1;
            InputField1.clear();
            InputField1.setPromptText("Enter name");
        }
        else if(PlayerSearch_ComboBox.getValue().equals("Country")) {
            selectedOption = 2;
            InputField1.clear();
            InputField1.setPromptText("Enter country name");
        }
        else if(PlayerSearch_ComboBox.getValue().equals("Position")) {
            selectedOption = 3;
        }
        else if(PlayerSearch_ComboBox.getValue().equals("Salary Range")){
            selectedOption = 4;
            InputField1.clear();
            InputField2.clear();
            InputField1.setPromptText("Enter min salary");
            InputField2.setPromptText("Enter max salary");
        } else if (PlayerSearch_ComboBox.getValue().equals("Maximum Salary")) selectedOption = 5;
        else if(PlayerSearch_ComboBox.getValue().equals("Maximum Age")) selectedOption = 6;
        else selectedOption = 7;
        InputField2.setVisible(selectedOption == 4);
        InputField1.setVisible(selectedOption <= 4 && selectedOption != 3);
    }

    @FXML
    private void Search_Util(ActionEvent actionEvent) {
        SelectedPlayer_Text.setText("None is selected");
        SellMessage_Text.setText("");
        CPWC_ListView.setVisible(false);
        PlayerList_TableView.setVisible(true);
        PlayerDatabase resultDB = new PlayerDatabase();
        if(selectedOption == 1){
            resultDB.addPlayer(PlayerSearch.searchByName(InputField1.getText(), clientApp.getPlayerDatabase()));
        }
        else if(selectedOption == 2) {
            resultDB = PlayerSearch.searchByCountry(InputField1.getText(), clientApp.getPlayerDatabase());
        }
        else if(selectedOption == 3){
            if(Position_ComboBox.getValue() == null){
                SearchBox_Text.setVisible(true);
                SearchBox_Text.setText("Select a position");
            }
            else{
                SearchBox_Text.setVisible(false);
                resultDB = PlayerSearch.searchByPosition(Position_ComboBox.getValue(), clientApp.getPlayerDatabase());
            }
        }
        else if(selectedOption == 4){
            if(InputHandler.integerInput(InputField1.getText()) == null
                    || InputHandler.integerInput(InputField2.getText()) == null || InputHandler.integerInput(InputField1.getText()) < 0
                    || InputHandler.integerInput(InputField2.getText()) < 0) {
                SearchBox_Text.setVisible(true);
                SearchBox_Text.setText("Enter valid positive number");
            }
            else {
                SearchBox_Text.setVisible(false);
                resultDB = PlayerSearch.searchBySalaryRange(InputHandler.integerInput(InputField1.getText()), InputHandler.integerInput(InputField2.getText()), clientApp.getPlayerDatabase());
            }
        }
        else if(selectedOption == 5) resultDB = ClubSearch.MaxSalaryPlayers(clientApp.getName(), clientApp.getPlayerDatabase());
        else if(selectedOption == 6) resultDB = ClubSearch.MaxAgePlayers(clientApp.getName(), clientApp.getPlayerDatabase());
        else resultDB = ClubSearch.MaxHeightPlayers(clientApp.getName(), clientApp.getPlayerDatabase());
        if(resultDB != null) showTable(resultDB);
        else Query_Label.setText("Found: " + 0 + " players");
    }

    private void showTable(PlayerDatabase playerDB){
        Query_Label.setText("Found: " + playerDB.size() + " players");
        if(!playerDB.isEmpty()) {
            data = FXCollections.observableArrayList(playerDB.getPlayerList());
            PlayerList_TableView.setEditable(true);
            PlayerList_TableView.setItems(data);
        }
        else {
            PlayerList_TableView.setItems(FXCollections.observableArrayList());
        }
    }

    public void showTable(ObservableList<Player> data){
        Query_Label.setText("Total: " + data.size() + " players");
        PlayerList_TableView.setEditable(true);
        PlayerList_TableView.setItems(data);
    }

    @FXML
    private void CWPC_Util(ActionEvent actionEvent) {
        SelectedPlayer_Text.setText("None is selected");
        SellMessage_Text.setText("");
        PlayerList_TableView.setVisible(false);
        CPWC_ListView.setVisible(true);
        Query_Label.setText("Country-wise Player Count");
        Map<String, Integer> countMap = PlayerSearch.countryWisePlayerCount(clientApp.getPlayerDatabase());
        CPWC_ListView.getItems().clear();
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            String item = entry.getKey() + ": " + entry.getValue();
            CPWC_ListView.getItems().add(item);
        }
    }

    @FXML
    private void showAllPlayers(ActionEvent actionEvent) {
        CPWC_ListView.setVisible(false);
        PlayerList_TableView.setVisible(true);
        data = FXCollections.observableArrayList(clientApp.getPlayerDatabase().getPlayerList());
        Query_Label.setText("Total: " + data.size() + " players");
        PlayerList_TableView.setEditable(true);
        PlayerList_TableView.setItems(data);
    }

    @FXML
    private void showTotalSalary() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Total Salary");
        alert.setHeaderText("Total Salary:");
        alert.setContentText("The total salary of " + clientApp.getName() + " is: " + clientApp.getClub().getTotalSalary());
        alert.showAndWait();
    }

    public void addPlayerAction(ActionEvent actionEvent) {
        try {
            clientApp.showAddPlayerMenu();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void logout(ActionEvent actionEvent) {
        try {
            clientApp.getSocketWrapper().write("logout," + clientApp.getClub().getName());
            clientApp.showLoginPage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Stage changePassWindow;

    @FXML
    public void showChangePass(ActionEvent actionEvent) {
        if (changePassWindow == null || !changePassWindow.isShowing()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("changePass.fxml"));
                Parent root = loader.load();
                ChangePassController changePassController = loader.getController();
                changePassController.setClientApp(clientApp);
                clientApp.setChangePassController(changePassController);
                changePassWindow = new Stage();
                changePassWindow.setTitle("Change Password");
                changePassWindow.setScene(new Scene(root));
                changePassWindow.show();

                changePassWindow.setOnCloseRequest((WindowEvent event) -> {
                    changePassWindow = null;
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void sellAction(ActionEvent actionEvent) {
        Player selectedPlayer = PlayerList_TableView.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null && PlayerList_TableView.isVisible()) {
            if (clientApp.getTransferDatabase().playerExists(selectedPlayer.getName())) {
                SellMessage_Text.setText("This player is already listed for transfer");
                } else {
                    try {
                        clientApp.getSocketWrapper().write(new PlayerDTO("sell", selectedPlayer));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    SellMessage_Text.setText("Player successfully listed for transfer");
                }
        } else {
            SellMessage_Text.setText("Please select a player from the table first");
        }
    }

    @FXML
    public void transferMarketOpen(ActionEvent actionEvent) {
        try {
            clientApp.showTransferMarket();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
