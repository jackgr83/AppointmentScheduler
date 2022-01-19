package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utility.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class AppointmentDatabase {

    public static int getNumAppointments() throws SQLException {
        int rowCount = 0;
        String query = "SELECT * FROM appointments";
        Statement sql = Database.getConnection().createStatement();
        ResultSet records = sql.executeQuery(query);
        while(records.next()) {
            rowCount++;
        }
        sql.close();
        return rowCount;
    }

    public static ObservableList<Appointment> getAllMonthlyAppointments() throws SQLException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        String query = "SELECT * FROM appointments as a INNER JOIN contacts as c on a.Contact_ID = c.Contact_ID" +
                " WHERE a.Start >='" + LocalDate.now() + "' AND a.Start <='" + LocalDate.now().plusMonths(1) + "'";
        Statement sql = Database.getConnection().createStatement();
        ResultSet records = sql.executeQuery(query);

        while(records.next()) {
            Appointment appointment = new Appointment(
                    records.getInt("Appointment_ID"),
                    records.getString("Title"),
                    records.getString("Description"),
                    records.getString("Location"),
                    records.getString("Contact_Name"),
                    records.getString("Type"),
                    records.getString("Start"),
                    records.getString("End"),
                    records.getInt("Customer_ID"),
                    records.getInt("User_ID")
            );
            allAppointments.add(appointment);
        }
        sql.close();
        return allAppointments;
    }

    public static ObservableList<Appointment> getAllWeeklyAppointments() throws SQLException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        String query = "SELECT * FROM appointments as a INNER JOIN contacts as c on a.Contact_ID = c.Contact_ID" +
                " WHERE a.Start >='" + LocalDate.now() + "' AND a.Start <='" + LocalDate.now().plusWeeks(1) + "'";
        Statement sql = Database.getConnection().createStatement();
        ResultSet records = sql.executeQuery(query);

        while(records.next()) {
            Appointment appointment = new Appointment(
                    records.getInt("Appointment_ID"),
                    records.getString("Title"),
                    records.getString("Description"),
                    records.getString("Location"),
                    records.getString("Contact_Name"),
                    records.getString("Type"),
                    records.getString("Start"),
                    records.getString("End"),
                    records.getInt("Customer_ID"),
                    records.getInt("User_ID")
            );
            allAppointments.add(appointment);
        }
        sql.close();
        return allAppointments;
    }

    public static Boolean deleteCustomerAppointments(int id) throws SQLException {
        String command = "DELETE FROM appointments WHERE Customer_ID ='" + id + "'";
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
}
