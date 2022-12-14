package Classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class ConnectDB {

    static ObservableList<String> Paths = FXCollections.observableArrayList();
    static ObservableList<String> OfflinePaths = FXCollections.observableArrayList();



    public ConnectDB()throws Exception{

    }
    public static ObservableList<String> getPath()throws Exception{
        Connection MainConnect = MainConnection.LocalConnection();
        String sql = "SELECT Path, File FROM Branch ";
        PreparedStatement ps = MainConnect.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            Paths.add(rs.getString("Path")+""+rs.getString("File"));
        }
        MainConnect.close();
        return Paths;
    }



    //ALABA DATABASE
    public static Connection Main() throws Exception {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(getPath().get(0));
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }



    public static Connection Log() throws Exception {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(getPath().get(1));
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
