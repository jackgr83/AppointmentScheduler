package controller;

import javafx.collections.FXCollections;
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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
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
    @FXML
    private RadioButton amSt;
    @FXML
    private RadioButton pmSt;
    @FXML
    private RadioButton amEnd;
    @FXML
    private RadioButton pmEnd;

    Alert error = new Alert(Alert.AlertType.ERROR);
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    Alert warn = new Alert(Alert.AlertType.WARNING);

    Appointment appointment;

    public AppointmentEditController(Appointment appointment) {
        this.appointment = appointment;
    }

    public void handleSave() throws Exception {
        Integer id = Integer.parseInt(IdField.getText());
        String ttl = title.getText();
        String dsc = desc.getText();
        String lc = loc.getText();
        String cnt = cont.getValue().toString();
        String tp = type.getValue().toString();
        String dt = date.getValue().toString();
        String starttime = sttime.getText() + ":00";
        String endtime = etime.getText() + ":00";
        String start = "";
        String end = "";
        if (amSt.isSelected()) {
            start = dt + " " + starttime + " " + amSt.getText();
        } else {
            start = dt + " " + starttime + " " + pmSt.getText();
        }
        if (amEnd.isSelected()) {
            end = dt + " " + endtime + " " + amEnd.getText();
        } else {
            end = dt + " " + endtime + " " + pmEnd.getText();
        }
        int custId = CustomerDatabase.getCustomerId(cust.getValue().toString());
        int userId = Login.getUser().getUserId();
        int contId = AppointmentDatabase.getContactId(cont.getValue().toString());

        if (ttl.isBlank() || dsc.isBlank() || lc.isBlank() || cnt.isBlank() || tp.isBlank()) {
            error.setTitle("Error");
            error.setHeaderText("Please fill in all fields");
            error.showAndWait();
            return;
        }
        // Check business hours
//        String[] businessHoursUtc = {"13","14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "00", "01", "02"};
//        String utcStart = AppointmentDatabase.convertToUtc(start);
//        String utcEnd = AppointmentDatabase.convertToUtc(end);
//        if (!Arrays.asList(businessHoursUtc).contains(utcStart.split("\\s+")[1].substring(0, 2)) ||
//                !Arrays.asList(businessHoursUtc).contains(utcEnd.split("\\s+")[1].substring(0, 2))) {
//            Alert error = new Alert(Alert.AlertType.ERROR);
//            error.setTitle("Error");
//            error.setHeaderText("Appointment start/end time is outside business hours (8:00AM-10:00PM EST)");
//            error.showAndWait();
//            return;
//        }

        Boolean successful = AppointmentDatabase.updateAppointment(id, ttl, dsc, lc, tp, start, end, custId, userId, contId);



        if (successful) {
            confirm.setTitle("Success!");
            confirm.setHeaderText("Appointment updated");
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
            warn.setHeaderText("Failed to update appointment");
            warn.showAndWait();
            return;
        }
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

    public void handleAmStRadio() {
        amSt.setSelected(true);
        pmSt.setSelected(false);
    }

    public void handleAmEndRadio() {
        amEnd.setSelected(true);
        pmEnd.setSelected(false);
    }

    public void handlePmStRadio() {
        pmSt.setSelected(true);
        amSt.setSelected(false);
    }

    public void handlePmEndRadio() {
        amEnd.setSelected(false);
        pmEnd.setSelected(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (appointment.getStart().split("\\s+")[2].contains("AM")) {
            amSt.setSelected(true);
        } else {
            pmSt.setSelected(true);
        }
        if (appointment.getEnd().split("\\s+")[2].contains("AM")) {
            amEnd.setSelected(true);
        } else {
            pmEnd.setSelected(true);
        }
        IdField.setText(String.valueOf(appointment.getId()));
        title.setText(appointment.getTitle());
        desc.setText(appointment.getDesc());
        loc.setText(appointment.getLoc());
        String s = appointment.getStart().split("\\s+")[1];
        sttime.setText(s.substring(0, s.length() - 3));
        String en = appointment.getEnd().split("\\s+")[1];
        etime.setText(en.substring(0, en.length() - 3));
        type.setItems(FXCollections.observableArrayList("Introduction", "Status Update", "One on One", "Review"));
        type.getSelectionModel().select(appointment.getType());
        date.setValue(LocalDate.parse(appointment.getStart(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a")));
        System.out.println(date.getValue().toString());
        try {
            cust.setItems(CustomerDatabase.getCustomerNames());
            cust.getSelectionModel().select(CustomerDatabase.getCustomerName(appointment.getCustId()));
            cont.setItems(AppointmentDatabase.getContactNames());
            cont.getSelectionModel().select(appointment.getCont());
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

}
