package model;

import utility.Database;
import utility.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;

public class Login {

    private static User user;
    private static ZoneId zoneId;

    public static boolean tryLogin(String username, String password) throws SQLException {
        String query = "SELECT * FROM USERS WHERE User_Name='" + username + "' AND Password='" + password + "'";
        Statement sql = Database.connect().createStatement();
        ResultSet records = sql.executeQuery(query);
        if (records.next()) {
            user = new User();
            sql.close();
            Log.log(username, true);
            return true;
        } else {
            sql.close();
            Log.log(username, false);
            return false;
        }

    }
}
