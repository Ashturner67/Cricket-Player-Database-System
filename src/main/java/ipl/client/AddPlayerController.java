package ipl.client;

import ipl.database.Players.Player;
import ipl.utility.InputHandler;
import ipl.utility.PlayerDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class AddPlayerController {

    private ClientApp clientApp;

    void setClientApp(ClientApp clientApp) {
        this.clientApp = clientApp;
    }

    @FXML
    private Text Status_Text;
    @FXML
    private TextField Name_Field;
    @FXML
    private TextField Country_Field;
    @FXML
    private ComboBox<String> Position_ComboBox;
    @FXML
    private TextField Age_Field;
    @FXML
    private TextField Height_Field;
    @FXML
    private TextField Number_Field;
    @FXML
    private TextField Salary_Field;

    public Text getStatus_Text() {
        return Status_Text;
    }

    @FXML
    public void initialize() {
        Position_ComboBox.getItems().addAll("Batsman", "Bowler", "Allrounder", "Wicketkeeper");
    }


    @FXML
    void returnToHomePage(ActionEvent event) {
        try {
            clientApp.showHomePage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    private void addPlayer(ActionEvent actionEvent) {
        String message;
        String name, country, position;
        Integer age, number, weeklySalary;
        Double height;
        name = Name_Field.getText();
        country = Country_Field.getText();
        position = InputHandler.capitalizeWord(Position_ComboBox.getValue());
        age = InputHandler.integerInput(Age_Field.getText());
        number = InputHandler.integerInput(Number_Field.getText());
        weeklySalary = InputHandler.integerInput(Salary_Field.getText());
        height = InputHandler.doubleInput(Height_Field.getText());
        boolean[] inputValidity = new boolean[7];
        inputValidity[2] = !position.isEmpty() && (position.equalsIgnoreCase("Batsman") || position.equalsIgnoreCase("Bowler") ||
                position.equalsIgnoreCase("Allrounder") || position.equalsIgnoreCase("Wicketkeeper"));
        inputValidity[3] = (age!=null && age > 0);
        inputValidity[5] = (number != null && number >= -1);
        inputValidity[6] = (weeklySalary != null && weeklySalary >= 0);
        inputValidity[4] = (height != null);
        inputValidity[0] = !name.isEmpty();
        inputValidity[1] = !country.isEmpty();
        boolean validity = true;
        StringBuilder errorMessage = new StringBuilder();
        for(int i = 0; i < 7; i++){
            if(!inputValidity[i]) {
                validity = false;
                if(!errorMessage.isEmpty()) errorMessage.append(", ");
                if(i == 0) errorMessage.append("name");
                else if(i == 1) errorMessage.append("country");
                else if(i == 2) errorMessage.append("position");
                else if(i == 3) errorMessage.append("age");
                else if(i == 5) errorMessage.append("jersey number");
                else if(i == 6) errorMessage.append("weekly salary");
                else errorMessage.append("height");
            }
        }
        if(!validity) {
            message = "Invalid " + errorMessage.toString();
            Status_Text.setText(message);
            return;
        }
        else{
            Player newPlayer = new Player(name, country, age, height, clientApp.getName(), position, number, weeklySalary);
            try {
                clientApp.getSocketWrapper().write(new PlayerDTO("add", newPlayer));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void ComboBox_Action(ActionEvent actionEvent){

    }
}
