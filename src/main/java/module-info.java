module ipl.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;

    exports ipl.client;
    exports ipl.server;
    exports ipl.database.Clubs;
    exports ipl.database.Players;
    exports ipl.database.Utils;
    exports ipl.database.SearchOperations;
    exports ipl.database.FileHandling;

    opens ipl.client to javafx.fxml;
    opens ipl.server to javafx.fxml;
    opens ipl.database.Clubs to javafx.fxml;
    opens ipl.database.Players to javafx.fxml;
    opens ipl.database.Utils to javafx.fxml;
    opens ipl.database.SearchOperations to javafx.fxml;
    opens ipl.database.FileHandling to javafx.fxml;
    exports ipl.utility;
    opens ipl.utility to javafx.fxml;
}