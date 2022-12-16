package Controllers;


import Classes.ConnectDB;
import Classes.MainClass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public JFXPasswordField PassField;
    public JFXComboBox<String> StaffBox;
    public JFXButton LoginBut;
    public Label accounttech;

    ObservableList<String> Staffs = FXCollections.observableArrayList();
//    public static String url = "https://ogadiscountserver.herokuapp.com";
    public static String url = "http://localhost:3000";

    public String ClassName = "LoginController ";
    public Alert Erroralert;

    public Stage MainStage, AuthView;
    public BorderPane root;
    public Scene cene;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            root.getStylesheets().remove(0);
            root.getStylesheets().add("file:/"+System.getProperty("user.home").replace("\\", "/" ) + "/MainStyle.css");

            Connection con;
            con  =ConnectDB.Main();
            String sql = "SELECT Name FROM Staffs";
            PreparedStatement ps =  con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Staffs.add(rs.getString("Name"));
                System.out.println(rs.getString("Name"));
            }
            StaffBox.setItems(Staffs);
            ps.close();
            con.close();
            String Operator = "";
            File login = new File(System.getProperty("user.home") + "/AppData/Local/login.zty");
            if (!login.createNewFile()) {
                List<String> lines = Files.readAllLines(login.toPath());
                for (String line : lines) {
                    Operator = line;
                }
                StaffBox.getSelectionModel().select(Operator);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mouseEvent(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() > 1){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Add Staff");
            JFXTextField name = new JFXTextField();
            JFXPasswordField field = new JFXPasswordField();
            JFXPasswordField field2 = new JFXPasswordField();
            field.setPromptText("Enter Staff Password");
            field2.setPromptText("Management Password");
            name.setPromptText("Enter Staff Name");
            name.setLabelFloat(true);
            Button button = new Button();
            button.setText("Save");
            button.setTextFill(Paint.valueOf("#003300"));
            button.setOnAction( v-> {
                try {
                    if(name.getText().isEmpty()){
                        name.requestFocus();
                        return;
                    }
                    if(field.getText().isEmpty()){
                        field.requestFocus();
                        return;
                    }
                    if(field2.getText().isEmpty()){
                        field2.requestFocus();
                        return;
                    }
                    ActivateCode(name.getText(), field.getText(), field2.getText() );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            VBox vbox = new VBox();
            vbox.getChildren().add(name);
            vbox.getChildren().add(field);
            vbox.getChildren().add(field2);
            vbox.setSpacing(10);
            vbox.getChildren().add(button);
            alert.setHeaderText(null);
            alert.getDialogPane().setContent(vbox);
            alert.setOnCloseRequest(v->{
                alert.close();
            });
            alert.getButtonTypes().add(ButtonType.CLOSE);
            alert.show();
        }
    }

    private void ActivateCode(String name, String password, String management) throws Exception {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            System.out.println("creating new Log DB");
        Connection con = ConnectDB.Main();
        String sql = "CREATE TABLE IF NOT EXISTS Staffs (Name, Password, Access)";
        PreparedStatement ps =  con.prepareStatement(sql);
        ps.execute();


             sql = "SELECT Password FROM Staffs WHERE Name = 'Management' ";
             ps =  con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();
            if(!rs.getString("Password").equalsIgnoreCase(management)){
                alert.setTitle("Incorrect");
                alert.setContentText("Management Password is Incorrect");
                alert.setHeaderText(null);
                alert.setWidth(300);
                alert.showAndWait();
                rs.close();
                con.close();
                return;
            }

            sql = "SELECT Name FROM Staffs WHERE Name = '"+name.toUpperCase()+"' ";
            ps =  con.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
                alert.setTitle("Add Staffs");
                alert.setContentText("Name Already Exists");
            }else{
                sql = "INSERT INTO Staffs (Name, Password, Access) VALUES (?,?,?)";
                ps = con.prepareStatement(sql);
                ps.setString(1, name.toUpperCase() );
                ps.setString(2, password);
                ps.setInt(3, 1);
                ps.execute();
                ps.close();

                alert.setTitle("Successful!");
                alert.setContentText(name +" Added Successfully");

            }
        alert.setHeaderText(null);
        alert.setWidth(300);
        alert.showAndWait();
        rs.close();

            Staffs.clear();
            sql = "SELECT Name FROM Staffs ";
            ps =  con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                Staffs.add(rs.getString("Name"));
            }
            StaffBox.setItems(Staffs);
            StaffBox.getSelectionModel().select(name);
            ps.close();
            con.close();
    }

    public void actionEvent(ActionEvent event) throws Exception {
        accounttech.setText("Logging In...");
        System.out.println("Logging in");
        Connection con;
        con  =ConnectDB.Main();
        String name = StaffBox.getSelectionModel().getSelectedItem();
        String pass = PassField.getText();

        final boolean[] isCorrect = {false};

        Service<Void> LoadService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {

                        String sql = "SELECT Password FROM Staffs WHERE Name = '"+name+"' ";
                        PreparedStatement ps =  con.prepareStatement(sql);
                        ResultSet rs = ps.executeQuery();
                        isCorrect[0] = rs.getString("Password").equalsIgnoreCase(pass);
                        con.close();
                        return null;
                    }
                };
            }
        };


        LoadService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
              if(isCorrect[0]){
                  try {
                      File version = new File(System.getProperty("user.home") + "/AppData/Local/login.zty");
                      if(version.exists())
                          version.delete();
                          version.createNewFile();
                          FileWriter writer = new FileWriter(version);
                          writer.write(name);
                          writer.close();

                      FXMLLoader Loader = new FXMLLoader();
                      Parent root = Loader.load(getClass().getResource("/MainPanes/MainPane.fxml").openStream());
                      Loader.getController();
                      Scene scene = new Scene(root);
                      Stage AuthView = new Stage();
                      AuthView.setScene(scene);
                      AuthView.centerOnScreen();
                      AuthView.show();
                      Stage window = (Stage) LoginBut.getScene().getWindow();
                      window.close();


                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              }else {

                  Alert alert = new Alert(Alert.AlertType.INFORMATION);
                  alert.setTitle("Incorrect");
                  alert.setContentText("Password is Incorrect");
                  alert.setHeaderText(null);
                  alert.setWidth(300);
                  alert.showAndWait();
                  accounttech.setText("Log In");
              }
            }
        });




        LoadService.restart();

    }
}
