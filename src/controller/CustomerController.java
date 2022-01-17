package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;
import model.CustomerDatabase;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {
    @FXML
    private Button BackButton;
    @FXML
    private Button AddButton;
    @FXML
    private Button EditButton;
    @FXML
    private Button DeleteButton;
    @FXML
    private TableView<Customer> CustomerTable;
    @FXML
    private TableColumn<Customer, Integer> IDColumn;
    @FXML
    private TableColumn<Customer, String> NameColumn;
    @FXML
    private TableColumn<Customer, String> PhoneColumn;
    @FXML
    private TableColumn<Customer, String> AddressColumn;
    @FXML
    private TableColumn<Customer, String> ZipCodeColumn;
    @FXML
    private TableColumn<Customer, String> CountryColumn;
    @FXML
    private TableColumn<Customer, String> DivisionColumn;

    Alert error = new Alert(Alert.AlertType.ERROR);
    Alert warn = new Alert(Alert.AlertType.WARNING);

    public void handleBackButton() throws IOException {
        Stage stage = (Stage) BackButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Main.fxml"));
        MainController controller = new MainController();
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    public void handleAddButton() throws IOException {
        Stage stage = (Stage) AddButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/CustomerAdd.fxml"));
        CustomerAddController controller = new CustomerAddController();
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    public void handleEditButton() throws IOException {

        Customer selectedCustomer = CustomerTable.getSelectionModel().getSelectedItem();

        Stage stage = (Stage) EditButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/CustomerEdit.fxml"));
        CustomerEditController controller = new CustomerEditController(selectedCustomer);
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    public void handleDeleteButton() throws IOException {
        Customer selectedCustomer = CustomerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            error.setTitle("Error");
            error.setHeaderText("No customer selected");
            error.showAndWait();
            return;
        }
        String message = "Are you sure you want to delete Customer: " + selectedCustomer.getId() + " and all their appointments?";
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Warning");
        Optional<ButtonType> clicked = confirm.showAndWait();

        if (clicked.get() == ButtonType.YES) {
            Boolean deletedAppts = true;
            Boolean deletedCust = false;

        }

    }

    public void populateCustomerTable() throws SQLException {
        IDColumn.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("id"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        PhoneColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
        AddressColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        ZipCodeColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("zipcode"));
        CountryColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("country"));
        DivisionColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("division"));
        CustomerTable.setItems(CustomerDatabase.getAllCustomers());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            populateCustomerTable();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
