package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.CustomerDatabase;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;

public class CustomerAddController implements Initializable {

    @FXML
    private TextField IdField;
    @FXML
    private TextField NameField;
    @FXML
    private TextField AddressField;
    @FXML
    private TextField ZipcodeField;
    @FXML
    private TextField PhoneField;
    @FXML
    private ComboBox CountryBox;
    @FXML
    private ComboBox DivisionBox;
    @FXML
    private Button SaveButton;
    @FXML
    private Button CancelButton;

    Alert error = new Alert(Alert.AlertType.ERROR);
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    Alert warn = new Alert(Alert.AlertType.WARNING);
    Integer id;

    /**
     * This function checks inputs in the following ways before updating the appointment:
     * Logical error checks:
     * - No input can be empty
     */
    public void handleSaveButton() throws IOException, SQLException {
        String name = NameField.getText();
        String address = AddressField.getText();
        String zip = ZipcodeField.getText();
        String phone = PhoneField.getText();
        String country = CountryBox.getValue().toString();
        String division = DivisionBox.getValue().toString();


        if (name.isBlank() || address.isBlank() || zip.isBlank() || phone.isBlank() || country.isBlank() || division.isBlank()) {
            error.setTitle("Error");
            error.setHeaderText("Please fill in all fields");
            error.showAndWait();
            return;
        }

        Boolean successful = CustomerDatabase.addCustomer(id, name, address, zip, phone, CustomerDatabase.getDivisionId(division));

        if (successful) {
            confirm.setTitle("Success!");
            confirm.setHeaderText("Customer added");
            confirm.showAndWait();
            Stage stage = (Stage) SaveButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Customer.fxml"));
            CustomerController controller = new CustomerController();
            loader.setController(controller);
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } else {
            warn.setTitle("Failure");
            warn.setHeaderText("Failed to add customer");
            warn.showAndWait();
            return;
        }

    }

    /**
     * This function takes user back to the customer view screen when Cancel button is pressed
     */
    public void handleCancelButton() throws IOException {
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Customer.fxml"));
        CustomerController controller = new CustomerController();
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This function generates a sequential ID
     */
    private void generateId() throws SQLException {
        id = (CustomerDatabase.getAllCustomers().size() + 1);
        IdField.setText(id.toString());
    }

    /**
     * LAMBDA - Adds a change listener to the Combo Box
     * This Lambda expression simplifies and reduces the amount of code needed
     * to add a change listener on the Country/Division Combo Boxes
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            generateId();
            CountryBox.setItems(CustomerDatabase.getAllCountries());
        } catch (SQLException e){
            System.out.println(e);
        }

        CountryBox.valueProperty().addListener((obsV, oldV, newV) -> {
            if (newV != null) {
                DivisionBox.setDisable(false);
                try {
                    DivisionBox.setItems(CustomerDatabase.getDivisions(CountryBox.getValue().toString()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                DivisionBox.getItems().clear();
                DivisionBox.setDisable(true);
            }
        });
    }
}
