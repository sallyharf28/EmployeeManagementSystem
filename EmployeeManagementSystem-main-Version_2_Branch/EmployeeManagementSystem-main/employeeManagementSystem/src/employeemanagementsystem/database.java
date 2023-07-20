package employeemanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;

public class database {
    private static Connection connect = null;

    private database() {}

    public static Connection connectDb() {
        try {
            if (connect == null) {
                connect = DriverManager.getConnection("jdbc:mysql://localhost/employee", "root", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connect;
    }
}
