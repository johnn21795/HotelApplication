/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;


import Classes.MainClass;
import Classes.ModelClassLarge;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class MainPanelController implements Initializable {

    public AnchorPane RecordsPane;
    public Label versionLabel;
    public JFXButton OperatorID;
    public JFXButton ImgButton;
    public JFXButton NotificationButton;
    public JFXButton SettingsButton;
    public JFXButton RecordsButton;
    public AnchorPane CheckInPane;

    public BorderPane rootPane;

//    public AnchorPane ServicesPane;
    public AnchorPane CheckOutPane;
    @FXML   private BorderPane root;
    @FXML   private AnchorPane HomePane;
    @FXML   private AnchorPane RoomListPane;
//    @FXML   private AnchorPane ReportPane;
    @FXML   private AnchorPane SettingsPane;
    @FXML 	private Label MainTime;
    @FXML	private Label MainDate;
    @FXML	private JFXButton HomeButton;
    @FXML	private JFXButton RoomListButton;
    @FXML	private JFXButton CheckInButton;
//    @FXML	private JFXButton ServicesButton;
    @FXML	private JFXButton SignOutButton;

    StringBuilder notificationBuilder = new StringBuilder();

    Alert alert;
    static String currentStyle = "file:/"+System.getProperty("user.home").replace("\\", "/" ) + "/currentTheme.css";
    static File currentStyleFile = new File(System.getProperty("user.home") + "/currentTheme.css");
    /**
     * Initializes the controller class.
     */
    ObservableList<AnchorPane> Mpanes;

    public Service<Void> Timeservice;

    String MyTime;
    public static String Staff = "";
    static String FileLocation = "C:\\Users\\Public\\Louisiana\\background.jpg";
    static String OriginalLocation= "";


    public static boolean checkIn = false;
    public static boolean checkOut = false;

    Splashcontroller splashcontroller = new Splashcontroller();
    @FXML CheckOutController checkOutPaneController;
    @FXML CheckInController checkInPaneController;
    @FXML RoomListController roomListPaneController;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            rootPane.getStylesheets().remove(0);
            rootPane.getStylesheets().add("file:/"+System.getProperty("user.home").replace("\\", "/" ) + "/currentTheme.css");
            try {
                myChildren();


                //TODO
                try {
                    File copiedFile = new File(FileLocation);
                    File file = new File(System.getProperty("user.home") + "/Receipt.xlsx");
                    File file2 = new File(System.getProperty("user.home") + "/MyReceipt.xlsx");
                    File file3 = new File(System.getProperty("user.home") + "/currentTheme.css");
                    if(!file.exists()){
                        InputStream in = getClass().getClassLoader().getResourceAsStream("Raw/Receipt.xlsx");
                        InputStream in2 = getClass().getClassLoader().getResourceAsStream("Raw/MyReceipt.xlsx");
                        InputStream in3 = getClass().getClassLoader().getResourceAsStream("Raw/currentTheme.css");
                        InputStream in4 = getClass().getClassLoader().getResourceAsStream("Raw/background.jpg");
                        assert in != null;
                        assert in2 != null;
                        assert in3 != null;
                        assert in4 != null;
                        Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        Files.copy(in2, file2.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        Files.copy(in3, file3.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        Files.copy(in4, copiedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        in.close();
                        in2.close();
                        in3.close();
                        in4.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Image img = new Image("file:\\"+FileLocation);
                BackgroundSize size = new BackgroundSize(1.0, 1.0, true, true, true, true );
                Background background = new Background(new BackgroundImage(img, BackgroundRepeat.NO_REPEAT , BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size));
                HomePane.setBackground(background);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Staff = "";
            File login = new File(System.getProperty("user.home") + "/AppData/Local/login.zty");
            if (!login.createNewFile()) {
                List<String> lines = Files.readAllLines(login.toPath());
                for (String line : lines) {
                    Staff = line;
                }
                OperatorID.setText(Staff);
            }

            OperatorID.setOnMouseEntered(event -> OperatorID.setText("Log Out"));
            OperatorID.setOnMouseExited(event -> OperatorID.setText(Staff));
            MainDate.setText(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
            TimeService();
            Mpanes = FXCollections.observableArrayList(HomePane, RoomListPane, CheckInPane,  CheckOutPane, RecordsPane, SettingsPane );
            Mpanes.forEach((action) -> action.setVisible(false));
            Mpanes.get(0).setVisible(true);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


  @FXML
  private void MainNavigation(ActionEvent event) throws Exception{
      if(event.getSource() == OperatorID){
          splashcontroller.Done((Stage) HomeButton.getScene().getWindow());
      }
   if(event.getSource() == HomeButton){
          MainNav(Mpanes.get(0) );
      //LoadAllrecords();
      }
    else if(event.getSource() == RoomListButton){
        MainNav(Mpanes.get(1) );
    }
   else if(event.getSource() == CheckInButton){
       MainNav(Mpanes.get(2) );
   }
//   else if(event.getSource() == ServicesButton){
//          MainNav(Mpanes.get(4) );
//   }
//   else if(event.getSource() == ServicesButton){
//       MainNav(Mpanes.get(4) );
//   }
   else if(event.getSource() == SignOutButton){
       MainNav(Mpanes.get(3) );
   }
   else if(event.getSource() == RecordsButton){
       MainNav(Mpanes.get(4) );
   }
   else if(event.getSource() == SettingsButton){
       HomePane.setVisible(false);
       SettingsPane.setVisible(true);
       MainNav(Mpanes.get(5) );
   }
   else if(event.getSource() == OperatorID){
       splashcontroller.Done((Stage) HomeButton.getScene().getWindow());
   }
   else if (event.getSource() == ImgButton) {
       FileChooser chooser = new FileChooser();
       chooser.setTitle("Select File");
       File file = chooser.showOpenDialog(this.ImgButton.getScene().getWindow());
       OriginalLocation = file.getAbsolutePath();
       File originalFile = new File(OriginalLocation);
       File copiedFile = new File(FileLocation);
       copiedFile.mkdirs();

       Files.copy(originalFile.toPath(), copiedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
       FileLocation = copiedFile.getAbsolutePath();
       Image img = new Image("file:\\"+FileLocation);
       BackgroundSize size = new BackgroundSize(1.0, 1.0, true, true, true, true );
       Background background = new Background(new BackgroundImage(img, BackgroundRepeat.NO_REPEAT , BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size));
       HomePane.setBackground(background);
   }else if(event.getSource().equals(NotificationButton)){

       Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
       alert.setTitle("Check Outs");
       alert.setContentText(notificationBuilder.toString());
       alert.setHeaderText(null);
       alert.showAndWait();
       ButtonType result2 = alert.getResult();
       if(result2.equals(ButtonType.OK)){
           notificationBuilder = new StringBuilder();
           NotificationButton.setStyle("-fx-background-color: linear-gradient(#901500, #490b02); -fx-text-fill: white; -fx-font-weight: bold;");
           NotificationButton.setText("0 Notifications");
       }
   }


  }

    public void MainNav(AnchorPane pane) {

        Mpanes.forEach((action) -> action.setVisible(false));
        pane.setVisible(true);

    }

	public void TimeService() {
        Timeservice = new Service<Void>() {
            @Override
            public Task<Void> createTask() {
               return new Task<Void>(){
                   @Override
                   public Void call() throws Exception {
                   MyTime = (""+ LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)));
                   while(true){
                       updateMessage(""+LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)));
                       if(checkIn){
                           break;
                       }
                       if(checkOut){
                           break;
                       }
                       if(LocalTime.now().getMinute() == 55){
                           if(LocalTime.now().getSecond() < 3){
                               break;
                           }
                       }
                       Thread.sleep(1000);
                   }
                   return null;
                   }
            };   
        }
    
    };
     Timeservice.setOnSucceeded(event -> {
         try {
             if(checkIn){
                 MainNav(Mpanes.get(2));
                 checkIn =false;
             }
             NotificationService();
             TimeService();
         } catch (Exception e) {
             throw new RuntimeException(e);
         }
     });
     MainTime.textProperty().bind(Timeservice.messageProperty());
            Timeservice.restart();
             
    }

    @FXML private void myChildren()throws Exception{
        checkOutPaneController.myParent(this);
        checkInPaneController.myParent(this);
        roomListPaneController.myParent(this);

    }

    public void NotificationService() throws  Exception{
        String sql = "SELECT Name,Room,ToCheckOutTime FROM (SELECT * FROM Occupants WHERE CheckedOutDate = 'NO') WHERE ToCheckOutTime between '"+LocalTime.now()+"' and '"+LocalTime.now().plusMinutes(11)+"'";
        ObservableList<ModelClassLarge> Data = MainClass.FillTableLarge(3, sql);
        notificationBuilder = new StringBuilder();
        if(!Data.isEmpty()){
            for(ModelClassLarge M : Data){
                notificationBuilder.append(M.getCol1()).append(" From Room ").append(M.getCol2()).append(" Should be Checking By ").append(M.getCol3()).append("\n");
            }
            NotificationButton.setStyle("-fx-background-color: linear-gradient(#2efb2e, #076b07  ); -fx-text-fill: white; -fx-font-weight: bold;");
            NotificationButton.setText(Data.size()+" Notifications");
        }else {
            NotificationButton.setStyle("-fx-background-color: linear-gradient(#901500, #490b02); -fx-text-fill: white; -fx-font-weight: bold;");
            NotificationButton.setText("0 Notifications");
        }

    }


    public void mouseEvent(MouseEvent event) throws Exception {
        alert = new Alert(Alert.AlertType.NONE);
        File version = new File(System.getProperty("user.home") + "/AppData/Local/Activation.txt");
        if(event.getSource().equals(versionLabel)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New Version 1.1.6");
            alert.setContentText("Update Complete! \n " +
                    "What's New ? \n " +
                    "1. Added Notifications (Under Development) \n " +
                    "2. Added Card Auditing \n " +
                    "3. Cards will now be marked as 'Inactive' if no payment in 1 month \n" +
                    "4. All Cards will be due for auditing every month \n" +
                    "5. Updated Customers Section \n" +
                    "6. Cards can now be Marked as 'Away' if Customer is not available for a month \n" +
                    "7. Passwords are now required from operators (Every Session) for record purposes \n" +
                    "Click on the Version number(Top right corner) to view this message again");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    public void LoadTheme(String name) throws Exception{
        ObservableList<String> theme = MainClass.getObservableList("SELECT Data FROM Themes WHERE Name = '"+name+"' ", new String[]{"Data"});

        FileWriter fileWriter = new FileWriter(currentStyleFile.getAbsoluteFile());
        fileWriter.write(theme.get(0));
        fileWriter.close();
        rootPane.getStylesheets().remove(0);
        rootPane.getStylesheets().add(currentStyle);
    }

    public void setCheckOutUI(String name, int room) throws Exception {
        MainNav(Mpanes.get(3));
        checkOutPaneController.NameBox.getEditor().setText(name);
        checkOutPaneController.loadUI(name, room);
    }
    public void setRoom(int room) throws Exception {
        MainNav(Mpanes.get(2));
        checkInPaneController.RoomNo.setText(String.valueOf(room));
        checkInPaneController.getRoomDetails();
    }


//    public ObservableList<File> listfiles(File file)throws Exception{
//        ObservableList<File[]> files = FXCollections.observableArrayList();
//        if(file.isDirectory()){
//            files.add(file.listFiles());
//        }
//
//    }


    
}
