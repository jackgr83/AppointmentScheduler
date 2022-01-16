package utility;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    private static Connection connection;
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/client_schedule";
    private static final String USER = "sqlUser";
    private static final String PASS = "Passw0rd!";

    public static Connection connect() {
        return connection;
    }

    public static void testConnection() throws Exception {
        System.out.println("testing connection...");
        Class.forName(DRIVER);
        connection = DriverManager.getConnection(URL, USER, PASS);
        System.out.println("Connected!");
    }
}
