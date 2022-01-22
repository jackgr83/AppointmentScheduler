package model;

import utility.Database;
import utility.Log;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;

public class Login {

    private static User user;

    /**
     * @param username
     * @param password
     *
     * Check if username and password exists in the database
     */
    public static boolean tryLogin(String username, String password) throws SQLException, IOException {
        String query = "SELECT * FROM USERS WHERE User_Name='" + username + "' AND Password='" + password + "'";
        Statement sql = Database.getConnection().createStatement();
        ResultSet records = sql.executeQuery(query);
        if (records.next()) {
            user = new User(records.getString("User_Name"), records.getInt("User_ID"));
            Log.log(username, true);
            sql.close();
            return true;
        } else {
            sql.close();
            Log.log(username, false);
            return false;
        }

    }

    /**
     * @return User
     */
    public static User getUser() {
        return user;
    }
}
