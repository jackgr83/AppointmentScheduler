package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import utility.Database;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    @FXML
    private TextArea report1;
    @FXML
    private TextArea report2;
    @FXML
    private TextArea report3;
    @FXML
    private Button Back;


    /**
     * This function populates the first report text section
     */
    public void createReport1() throws SQLException {
        ArrayList<String> lines = new ArrayList<String>();
        Statement sql = Database.getConnection().createStatement();
        String query1 = "SELECT MONTHNAME(Start) as 'Month', COUNT(*) as 'Total' FROM appointments GROUP BY MONTHNAME(Start)";
        ResultSet records1 = sql.executeQuery(query1);
        while (records1.next()) {
            lines.add("Total customer appointments in " + records1.getString("Month") + ": " + records1.getInt("Total"));
        }
        String query2 = "SELECT Type, COUNT(*) as 'Total' FROM appointments GROUP BY Type";
        ResultSet records2 = sql.executeQuery(query2);
        while (records2.next()) {
            lines.add("Total customer appointments of type " + records2.getString("Type") + ": " + records2.getInt("Total"));
        }
        sql.close();

        StringBuilder report1Text = new StringBuilder();
        for (int i=0;i<lines.size();i++) {
            report1Text.append(lines.get(i) + " \n");
        }
        report1.setText(report1Text.toString());
    }

    /**
     * This function populates the second report text section
     */
    public void createReport2() throws SQLException {
        Statement sql = Database.getConnection().createStatement();

        ArrayList<String> contactNames = new ArrayList<String>();
        String contactNameQuery = "SELECT DISTINCT Contact_Name FROM contacts ORDER BY Contact_Name";
        ResultSet contactNameRecords = sql.executeQuery(contactNameQuery);
        while (contactNameRecords.next()) {
            contactNames.add(contactNameRecords.getString("Contact_Name"));
        }
        ArrayList<String> lines = new ArrayList<String>();
        for (int i=0;i<contactNames.size();i++) {
            lines.add("Schedule for " + contactNames.get(i) + ":");
            String query = "SELECT a.Appointment_ID, a.Title, a.Description, a.Type, a.Start, a.End, a.Customer_ID, c.Contact_Name " +
                    "FROM appointments as a JOIN contacts as c ON a.Contact_ID = c.Contact_ID " +
                    "WHERE c.Contact_Name='" + contactNames.get(i) + "'";
            ResultSet records = sql.executeQuery(query);
            while (records.next()) {
                lines.add("     " + records.getString("Start") + " TO " + records.getString("End") + ", Appointment ID: " +
                        records.getString("Appointment_ID") + ", Title: " + records.getString("Title") + ", Description: " +
                        records.getString("Description") + ", Type: " + records.getString("Type") + ", Customer ID: " + records.getInt("Customer_ID"));
            }

        }

        sql.close();
        StringBuilder report2Text = new StringBuilder();
        for (int i=0;i<lines.size();i++) {
            report2Text.append(lines.get(i) + " \n");
        }
        report2.setText(report2Text.toString());
    }

    /**
     * This function populates the third report text section
     */
    public void createReport3() throws SQLException {
        // Total Number of Customer Appointments for Each Customer
        ArrayList<String> lines = new ArrayList<String>();
        Statement sql = Database.getConnection().createStatement();
        String query = "SELECT c.Customer_Name, COUNT(*) as 'Total' FROM appointments as a JOIN customers as c ON a.Customer_ID = c.Customer_ID " +
                "GROUP BY c.Customer_Name";
        ResultSet records = sql.executeQuery(query);
        while (records.next()) {
            lines.add("Total number of appointments for " + records.getString("Customer_Name") + ": " + records.getInt("Total"));
        }

        sql.close();

        StringBuilder report3Text = new StringBuilder();
        for (int i=0;i<lines.size();i++) {
            report3Text.append(lines.get(i) + " \n");
        }
        report3.setText(report3Text.toString());

    }

    /**
     * This function takes the user back to the main screen
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            createReport1();
            createReport2();
            createReport3();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
