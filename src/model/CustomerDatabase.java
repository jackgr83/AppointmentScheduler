package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utility.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerDatabase {


    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        String query = "SELECT cu.Customer_ID, cu.Customer_Name, cu.Address, cu.Postal_Code, cu.Phone, d.Division, co.Country" +
                " FROM CUSTOMERS as cu INNER JOIN FIRST_LEVEL_DIVISIONS as d on cu.Division_ID = d.Division_ID" +
                " INNER JOIN COUNTRIES as co on d.Country_ID = co.Country_ID";
        Statement sql = Database.getConnection().createStatement();
        ResultSet records = sql.executeQuery(query);

        while(records.next()) {
            Customer customer = new Customer(
                    records.getInt("Customer_ID"),
                    records.getString("Customer_Name"),
                    records.getString("Address"),
                    records.getString("Postal_Code"),
                    records.getString("Phone"),
                    records.getString("Country"),
                    records.getString("Division")
                    );
            allCustomers.add(customer);
        }
        sql.close();
        return allCustomers;
    }
}
