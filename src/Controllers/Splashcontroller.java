package Controllers;


import Classes.ConnectDB;
import Classes.MainClass;
import Classes.MainConnection;
import com.jfoenix.controls.JFXProgressBar;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class Splashcontroller implements Initializable {
    @FXML
    private Label accounttech;
    @FXML
    private Label richtech;
    @FXML
    private JFXProgressBar Bar;
    @FXML
    private Label Label;
    @FXML
    private Label Update;

    public Service<Void> Loadservice, startservice;
    public Stage MainStage, AuthView;
    public AnchorPane root;
    public Scene cene;
    FileWriter writer;
    InputStream in, in2;
    static boolean mousecode;
    static boolean isLoading;
    LocalDate expirationdate = LocalDate.of(2023, 1, 20);
    boolean newVersion;
    Alert alert;
    DateTimeFormatter format  = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public String ClassName = "SplashController ";
    public Alert Erroralert;

       @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            root.getStylesheets().remove(0);
            root.getStylesheets().add("file:/"+System.getProperty("user.home").replace("\\", "/" ) + "/currentTheme.css");

            LocalDate D = LocalDate.now();
            LocalTime T = LocalTime.now();

            String localTime  = D.format(MainClass.DatabaseDateFormat)+" "+T.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String[] x  = localTime.split(" ");
            for( String y : x){
                System.out.println(y);
            }
            File version = new File(System.getProperty("user.home") + "/AppData/Local/Version.txt");
            File logFile = new File(System.getProperty("user.home") + "/Desktop/Logs/Log.txt");
            File logDir = new File(System.getProperty("user.home") + "/Desktop/Logs/");
            logDir.mkdirs();
            if(!logFile.exists()){
                logFile.createNewFile();
            }
            if (version.createNewFile()) {
                writer = new FileWriter(version);
                writer.write("3.2.2");
                writer.close();
                newVersion = true;
            }else{
                List<String> lines = Files.readAllLines(version.toPath());
                for (String line : lines) {
                    if (line.contains("3.2.2")) {
                        newVersion = false;
                    }else {
                        newVersion = true;
                        version.delete();
                        writer = new FileWriter(version);
                        writer.write("3.2.2");
                        writer.close();
                    }

                }
            }

            //for dominion not in use
//            in = getClass().getClassLoader().getResourceAsStream("./Main/raw/Staff.sqlite");
//            in2 = getClass().getClassLoader().getResourceAsStream("./Main/raw/Main.sqlite");

            isLoading = true;
            LoadService();
            StartService();

        }catch (Exception e){
            try {
                StringBuilder writer = new StringBuilder();
                Files.write(MainClass.logFile.toPath(), (writer.append("\n").append(ClassName).append(e.getMessage()).append("  ").append(e.getLocalizedMessage()).toString()).getBytes(), StandardOpenOption.APPEND);
                if(MainClass.isDebugging){
                    Erroralert.show();
                }
            } catch (Exception ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }

    }

private void StartService(){


        startservice = new Service<Void>(){

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            CreateDatabase();
                            CheckTrial();
                            while (isLoading){
                                Thread.sleep(500);
                            }
                        } catch (Exception e) {
                            try {
                                StringBuilder writer = new StringBuilder();
                                Files.write(MainClass.logFile.toPath(), (writer.append("\n").append(ClassName).append(e.getMessage()).append("  ").append(e.getLocalizedMessage()).toString()).getBytes(), StandardOpenOption.APPEND);

                            } catch (Exception ioException) {
                                ioException.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                        return null;
                    }

                };
            }
        };

        startservice.setOnSucceeded(action -> {
            startservice.cancel();
            try {
                System.out.println("Doneee ");
                Done((Stage)Bar.getScene().getWindow());
            } catch (Exception e) {
                try {
                    StringBuilder writer = new StringBuilder();
                    Files.write(MainClass.logFile.toPath(), (writer.append("\n").append(ClassName).append(e.getMessage()).append("  ").append(e.getLocalizedMessage()).toString()).getBytes(), StandardOpenOption.APPEND);
                    if(MainClass.isDebugging){
                        Erroralert.show();
                    }
                } catch (Exception ioException) {
                    ioException.printStackTrace();
                }
                e.printStackTrace();
            }
            if(newVersion){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New Version 3.2.2");
            alert.setContentText("Database Update Complete! \n " +
                    "What's New ? \n " +
                    "1. Added Work Offline \n " +
//                    "2. Added Card Auditing \n " +
//                    "3. Cards will now be marked as 'Inactive' if no payment in 1 month \n" +
//                    "4. All Cards will be due for auditing every month \n" +
                    "2. Updated Customers Section \n" +
//                    "6. New Color Theme \n" +
//                    "7. Log in Details now required \n" +
//                    "\n" +
                    "");
            alert.setHeaderText(null);
            alert.setWidth(300);
            alert.showAndWait();
        }} );

    Update.setVisible(true);
    Update.textProperty().bind(startservice.messageProperty());

        startservice.restart();
}

private void LoadService() {
    System.out.println("load service");
    File MainFile = new File("C:\\Users\\Public\\Database\\BVFM_Database\\Main.og");
    if(!MainFile.exists()){
        Update.setVisible(true);
    }
    if(newVersion){
        Update.setVisible(true);
        Update.setText("Welcome to Version 3.2.2 2022 Edition...Please Wait while the Database Updates!!");
    }

        Loadservice = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {

                        for(int x = 0; x < 101; x+=2){
                            updateProgress(x, 100);
                            updateMessage(x+"%");

                            Thread.sleep(30);
                        }
                        return null;
                    }
                };
            }
        };


        Loadservice.setOnSucceeded(event -> isLoading = false);

    Bar.progressProperty().bind(Loadservice.progressProperty());
    Label.textProperty().bind(Loadservice.messageProperty());


    Loadservice.restart();

}

private void CreateDatabase() throws Exception{
File LocalConnectionFile = new File("C:\\Users\\Public\\Database\\LocalConnection.zny");
File directory = new File("C:\\Users\\Public\\Database\\HotelApplication\\");
File MainFile = new File("C:\\Users\\Public\\Database\\HotelApplication\\Main.zny");
directory.mkdirs();

boolean isNew = LocalConnectionFile.createNewFile();
    if(isNew){
        System.out.println("creating new database");
        Connection con = MainConnection.LocalConnection();
        String sql = "CREATE TABLE IF NOT EXISTS Branch (path, file)";
        PreparedStatement ps =  con.prepareStatement(sql);
        ps.execute();
        
        //for today 10/2/2022 only main and Log database is required others to be added as per need
        String[] files = {"Main"};

        for(String file : files){
            sql = "INSERT INTO Branch (path, file) VALUES (?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, "jdbc:sqlite:C:\\Users\\Public\\Database\\HotelApplication\\" );
            ps.setString(2, file+".zny");
            ps.execute();

            File sqlite = new File("C:\\Users\\Public\\Database\\HotelApplication\\"+file+".zny");
            sqlite.createNewFile();
        }

        con.close();

        con = ConnectDB.Main();
        sql = "CREATE TABLE IF NOT EXISTS Staffs (Name, Password, Access)";
        ps = con.prepareStatement(sql);
        ps.execute();

//        sql = "SELECT Name FROM Staffs WHERE Name = 'Management' ";
//        ps =  con.prepareStatement(sql);
//        ResultSet rs = ps.executeQuery();
//        if(!rs.next()){
            sql = "INSERT INTO Staffs (Name, Password, Access) VALUES (?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, "Manager" );
            ps.setString(2, "Manager");
            ps.setInt(3, 3);
            ps.execute();

        sql = "INSERT INTO Staffs (Name, Password, Access) VALUES (?,?,?)";
        ps = con.prepareStatement(sql);
        ps.setString(1, "Secretary" );
        ps.setString(2, "1234");
        ps.setInt(3, 1);
        ps.execute();
//        }
    }else {
        System.out.println("database Already Exists");
    }

    Connection con;
    String sql;
    PreparedStatement ps;
    try {

//        con = ConnectDB.Main();
//        sql = "DROP TABLE IF EXISTS RoomList";
//        ps = con.prepareStatement(sql);
//        ps.execute();

        con = ConnectDB.Main();
        sql = "CREATE TABLE IF NOT EXISTS RoomList (" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, " +
                "Number INTEGER," +
                "TimesBooked  INTEGER DEFAULT (0)," +
                "DaysBooked  INTEGER DEFAULT (0)," +
                "Customers  INTEGER DEFAULT (0)," +
                "TotalAmount  INTEGER DEFAULT (0)," +
                "Receipts  INTEGER DEFAULT (0)," +
                "CheckInDate  STRING DEFAULT [NO]," +
                "CheckInTime STRING DEFAULT [NO]," +
                "ToCheckOutDate STRING DEFAULT [NO]," +
                "ToCheckOutTime STRING DEFAULT [NO]," +
                "CheckedOutDate STRING DEFAULT [NO]," +
                "CheckedOutTime STRING DEFAULT [NO]," +
                "Type STRING, " +
                "Name STRING, " +
                "Rate INTEGER, " +
                "Status STRING, " +
                "Occupant STRING" +
                ")";
        ps = con.prepareStatement(sql);
        ps.execute();
        sql = "CREATE TABLE IF NOT EXISTS Receipts(" +
                "Date STRING, " +
                "Receipt INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL," +
                "Name STRING," +
                "Phone STRING," +
                "Address STRING," +
                "isAdult String," +
                "Gender STRING," +
                "Room STRING," +
                "CheckInDate STRING," +
                "CheckInTime STRING," +
                "ToCheckOutDate STRING," +
                "ToCheckOutTime STRING," +
                "CheckedOutDate STRING," +
                "CheckedOutTime STRING," +
                "Occupants INTEGER," +
                "Days INTEGER," +
                "Rate INTEGER," +
                "Total INTEGER," +
                "Paid INTEGER," +
                "Method INTEGER," +
                "Balance INTEGER)";
        ps = con.prepareStatement(sql);
        ps.execute();

        sql = "CREATE TABLE IF NOT EXISTS Payments(" +
                "Date STRING, " +
                "Receipt INTEGER, " +
                "Name STRING," +
                "Room STRING," +
                "Time STRING," +
                "Total INTEGER," +
                "Paid INTEGER," +
                "Method INTEGER," +
                "Balance INTEGER)";
        ps = con.prepareStatement(sql);
        ps.execute();

        sql = "CREATE TABLE IF NOT EXISTS Occupants(" +
                "Date STRING, " +
                "Receipt INTEGER ," +
                "Name STRING," +
                "Phone STRING," +
                "Address STRING," +
                "isAdult String," +
                "Gender STRING," +
                "Room STRING," +
                "CheckInDate STRING," +
                "CheckInTime STRING," +
                "ToCheckOutDate STRING," +
                "ToCheckOutTime STRING," +
                "CheckedOutDate STRING," +
                "CheckedOutTime STRING," +
                "Days INTEGER)";
        ps = con.prepareStatement(sql);
        ps.execute();

        //check in will serve as guests list
        sql = "CREATE TABLE IF NOT EXISTS CheckIns(" +
                "Date STRING, " +
                "Receipt INTEGER," +
                "Name STRING," +
                "Phone STRING," +
                "Address STRING," +
                "isAdult String," +
                "Gender STRING," +
                "Room STRING," +
                "CheckInDate STRING," +
                "CheckInTime STRING," +
                "ToCheckOutDate STRING," +
                "ToCheckOutTime STRING," +
                "CheckedOutDate STRING," +
                "CheckedOutTime STRING," +
                "Days INTEGER)";
        ps = con.prepareStatement(sql);
        ps.execute();

        sql = "CREATE TABLE IF NOT EXISTS Rooms(" +
                "Date STRING, " +
                "Receipt INTEGER," +
                "Name STRING," +
                "Phone STRING," +
                "Address STRING," +
                "isAdult String," +
                "Gender STRING," +
                "Room STRING," +
                "CheckInDate STRING," +
                "CheckInTime STRING," +
                "ToCheckOutDate STRING," +
                "ToCheckOutTime STRING," +
                "CheckedOutDate STRING," +
                "CheckedOutTime STRING," +
                "Days INTEGER)";
        ps = con.prepareStatement(sql);
        ps.execute();
    } catch (Exception e) {
        e.printStackTrace();
        return;

    }

//
//    sql ="CREATE TABLE IF NOT EXISTS GuestList (Date STRING, Name STRING, Phone INTEGER, Gender STRING, Adult STRING, Room STRING, CheckedIn STRING, ToCheckOut STRING )";
//    ps = con.prepareStatement(sql);
//    ps.execute();
//
//    sql ="CREATE TABLE IF NOT EXISTS ReservationList (Date STRING, Name STRING, Phone INTEGER, Gender STRING, Room STRING, ToCheckIn STRING, ToCheckOut STRING, Deposit INTEGER  )";
//    ps = con.prepareStatement(sql);
//    ps.execute();

//    sql ="CREATE TABLE IF NOT EXISTS CustomersLog (Date,Ind INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, Customer, Log)";
//    ps = con.prepareStatement(sql);
//    ps.execute();
//
//    sql ="CREATE TABLE IF NOT EXISTS SoldItems (ItemNo, Description,Condition,ImageLink,SellingPrice,AmountPaid INTEGER,Location,Customer,SoldDate,Comment,Status,Dispatch,DispatchDate,SortedBy)";
//    ps = con.prepareStatement(sql);
//    ps.execute();
//
//    sql ="CREATE TABLE IF NOT EXISTS DeliveryRecord (ItemNo, Description,Condition,ImageLink,SellingPrice,AmountPaid INTEGER,Location,Customer,SoldDate,Comment,Status,Dispatch,DispatchDate,SortedBy)";
//    ps = con.prepareStatement(sql);
//    ps.execute();
//
//    sql ="CREATE TABLE IF NOT EXISTS SystemService (ResetTime, CurrentTime,LocalQuery)";
//    ps = con.prepareStatement(sql);
//    ps.execute();
//
//    sql = "CREATE TABLE IF NOT EXISTS Delivery (Customer UNIQUE ON CONFLICT REPLACE,Name,Phone,Address, Items,Availability,Status,Sorted DEFAULT KELVIN,Comment,Dispatch,DispatchDate, SortedBy,Region)";
//    ps = con.prepareStatement(sql);
//    ps.execute();
//
//    sql = "CREATE TABLE IF NOT EXISTS DeliveryRequest (Customer UNIQUE ON CONFLICT REPLACE,Name,Phone,Address, Items,Availability,Status,Sorted DEFAULT KELVIN,Comment,Dispatch,DispatchDate, SortedBy,Region)";
//    ps = con.prepareStatement(sql);
//    ps.execute();
//
//    sql="CREATE TABLE IF NOT EXISTS RemovedInventory(ItemNo,ItemDescription,Quantity,Location,Category,ImageLink,Status,DateAdded,AddedBy,DateRemoved,RemovedBy,Staff,Customer,SortedBy)";
//    ps = con.prepareStatement(sql);
//    ps.execute();
//
//    sql = "SELECT Version FROM PendingQuery";
//    ps =  con.prepareStatement(sql);
//    ResultSet rs = ps.executeQuery();
//    if(!rs.next()){
//        sql = "INSERT INTO PendingQuery (Version, query, Command) VALUES (?,?,?)";
//        ps = con.prepareStatement(sql);
//        ps.setString(1, "0" );
//        ps.setString(2, "0");
//        ps.setString(3, "");
//        ps.execute();
//    }
//    rs.close();
//


//    String[] categorys = {"BEDDING","KITCHEN","HOME", "LIGHT","FAN", "OTHER"};
//
//    try {
//        for(String category : categorys){
//            sql = "INSERT INTO Category (Name) VALUES (?)";
//            ps = con.prepareStatement(sql);
//            ps.setString(1, category );
//            ps.execute();
//        }
//    } catch (SQLException ignored) {
//
//    }
//    rs.close();
    con.close();



    System.out.println("Log File Already Exists");
}

private void CheckTrial() throws Exception {

        File activation = new File(System.getProperty("user.home") + "/AppData/Local/Activation.txt");

        if (activation.createNewFile()) {
            writer = new FileWriter(activation);
            writer.write("Expiration Date: "+expirationdate.format(format));
            writer.close();

        }

//        Iterator var6 = alllines.iterator();
        List<String> activate = Files.readAllLines(activation.toPath());

        for (String line : activate) {
            if (line.contains("Expiration")) {
                expirationdate = LocalDate.parse(line.replace("Expiration Date: ", "").trim(), format);

            }
            System.out.println("Expiration "+expirationdate);
            return;
        }



        AuthView = new Stage();
        FXMLLoader Loader = new FXMLLoader();
        Parent root =(Parent) Loader.load(getClass().getResource("/Main/MainPanes/Expired.fxml").openStream());
        cene = new Scene(root);
        AuthView.setScene(cene);
        AuthView.centerOnScreen();
        AuthView.showAndWait();
        Stage window = (Stage) Bar.getScene().getWindow();
        window.close();


}

    public void Done(Stage stage) throws Exception {
        LocalDate localdate = LocalDate.now();
        FXMLLoader Loader;
        Parent root;
        if(localdate.isAfter(expirationdate)){
            System.out.println("Starting Expired");

                AuthView = new Stage();
                Loader = new FXMLLoader();
                root = Loader.load(getClass().getResource("/MainPanes/Expired.fxml").openStream());
                cene = new Scene(root);
                AuthView.setScene(cene);
                AuthView.centerOnScreen();
                AuthView.showAndWait();
            stage.close();
            Stage window = (Stage) Bar.getScene().getWindow();
            window.close();
    } else {
            try {
//                System.out.println("Starting undone1");
                AuthView = new Stage();
                Loader = new FXMLLoader();
                root = (Parent) Loader.load(getClass().getResource("/MainPanes/Login.fxml").openStream());
                Loader.getController();
                cene = new Scene(root);
                AuthView.setScene(cene);
                AuthView.centerOnScreen();
                AuthView.show();
                stage.close();

                Stage window = (Stage) Bar.getScene().getWindow();
                window.close();
            } catch (Exception e) {
                try {
                    StringBuilder writer = new StringBuilder();
                    Files.write(MainClass.logFile.toPath(), (writer.append("\n").append(ClassName).append(e.getMessage()).append("  ").append(e.getLocalizedMessage()).toString()).getBytes(), StandardOpenOption.APPEND);
                    if(MainClass.isDebugging){
                        Erroralert.show();
                    }
                } catch (Exception ioException) {
                    ioException.printStackTrace();
                }

            }
        }

    }

    public void mouseEvent(MouseEvent mouseEvent) {
    }


    }

