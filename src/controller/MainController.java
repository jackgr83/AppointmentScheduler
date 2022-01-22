package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class MainController {
    @FXML
    private Button CustomerButton;
    @FXML
    private Button AppointmentButton;
    @FXML
    private Button ReportsButton;

    /**
     * This function takes user to the Customer main screen
     */
    public void handleCustomerButton() throws IOException {
        Stage stage = (Stage) CustomerButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Customer.fxml"));
        CustomerController controller = new CustomerController();
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This function takes user to the Appointment main screen
     */
    public void handleAppointmentButton() throws IOException{
        Stage stage = (Stage) AppointmentButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Appointment.fxml"));
        AppointmentController controller = new AppointmentController();
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This function takes user to the Reports main screen
     */
    public void handleReportsButton() throws IOException {
        Stage stage = (Stage) ReportsButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Reports.fxml"));
        ReportsController controller = new ReportsController();
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }
}
