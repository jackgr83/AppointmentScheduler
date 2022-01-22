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
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

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
    Integer id;
    Integer custApptStHour;
    Integer custApptEndHour;
    Integer enteredStHour;
    Integer enteredEndHour;

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

//        // Check that appointment start < end
        // convert start to 24 hour
        SimpleDateFormat twelveHourFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
        twelveHourFormat.setTimeZone(TimeZone.getDefault());
        Date twelveHourDateTimeStart = twelveHourFormat.parse(start);
        Date twelveHourDateTimeEnd = twelveHourFormat.parse(end);
        SimpleDateFormat twentyFourFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        twentyFourFormat.setTimeZone(TimeZone.getDefault());
        Integer twentyFourStart = Integer.parseInt(twentyFourFormat.format(twelveHourDateTimeStart).split("\\s+")[1].substring(0,5).replaceAll(":",""));
        Integer twentyFourEnd = Integer.parseInt(twentyFourFormat.format(twelveHourDateTimeEnd).split("\\s+")[1].substring(0,5).replaceAll(":",""));
        if (twentyFourStart > twentyFourEnd) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText("Appointment start time must occur before Appointment end time");
            error.showAndWait();
            return;
        }


        // Check for overlapping appointment times for customer
        ArrayList<String[]> times = AppointmentDatabase.getCustomerAppointmentTimes(custId, dt);
        if (times.size() > 0){

            for (int i=0;i<times.size();i++) {
                System.out.println("Customer Appt TIME: " + times.get(i)[0] + " TO " + times.get(i)[1]);
                if (Integer.parseInt(times.get(i)[0].split("\\s+")[1].substring(0, 1)) == 0) {
                    this.custApptStHour = Integer.parseInt(times.get(i)[0].split("\\s+")[1].substring(1, 5).replaceAll(":", ""));
                } else {
                    this.custApptStHour = Integer.parseInt(times.get(i)[0].split("\\s+")[1].substring(0, 2).replaceAll(":", ""));
                }
                if (Integer.parseInt(times.get(i)[1].split("\\s+")[1].substring(0, 1)) == 0) {
                    this.custApptEndHour = Integer.parseInt(times.get(i)[1].split("\\s+")[1].substring(1, 5).replaceAll(":", ""));
                } else {
                    this.custApptEndHour = Integer.parseInt(times.get(i)[1].split("\\s+")[1].substring(0, 5).replaceAll(":", ""));
                }

                if (Integer.parseInt(AppointmentDatabase.convertToUtc(start).split("\\s")[1].substring(0,1)) == 0) {
                    this.enteredStHour = Integer.parseInt(AppointmentDatabase.convertToUtc(start).split("\\s")[1].substring(1, 5).replaceAll(":", ""));
                } else {
                    this.enteredStHour = Integer.parseInt(AppointmentDatabase.convertToUtc(start).split("\\s")[1].substring(0, 5).replaceAll(":", ""));
                }
                if (Integer.parseInt(AppointmentDatabase.convertToUtc(end).split("\\s")[1].substring(0,1)) == 0) {
                    this.enteredEndHour = Integer.parseInt(AppointmentDatabase.convertToUtc(start).split("\\s")[1].substring(1, 5).replaceAll(":", ""));
                } else {
                    this.enteredEndHour = Integer.parseInt(AppointmentDatabase.convertToUtc(start).split("\\s")[1].substring(0, 5).replaceAll(":", ""));
                }

                if ((this.enteredStHour >= this.custApptStHour) && (this.custApptEndHour >= this.enteredStHour)) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("Appointment times are overlapping");
                    error.showAndWait();
                    return;
                }

                if ((this.enteredStHour <= this.custApptStHour) && (this.enteredEndHour > this.custApptStHour)) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("Appointment times are overlapping");
                    error.showAndWait();
                    return;
                }

                if ((this.enteredStHour <= this.custApptStHour) && (this.enteredEndHour > this.custApptEndHour)) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("Appointment times are overlapping");
                    error.showAndWait();
                    return;
                }


            }
        } else {
            System.out.println("No other appts for this customer");
        }


        // Check business hours
        String[] businessHoursUtc = {"13","14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "00", "01", "02"};
        String utcStart = AppointmentDatabase.convertToUtc(start);
        String utcEnd = AppointmentDatabase.convertToUtc(end);
        if (!Arrays.asList(businessHoursUtc).contains(utcStart.split("\\s+")[1].substring(0, 2)) ||
                !Arrays.asList(businessHoursUtc).contains(utcEnd.split("\\s+")[1].substring(0, 2))) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText("Appointment start/end time is outside business hours (8:00AM-10:00PM EST)");
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
        amSt.setSelected(true);
        amEnd.setSelected(true);
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
