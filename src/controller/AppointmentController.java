package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.AppointmentDatabase;
import model.Customer;
import model.CustomerDatabase;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {

    @FXML
    private Tab MonthlyTab;
    @FXML
    private Tab WeeklyTab;
    @FXML
    private TableView<Appointment> MonthlyTable;
    @FXML
    private TableColumn<Appointment, Integer> monthlyId;
    @FXML
    private TableColumn<Appointment, String> monthlyTitle;
    @FXML
    private TableColumn<Appointment, String> monthlyDesc;
    @FXML
    private TableColumn<Appointment, String> monthlyLoc;
    @FXML
    private TableColumn<Appointment, String> monthlyCont;
    @FXML
    private TableColumn<Appointment, String> monthlyType;
    @FXML
    private TableColumn<Appointment, String> monthlySt;
    @FXML
    private TableColumn<Appointment, String> monthlyEnd;
    @FXML
    private TableColumn<Appointment, Integer> monthlyCustId;
    @FXML
    private TableColumn<Appointment, Integer> monthlyCreatorId;
    @FXML
    private TableView<Appointment> WeeklyTable;
    @FXML
    private TableColumn<Appointment, Integer> weeklyId;
    @FXML
    private TableColumn<Appointment, String> weeklyTitle;
    @FXML
    private TableColumn<Appointment, String> weeklyDesc;
    @FXML
    private TableColumn<Appointment, String> weeklyLoc;
    @FXML
    private TableColumn<Appointment, String> weeklyCont;
    @FXML
    private TableColumn<Appointment, String> weeklyType;
    @FXML
    private TableColumn<Appointment, String> weeklySt;
    @FXML
    private TableColumn<Appointment, String> weeklyEnd;
    @FXML
    private TableColumn<Appointment, Integer> weeklyCustId;
    @FXML
    private TableColumn<Appointment, Integer> weeklyCreatorId;
    @FXML
    private Button Create;
    @FXML
    private Button Edit;
    @FXML
    private Button Delete;
    @FXML
    private Button Back;

    Alert error = new Alert(Alert.AlertType.ERROR);
    Alert warn = new Alert(Alert.AlertType.WARNING);

    /**
     * This function takes user back to the main screen when the "Back" button is pressed
     */
    public void handleBackButton() throws IOException {
        Stage stage = (Stage) Back.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Main.fxml"));
        MainController controller = new MainController();
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This function confirms with the user before deleting an appointment
     */
    public void handleDeleteButton() throws Exception {
        Appointment selectedAppt;
        if (MonthlyTab.isSelected()) {
            selectedAppt = MonthlyTable.getSelectionModel().getSelectedItem();
        } else {
            selectedAppt = WeeklyTable.getSelectionModel().getSelectedItem();
        }
        if (selectedAppt == null) {
            error.setTitle("Error");
            error.setHeaderText("No appointment selected");
            error.showAndWait();
            return;
        }
        String message = "Are you sure you want to delete Appointment: " + selectedAppt.getId();
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Warning");
        Optional<ButtonType> clicked = confirm.showAndWait();

        if (clicked.get() == ButtonType.YES) {
            Boolean deletedAppt = AppointmentDatabase.deleteAppointment(selectedAppt.getId());

            if (deletedAppt) {
                Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
                conf.setTitle("Success");
                conf.setHeaderText("Appointment deleted");
                conf.showAndWait();
            } else {
                warn.setTitle("Failure");
                warn.setHeaderText("Failed to delete appointment");
                warn.showAndWait();
            }
            try {
                populateAppointments();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This function takes the user to the edit appointment screen
     */
    public void handleEditButton() throws IOException {

        if (MonthlyTab.isSelected()) {
            Appointment selectedAppt = MonthlyTable.getSelectionModel().getSelectedItem();
            Stage stage = (Stage) Edit.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AppointmentEdit.fxml"));
            AppointmentEditController controller = new AppointmentEditController(selectedAppt);
            loader.setController(controller);
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } else {
            Appointment selectedAppt = WeeklyTable.getSelectionModel().getSelectedItem();
            Stage stage = (Stage) Edit.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AppointmentEdit.fxml"));
            AppointmentEditController controller = new AppointmentEditController(selectedAppt);
            loader.setController(controller);
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        }

    }

    /**
     * This function takes the user to the add appointment screen
     */
    public void handleCreateButton() throws IOException {
        Stage stage = (Stage) Create.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AppointmentAdd.fxml"));
        AppointmentAddController controller = new AppointmentAddController();
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This function populates the monthly and weekly tables with appointments
     */
    public void populateAppointments() throws Exception {
        monthlyId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("id"));
        monthlyTitle.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        monthlyDesc.setCellValueFactory(new PropertyValueFactory<Appointment, String>("desc"));
        monthlyLoc.setCellValueFactory(new PropertyValueFactory<Appointment, String>("loc"));
        monthlyCont.setCellValueFactory(new PropertyValueFactory<Appointment, String>("cont"));
        monthlyType.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        monthlySt.setCellValueFactory(new PropertyValueFactory<Appointment, String>("start"));
        monthlyEnd.setCellValueFactory(new PropertyValueFactory<Appointment, String>("end"));
        monthlyCustId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("custId"));
        monthlyCreatorId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("userId"));
        MonthlyTable.setItems(AppointmentDatabase.getAllMonthlyAppointments());
        weeklyId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("id"));
        weeklyTitle.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        weeklyDesc.setCellValueFactory(new PropertyValueFactory<Appointment, String>("desc"));
        weeklyLoc.setCellValueFactory(new PropertyValueFactory<Appointment, String>("loc"));
        weeklyCont.setCellValueFactory(new PropertyValueFactory<Appointment, String>("cont"));
        weeklyType.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        weeklySt.setCellValueFactory(new PropertyValueFactory<Appointment, String>("start"));
        weeklyEnd.setCellValueFactory(new PropertyValueFactory<Appointment, String>("end"));
        weeklyCustId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("custId"));
        weeklyCreatorId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("userId"));
        WeeklyTable.setItems(AppointmentDatabase.getAllWeeklyAppointments());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            populateAppointments();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
