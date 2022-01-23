package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utility.Database;

/**
 * Javadocs located in /Javadoc
 */
public class main extends Application {

    /**
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This function starts the application at the Login page and initializes a connection to the database
     */
    public void start(Stage stage) throws Exception {
        connectDatabase();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Login.fxml"));
        controller.LoginController controller = new controller.LoginController();
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    void connectDatabase() throws Exception {
        Database.connect();
    }


}
