package Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;

public class MainConnection {
    //MANAGEMENT DATABASE
    private static final String localConnection = "jdbc:sqlite:C:\\Users\\Public\\Database\\LocalConnection.zny";

    //MANAGEMENT DATABASE
    public static Connection LocalConnection() throws Exception {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(localConnection);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }






}
