package ipl.client;

import ipl.utility.RegisterDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    private ClientApp clientApp;
    private Stage stage;

    void setClientApp(ClientApp clientApp) {
        this.clientApp = clientApp;
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    @FXML
    private TextField InputField1;
    @FXML
    private TextField InputField2;
    @FXML
    private TextField InputField3;
    @FXML
    private Text Message_Text;

    public Text getMessage_Text() {
        return Message_Text;
    }

    @FXML
    public void registerClub(ActionEvent actionEvent) {
        String clubName = InputField1.getText().trim();
        String newPass = InputField2.getText().trim();
        String retypePass = InputField3.getText().trim();
        if(!newPass.equals(retypePass)) Message_Text.setText("New password does not match");
        else if (clubName.isEmpty()) Message_Text.setText("Enter Club Name");
        else if(newPass.isEmpty()) Message_Text.setText("Enter New Password");
        else {
            try {
                clientApp.getSocketWrapper().write(new RegisterDTO(clubName, newPass));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeRegisterPage() {
        stage.close();
        try {
            clientApp.showLoginPage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void returnToLoginPage(ActionEvent event) {
        try {
            clientApp.showLoginPage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
