package ipl.client;

import ipl.utility.ChangePassDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class ChangePassController {

    private ClientApp clientApp;

    @FXML
    private Text Message_Text;

    public Text getMessage_Text(){
        return  Message_Text;
    }

    @FXML
    private TextField CurrentPass_Field;
    @FXML
    private TextField NewPass_Field;
    @FXML
    private TextField RetypePass_Field;
    @FXML
    private Label TeamName_Label;
    @FXML
    private Button Close_Button;

    void setClientApp(ClientApp clientApp) {
        this.clientApp = clientApp;
        TeamName_Label.setText(clientApp.getName());
    }

    @FXML
    private void changePassAction(ActionEvent actionEvent) {
        String currentPass = CurrentPass_Field.getText().trim();
        String newPass = NewPass_Field.getText().trim();
        String retypePass = RetypePass_Field.getText().trim();
        if(!newPass.equals(retypePass)) Message_Text.setText("New password does not match");
        else if (currentPass.isEmpty()) Message_Text.setText("Enter Current Password");
        else if(newPass.isEmpty()) Message_Text.setText("Enter New Password");
        else {
            try {
                clientApp.getSocketWrapper().write(new ChangePassDTO(clientApp.getName(), currentPass, newPass));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) Close_Button.getScene().getWindow();
        stage.close();
    }

}
