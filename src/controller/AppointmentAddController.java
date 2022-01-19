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

    public void handleSave() throws IOException, SQLException {
        // get all fields, check for errors, add appt to database, confirm, re-populate tables.
        String ttl = title.getText();
        String dsc = desc.getText();
        String lc = loc.getText();
        String cnt = cont.getValue().toString();
        String tp = type.getValue().toString();
        LocalDate dt = date.getValue();
        LocalDateTime starttime = null;
        LocalDateTime endtime = null;



        int custId = CustomerDatabase.getCustomerId(cust.getValue().toString());
        int userId = Login.getUser().getUserId();
        int contId = AppointmentDatabase.getContactId(cont.getValue().toString());

        if (ttl.isBlank() || dsc.isBlank() || lc.isBlank() || cnt.isBlank() || tp.isBlank()) {
            error.setTitle("Error");
            error.setHeaderText("Please fill in all fields");
            error.showAndWait();
            return;
        }

        try {
            starttime = LocalDateTime.of(dt, LocalTime.parse(sttime.getText(), DateTimeFormatter.ofPattern("HH:MM")));
            endtime = LocalDateTime.of(dt, LocalTime.parse(etime.getText(), DateTimeFormatter.ofPattern("HH:MM")));
        } catch (DateTimeParseException e) {
            error.setTitle("Error");
            error.setHeaderText("Invalid start or end time. Please enter in HH:MM format, including leading 0s");
            error.showAndWait();
            return;
        }
        ZonedDateTime stdatetime = ZonedDateTime.of(starttime, ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime enddatetime = ZonedDateTime.of(endtime, ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC);

        Boolean successful = AppointmentDatabase.addAppointment(id, ttl, dsc, lc, tp, stdatetime, enddatetime, custId, userId, contId);

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
