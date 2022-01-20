package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AppointmentEditController implements Initializable {

    @FXML
    private TextField IdField;
    @FXML
    private TextField title;
    @FXML
    private TextField desc;
    @FXML
    private TextField loc;
    @FXML
    private ComboBox cust;
    @FXML
    private DatePicker date;
    @FXML
    private ComboBox cont;
    @FXML
    private TextField sttime;
    @FXML
    private TextField etime;
    @FXML
    private ComboBox type;
    @FXML
    private Button save;
    @FXML
    private Button cancel;

    Alert error = new Alert(Alert.AlertType.ERROR);
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    Alert warn = new Alert(Alert.AlertType.WARNING);

    Appointment appointment;

    public AppointmentEditController(Appointment appointment) {
        this.appointment = appointment;
    }

    public void handleSave() throws SQLException, IOException {

    }

    public void handleCancel() throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Appointment.fxml"));
        AppointmentController controller = new AppointmentController();
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
