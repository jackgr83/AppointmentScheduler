package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utility.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CustomerDatabase {

    public static Integer getCustomerId(String name) throws SQLException {
        Integer id = 0;
        String query = "SELECT * FROM customers WHERE Customer_Name='" + name + "';";
        Statement sql = Database.getConnection().createStatement();
        ResultSet records = sql.executeQuery(query);
        while (records.next()) {
            id = records.getInt("Customer_ID");
        }
        sql.close();
        return id;
    }

    public static ObservableList<String> getDivisions(String country) throws SQLException {
        ObservableList<String> divisions = FXCollections.observableArrayList();
        String query = "SELECT * FROM COUNTRIES RIGHT OUTER JOIN " +
                "first_level_divisions as d on COUNTRIES.Country_ID = d.Country_ID WHERE COUNTRIES.Country = '" + country + "';";
        Statement sql = Database.getConnection().createStatement();
        ResultSet records = sql.executeQuery(query);
        while (records.next()) {
            divisions.add(records.getString("Division"));
        }
        sql.close();
        return divisions;
    }

    public static ObservableList<String> getAllCountries() throws SQLException {
        ObservableList<String> allCountries = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT Country FROM COUNTRIES";
        Statement sql = Database.getConnection().createStatement();
        ResultSet records = sql.executeQuery(query);

        while (records.next()) {
            allCountries.add(records.getString("Country"));
        }
        sql.close();
        return allCountries;
    }

    public static Integer getDivisionId(String division) throws SQLException {
        Integer id = 0;
        String query = "SELECT Division_ID FROM first_level_divisions WHERE Division = '" + division + "';";
        Statement sql = Database.getConnection().createStatement();
        ResultSet records = sql.executeQuery(query);
        while (records.next()){
            id = records.getInt("Division_ID");
        }
        sql.close();
        return id;
    }

    public static Boolean updateCustomer(int id, String name, String address, String zip, String phone, int divisionId) throws SQLException {
        String datetime = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString();
        String command = "UPDATE customers SET Customer_Name='" + name + "', Address='" + address + "', Postal_Code='" + zip + "', Phone='" +
                phone + "', Last_Update='" + datetime + "', Last_Updated_By='" + Login.getUser().getUserName() + "', Division_ID='" + divisionId +
                "' WHERE Customer_ID='" + id + "';";
        Statement sql = Database.getConnection().createStatement();
        try {
            sql.executeUpdate(command);
            sql.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            sql.close();
            return false;
        }
    }

    public static Boolean deleteCustomer(int id) throws SQLException {
        String command = "DELETE FROM customers WHERE Customer_ID ='" + id + "'";
        Statement sql = Database.getConnection().createStatement();
        try {
            sql.executeUpdate(command);
            sql.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            sql.close();
            return false;
        }
    }

    public static Boolean addCustomer(int id, String name, String address, String zip, String phone, int divisionId) throws SQLException {
        String datetime = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString();
        String command = "INSERT INTO CUSTOMERS \n" +
                "VALUES('" + id + "', '" + name + "', '" + address + "', '" + zip + "', '" + phone + "', '" +
                datetime + "', '" + Login.getUser().getUserName() + "', '" +
                datetime + "', '" + Login.getUser().getUserName() + "', '" + divisionId + "');";
        Statement sql = Database.getConnection().createStatement();
        try {
            sql.executeUpdate(command);
            sql.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            sql.close();
            return false;
        }

    }

    public static ObservableList<String> getCustomerNames() throws SQLException {
        ObservableList<String> customers = FXCollections.observableArrayList();
        String query = "SELECT * FROM customers";
        Statement sql = Database.getConnection().createStatement();
        ResultSet records = sql.executeQuery(query);
        String name = "";
        while(records.next()) {
            name = records.getString("Customer_Name");
            customers.add(name);
        }
        sql.close();
        return customers;
    }


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
