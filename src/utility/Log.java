package utility;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.PrimitiveIterator;

public class Log {
    public static void log (String username, boolean successful) throws IOException {
        String yesNo;
        if (successful) {
            yesNo = "YES";
        } else {
            yesNo = "NO";
        }
        FileWriter fileWriter = new FileWriter("login_activity.txt", true);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        writer.append("Login attempt at: " + ZonedDateTime.now(ZoneOffset.UTC) + " UTC, Username: " +
                username + ", Success: " + yesNo + "\n");
        writer.flush();
        writer.close();
    }
}
