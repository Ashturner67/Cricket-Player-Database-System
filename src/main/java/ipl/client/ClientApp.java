package ipl.client;

import ipl.database.Clubs.Club;
import ipl.database.Players.PlayerDatabase;
import ipl.utility.SocketWrapper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class ClientApp extends Application {

    private Stage stage;
    private SocketWrapper clientWrapper;
    private Club club;
    private PlayerDatabase transferDatabase;

    private HomePageController homeController;
    private RegisterController registerController;
    private AddPlayerController addPlayerController;
    private ChangePassController changePassController;
    private TransferMarketController transferMarketController;
    private boolean isOnTransferMarket = false;
    private boolean isOnHomePage = false;
    public SocketWrapper getSocketWrapper() {
        return clientWrapper;
    }

    public boolean isOnHomePage() {
        return isOnHomePage;
    }

    public void setOnHomePage(boolean isOnHomePage) {
        this.isOnHomePage = isOnHomePage;
    }

    public boolean isOnTransferMarket() {
        return isOnTransferMarket;
    }

    public void setOnTransferMarket(boolean isOnTransferMarket) {
        this.isOnTransferMarket = isOnTransferMarket;
    }

    public HomePageController getHomeController() {
        return homeController;
    }

    public RegisterController getRegisterController() {
        return registerController;
    }

    public AddPlayerController getAddPlayerController(){
        return addPlayerController;
    }

    public TransferMarketController getTransferMarketController(){
        return transferMarketController;
    }

    public void setChangePassController(ChangePassController changePassController) {
        this.changePassController = changePassController;
    }

    public ChangePassController getChangePassController(){
        return changePassController;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Club getClub() {
        return club;
    }

    public PlayerDatabase getTransferDatabase(){
        return transferDatabase;
    }

    public void setTransferDatabase(PlayerDatabase playerDatabase){
        transferDatabase = playerDatabase;
    }

    public String getName(){
        return club.getName();
    }

    public PlayerDatabase getPlayerDatabase(){
        return club.getPlayerDatabase();  }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        connectToServer();
        showLoginPage();

        primaryStage.setOnCloseRequest(event -> {
            try {
                if(this.club != null) getSocketWrapper().write("logout," + club.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Closing application...");
            Platform.exit();
            System.exit(0);
        });
    }

    private void connectToServer() throws IOException {
        String serverAddress = "127.0.0.1";
        int serverPort = 44456;
        clientWrapper = new SocketWrapper(serverAddress, serverPort);
        new ReadThreadClient(this);
    }

    public void showLoginPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        LoginPageController loginController = loader.getController();
        loginController.setClientApp(this);
        stage.setTitle("Login");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        setOnHomePage(false);
    }

    public void showRegisterPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
        Parent root = loader.load();
        registerController = loader.getController();
        registerController.setClientApp(this);
        getRegisterController().setStage(stage);
        stage.setTitle("Register New Club");
        stage.setScene(new Scene(root));
        stage.show();
        setOnHomePage(false);
    }

    public void showHomePage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
        Parent root = loader.load();
        homeController = loader.getController();
        homeController.setClientApp(this);
        homeController.load();
        homeController.initialize(club.getName());
        stage.setTitle(club.getName());
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        setOnTransferMarket(false);
        setOnHomePage(true);
    }

    public void showAddPlayerMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addPlayer.fxml"));
        Parent root = loader.load();
        addPlayerController = loader.getController();
        addPlayerController.setClientApp(this);
        addPlayerController.getStatus_Text().setText("");
        stage.setTitle("IPL Player Database");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        setOnHomePage(false);
    }

    public void showTransferMarket() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("transferMarket.fxml"));
        Parent root = loader.load();
        transferMarketController = loader.getController();
        transferMarketController.setClientApp(this);
        transferMarketController.initialize(club.getName());
        transferMarketController.load();
        stage.setTitle("IPL Player Database");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        setOnTransferMarket(true);
        setOnHomePage(false);
    }

    public void showIncorrectAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Incorrect Credentials");
        alert.setHeaderText("Incorrect Credentials");
        alert.setContentText("The username and password you provided is not correct.");
        alert.showAndWait();
    }

    public void showExistsAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Client Exists");
        alert.setHeaderText("Client already logged in");
        alert.setContentText("You cannot login again as a session already exists.");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }

}