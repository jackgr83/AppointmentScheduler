package utility;

import java.sql.Connection;

public class Database {
    private static Connection connection;

    public static Connection connect() {
        return connection;
    }
}
