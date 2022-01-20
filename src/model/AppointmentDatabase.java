package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utility.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class AppointmentDatabase {

    public static Integer getContactId(String name) throws SQLException {
        Integer id = 0;
        String query = "SELECT * FROM contacts WHERE Contact_Name='" + name + "';";
        Statement sql = Database.getConnection().createStatement();
        ResultSet records = sql.executeQuery(query);
        while(records.next()) {
            id = records.getInt("Contact_ID");
        }
        sql.close();
        return id;
    }

    public static ObservableList<String> getContactNames() throws SQLException {
        ObservableList<String> contacts = FXCollections.observableArrayList();
        String query = "SELECT * FROM contacts";
        Statement sql = Database.getConnection().createStatement();
        ResultSet records = sql.executeQuery(query);
        String name = "";
        while(records.next()) {
          name = records.getString("Contact_Name");
          contacts.add(name);
        }
        sql.close();
        return contacts;
    }

    public static Boolean updateAppointment(int id, String title, String desc, String loc, String type, String start,
                                         String end, int custId, int userId, int contId) throws Exception {
        String now = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String command = "UPDATE appointments SET Title='" + title + "', Description='" + desc + "', Location='" + loc + "', Type='" + type + "', Start='" +
                convertToUtc(start) + "', End='" + convertToUtc(end) + "', Last_Update='" + now + "', Last_Updated_By='" + Login.getUser().getUserName() + "', Customer_ID='" + custId +
                "', User_ID='" + userId + "', Contact_ID='" + contId + "' WHERE Appointment_ID='" + id + "';";
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

    public static Boolean addAppointment(int id, String title, String desc, String loc, String type, String start,
                                         String end, int custId, int userId, int contId) throws Exception {
        String now = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String command = "INSERT INTO Appointments \n" +
                "VALUES('" + id + "', '" + title + "', '" + desc + "', '" + loc + "', '" + type + "', '" +
                convertToUtc(start) + "', '" + convertToUtc(end) + "', '" + now + "', '" + Login.getUser().getUserName() + "', '" + now +
                "', '" + Login.getUser().getUserName() + "', '" + custId + "', '" + userId + "', '" + contId + "');";
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

    public static Integer getNumAppointments() throws SQLException {
        Integer rowCount = 0;
        String query = "SELECT * FROM appointments";
        Statement sql = Database.getConnection().createStatement();
        ResultSet records = sql.executeQuery(query);
        while(records.next()) {
            rowCount++;
        }
        sql.close();
        return rowCount;
    }

    public static ObservableList<Appointment> getAllMonthlyAppointments() throws Exception {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        int month = LocalDate.now().getMonthValue();
        String query = "SELECT * FROM appointments as a INNER JOIN contacts as c on a.Contact_ID = c.Contact_ID" +
                " WHERE a.Start >='" + LocalDate.now().minusMonths(1) + "' AND a.Start <='" + LocalDate.now().plusMonths(1) + "'";
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
                    convertToLocal(records.getString("Start")),
                    convertToLocal(records.getString("End")),
                    records.getInt("Customer_ID"),
                    records.getInt("User_ID")
            );
            allAppointments.add(appointment);
        }
        sql.close();
        return allAppointments;
    }

    public static String convertToLocal(String utc) throws ParseException {
        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date utcTime = utcFormat.parse(utc);
        DateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        localFormat.setTimeZone(TimeZone.getDefault());
        System.out.println("Converting from UTC: " + utc + " to Local: " + localFormat.format(utcTime));
        return(localFormat.format(utcTime));
    }

    public static String convertToUtc(String local) throws ParseException {
        DateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        localFormat.setTimeZone(TimeZone.getDefault());
        Date localTime = localFormat.parse(local);
        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        System.out.println("Converting from Local: " + local + " to UTC: " + utcFormat.format(localTime));
        return(utcFormat.format(localTime));
    }

    public static ObservableList<Appointment> getAllWeeklyAppointments() throws SQLException, ParseException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        String query = "SELECT * FROM appointments as a INNER JOIN contacts as c on a.Contact_ID = c.Contact_ID" +
                " WHERE a.Start >='" + LocalDate.now().minusWeeks(1) + "' AND a.Start <='" + LocalDate.now().plusWeeks(1) + "'";
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
                    convertToLocal(records.getString("Start")),
                    convertToLocal(records.getString("End")),
                    records.getInt("Customer_ID"),
                    records.getInt("User_ID")
            );
            allAppointments.add(appointment);
        }
        sql.close();
        return allAppointments;
    }

    public static Boolean deleteAppointment(int id) throws SQLException {
        String command = "DELETE FROM appointments WHERE Appointment_ID ='" + id + "';";
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
