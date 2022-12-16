package Controllers.MiniControllers;

import Classes.MainClass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CheckoutController implements Initializable {

    public AnchorPane rootPane;
    public JFXButton CheckAll;
    public JFXButton Check;
    public Label Room;
    public Label Name;
    public JFXComboBox<String> GuestBox;

    ObservableList<String> Data = FXCollections.observableArrayList();
    @FXML
    int Rm = 0;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootPane.getStylesheets().remove(0);
        rootPane.getStylesheets().add("file:/"+System.getProperty("user.home").replace("\\", "/" ) + "/MainStyle.css");
    }

    public void actionEvent(ActionEvent event) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
       if(event.getSource().equals(CheckAll)){
           alert.setHeaderText(null);
           alert.setContentText("Check Out All ?");
           alert.setOnCloseRequest(event1 -> {

           });
           alert.showAndWait();
           ButtonType result2 = alert.getResult();
           if(result2.equals(ButtonType.OK)){
               for(String s : Data){
                   if(MainClass.setCheckOut(s, Rm)){
                       alert.setHeaderText(null);
                       alert.setContentText("CheckOut Successful...Thanks for Your Patronage");
                       alert.showAndWait();
                       Stage window = (Stage) Check.getScene().getWindow();
                       window.close();
                   }
               }
           }else {
               alert.close();
               return;
           }
       }
       if(event.getSource().equals(Check)){
           alert.setHeaderText(null);
           alert.setContentText("Check Out "+GuestBox.getSelectionModel().getSelectedItem()+" ? ");
           alert.setOnCloseRequest(event1 -> {

           });
           alert.showAndWait();
           ButtonType result2 = alert.getResult();
           if(result2.equals(ButtonType.OK)){
               if(MainClass.setCheckOut(GuestBox.getSelectionModel().getSelectedItem(), Rm)){
                   alert.setHeaderText(null);
                   alert.setContentText("CheckOut Successful...Thanks for Your Patronage");
                   alert.showAndWait();
                   Stage window = (Stage) Check.getScene().getWindow();
                   window.close();
               }
           }else {
               alert.close();
               return;
           }
       }
       if(event.getSource().equals(GuestBox)){
           String name = GuestBox.getSelectionModel().getSelectedItem();
           Name.setText(name);
       }

    }

    public void getRoom(String text) throws Exception{
        Room.setText(text);
        Rm = Integer.parseInt(text);
        Data = MainClass.getObservableList("SELECT Name FROM Occupants WHERE Room = "+text+" AND CheckedOutDate = 'NO' ", new String[]{"Name"});
        GuestBox.setItems(Data);
        GuestBox.getSelectionModel().select(0);
        Name.setText(Data.get(0));
    }

}

