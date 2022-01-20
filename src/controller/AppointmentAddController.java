package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.AppointmentDatabase;
import model.CustomerDatabase;
import model.Login;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class AppointmentAddController implements Initializable {

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
    Integer id;

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

    public void handleSave() throws Exception {
        // get all fields, check for errors, add appt to database, confirm, re-populate tables.
        String ttl = title.getText();
        String dsc = desc.getText();
        String lc = loc.getText();
        String cnt = cont.getValue().toString();
        String tp = type.getValue().toString();
        String dt = date.getValue().toString();
        String starttime = sttime.getText() + ":00";
        String endtime = etime.getText() + ":00";
        String start = dt + " " + starttime;
        String end = dt + " " + endtime;


        int custId = CustomerDatabase.getCustomerId(cust.getValue().toString());
        int userId = Login.getUser().getUserId();
        int contId = AppointmentDatabase.getContactId(cont.getValue().toString());

        if (ttl.isBlank() || dsc.isBlank() || lc.isBlank() || cnt.isBlank() || tp.isBlank()) {
            error.setTitle("Error");
            error.setHeaderText("Please fill in all fields");
            error.showAndWait();
            return;
        }

        Boolean successful = AppointmentDatabase.addAppointment(id, ttl, dsc, lc, tp, start, end, custId, userId, contId);


        if (successful) {
            confirm.setTitle("Success!");
            confirm.setHeaderText("Appointment added");
            confirm.showAndWait();
            Stage stage = (Stage) save.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Appointment.fxml"));
            AppointmentController controller = new AppointmentController();
            loader.setController(controller);
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } else {
            warn.setTitle("Failure");
            warn.setHeaderText("Failed to add appointment");
            warn.showAndWait();
            return;
        }

    }

    private void generateId() throws SQLException {
        id = (AppointmentDatabase.getNumAppointments() + 1);
        IdField.setText(id.toString());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        type.setItems(FXCollections.observableArrayList("Introduction", "Status Update", "One on One", "Review"));
        try {
            cust.setItems(CustomerDatabase.getCustomerNames());
            cont.setItems(AppointmentDatabase.getContactNames());
            generateId();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
