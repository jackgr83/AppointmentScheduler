package utility;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    /**
     * Initialize the database connection parameters
     */
    private static Connection connection;
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/client_schedule";
    private static final String USER = "sqlUser";
    private static final String PASS = "Passw0rd!";

    /**
     * Get the database connection for executing a SQL query
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Utilize the jdbc driver to connect to the database
     */
    public static void connect() throws Exception {
        System.out.println("testing connection...");
        Class.forName(DRIVER);
        connection = DriverManager.getConnection(URL, USER, PASS);
        System.out.println("Connected!");
    }
}
