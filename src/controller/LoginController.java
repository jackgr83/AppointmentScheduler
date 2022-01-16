package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Login;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Label PasswordLabel;
    @FXML
    private Label LoginHeaderLabel;
    @FXML
    private Label UsernameLabel;
    @FXML
    private Label ErrorLabel;
    @FXML
    private Label LocationInfoLabel;
    @FXML
    private Button LoginButton;
    @FXML
    private TextField UsernameField;
    @FXML
    private TextField PasswordField;

    String language = "English";
    Alert error = new Alert(Alert.AlertType.ERROR);

    void setLocation() {
        if (language == "French"){
            LocationInfoLabel.setText("Emplacement: " + ZoneId.systemDefault().toString());
        } else {
            LocationInfoLabel.setText("Location: " + ZoneId.systemDefault().toString());
        }
    }

    void translateFrench() {
        LoginHeaderLabel.setText("Connexion");
        UsernameLabel.setText("Nom d'utilisateur");
        PasswordLabel.setText("Mot de passe");
        LoginButton.setText("ouvrir une session");

    }

    public void handleLoginButton() throws SQLException, IOException {
        String username = UsernameField.getText();
        String password = PasswordField.getText();

        boolean valid = Login.tryLogin(username, password);

        if (valid) {
            Stage stage = (Stage) LoginButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Main.fxml"));
            MainController controller = new MainController();
            loader.setController(controller);
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } else if (language == "French"){
            error.setTitle("Erreur");
            error.setHeaderText("Nom d'utilisateur ou mot de passe invalide");
            error.showAndWait();
            return;
        } else {
            error.setTitle("Error");
            error.setHeaderText("Invalid username or password");
            error.showAndWait();
            return;
        }
    }


    /**
     * Initialize the Login Screen
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Locale locale = Locale.getDefault();
        if (locale.getDisplayLanguage() == "French") {
            language = "French";
            translateFrench();
        }
        setLocation();

    }

}
