package model;

import utility.Database;

import java.sql.SQLException;
import java.sql.Statement;

public class AppointmentDatabase {

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
