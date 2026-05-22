package ipl.client;

import ipl.utility.LoginDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Optional;

public class LoginPageController {
    
    private ClientApp clientApp;

    void setClientApp(ClientApp clientApp) {
        this.clientApp = clientApp;
    }

    @FXML
    private TextField userText;

    @FXML
    private PasswordField passwordText;
    @FXML
    private Text Register_Button;

    @FXML
    private void registerColorChange() {
        Register_Button.setFill(Color.BLUE);
    }

    @FXML
    private void registerColorReset() {
        Register_Button.setFill(Color.BLACK);
    }

    @FXML
    void loginAction(ActionEvent event) {
        String userName = userText.getText().trim();
        String password = passwordText.getText().trim();
        LoginDTO loginDTO = new LoginDTO(userName, password);
        //1234 is the password
        try {
            clientApp.getSocketWrapper().write(loginDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void resetAction(ActionEvent event) {
        userText.setText(null);
        passwordText.setText(null);
    }

    @FXML
    private void registerAction() {
        try {
            clientApp.showRegisterPage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}