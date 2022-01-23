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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

public class AppointmentDatabase {

    /**
     * @param name get contact ID given contact name
     */
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

    /**
     * Get all contact names
     */
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

    /**
     * @param id
     * @param title
     * @param desc
     * @param loc
     * @param type
     * @param start
     * @param end
     * @param custId
     * @param userId
     * @param contId
     *
     * Update the Appointment in the database
     */
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

    /**
     * @param id
     * @param title
     * @param desc
     * @param loc
     * @param type
     * @param start
     * @param end
     * @param custId
     * @param userId
     * @param contId
     *
     * Add the Appointment to the database
     */
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

    /**
     * Get total number of appointments in the database
     */
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

    /**
     * @return Appointment list of all monthly Appointments
     */
    public static ObservableList<Appointment> getAllMonthlyAppointments() throws Exception {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        int month = LocalDate.now().getMonthValue();
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

    /**
     * @param utc convert utc datetime string to local date time string
     */
    public static String convertToLocal(String utc) throws ParseException {
        SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date utcTime = utcFormat.parse(utc);
        SimpleDateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        localFormat.setTimeZone(TimeZone.getDefault());
        String local = localFormat.format(utcTime);
//        System.out.println("Converting from UTC: " + utc + " to Local: " + local);
        return(local);
    }

    /**
     * @param local convert local datetime string to utc date time string
     */
    public static String convertToUtc(String local) throws ParseException {
        SimpleDateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        localFormat.setTimeZone(TimeZone.getDefault());
        Date localTime = localFormat.parse(local);
        SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//        System.out.println("Converting from Local: " + local + " to UTC: " + utcFormat.format(localTime));
        return(utcFormat.format(localTime));
    }

    /**
     * @return Appointment list of all weekly appointments
     */
    public static ObservableList<Appointment> getAllWeeklyAppointments() throws SQLException, ParseException {
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

    /**
     * @param id delete specific appointment
     */
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

    /**
     * @param id delete all appointments associated with specific customer
     */
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

    /**
     * @param custId
     * @param date
     * Get a customer's appointment times on specific day to check for overlapping times when adding an appointment
     */
    public static ArrayList<String[]> getCustomerAppointmentTimes(int custId, String date) throws Exception {
        ArrayList<String[]> customerAppointmentTimes = new ArrayList<>();
        String query = "SELECT * FROM appointments WHERE Customer_ID ='" + custId + "'";
        Statement sql = Database.getConnection().createStatement();
        ResultSet records = sql.executeQuery(query);

        while (records.next()) {
            String dt = records.getString("Start").split("\\s+")[0];
            if (dt.equalsIgnoreCase(date)) {
                System.out.println("DATE: " + dt + " INPUT: " + date);
                String[] startEnd = new String[] {records.getString("Start"), records.getString("End")};
                customerAppointmentTimes.add(startEnd);

            }
        }
        sql.close();
        return customerAppointmentTimes;
    }

    /**
     * @param custId
     * @param apptId
     * @param date
     * Get a customer's other appointment times on a specific day to check for overlapping times when editing an appointment
     */
    public static ArrayList<String[]> getOtherCustomerAppointmentTimes(int custId, int apptId, String date) throws Exception {
        ArrayList<String[]> customerAppointmentTimes = new ArrayList<>();
        String query = "SELECT * FROM appointments WHERE NOT Appointment_ID='" + apptId + "' AND Customer_ID ='" + custId + "'";
        Statement sql = Database.getConnection().createStatement();
        ResultSet records = sql.executeQuery(query);

        while (records.next()) {
            String dt = records.getString("Start").split("\\s+")[0];
            if (dt.equalsIgnoreCase(date)) {
                System.out.println("DATE: " + dt + " INPUT: " + date);
                String[] startEnd = new String[] {records.getString("Start"), records.getString("End")};
                customerAppointmentTimes.add(startEnd);
            }
        }
        sql.close();
        return customerAppointmentTimes;
    }

    /**
     * @return Appointment list of any appointments occuring within 15 times of user's login
     */
    public static ObservableList<Appointment> getAppointmentsIn15Mins() throws Exception{
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        ZonedDateTime timezone = time.atZone(ZoneId.systemDefault());
        ZonedDateTime utctime = timezone.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime utc15Ahead = utctime.plusMinutes(15);

        String query = "SELECT * FROM appointments as a LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID WHERE " +
                "Start BETWEEN '" + utctime.format(format) + "' AND '" + utc15Ahead.format(format) + "' AND User_ID='" + Login.getUser().getUserId() + "'";
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
            appointments.add(appointment);
        }
        sql.close();
        return appointments;
    }
}
