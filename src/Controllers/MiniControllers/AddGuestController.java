package Controllers.MiniControllers;

import Classes.MainClass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AddGuestController implements Initializable {
    public AnchorPane rootPane;
    public Label Date;
    public Label Time;
    public JFXTextField FName;
    public JFXTextField LName;
    public JFXTextField Phone;
    public JFXTextField Address;
    public RadioButton Male;
    public RadioButton Female;
    public JFXCheckBox isAdult;
    public JFXButton SaveBut;
    public Label Room;

    ObservableList<String> MainData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Male.selectedProperty().set(!Female.isSelected());
        Date.setText(LocalDate.now().format(MainClass.format3));
        Time.setText(LocalTime.now().format(MainClass.timeFormatter));
        rootPane.getStylesheets().remove(0);
        rootPane.getStylesheets().add("file:/"+System.getProperty("user.home").replace("\\", "/" ) + "/currentTheme.css");
    }

    public void getInfo(ObservableList<String> Data) throws Exception{
        MainData = Data;
        Room.setText(MainData.get(0));
    }

    public void actionEvent(ActionEvent event) throws Exception {
        if(event.getSource().equals(Male)){
            Female.selectedProperty().set(!Male.isSelected());
        }
        if(event.getSource().equals(Female)){
            Male.selectedProperty().set(!Female.isSelected());
        }
        if(event.getSource().equals(SaveBut)){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            if(FName.getText().isEmpty()){
                alert.setContentText("First Name Cannot Be Empty");
                alert.setHeaderText(null);
                alert.showAndWait();
                FName.requestFocus();
                return;
            }
            if(Phone.getText().isEmpty()){
                alert.setContentText("Phone No is Required");
                alert.setHeaderText(null);
                alert.showAndWait();
                Phone.requestFocus();
                return;
            }

            ObservableList<String> Data = FXCollections.observableArrayList();
            Data.clear();
//            Date,Name,Phone,Address,IsAdult,
//            Gender,Room(0),CheckInDate,CheckInTime,ToCheckOutDate(1),
//            ToCheckOutTime(2),CheckedOutDate,CheckedOutTime,Days,Receipt(3)
            Data.addAll(
                    LocalDate.now().format(MainClass.DatabaseDateFormat),FName.getText().trim(), LName.getText().trim(),Phone.getText(), Address.getText(),  isAdult.isSelected()? "Adult" : "Child",
                    Male.isSelected()? "Male" : "Female", MainData.get(0),   LocalDate.now().format(MainClass.DatabaseDateFormat), LocalTime.now().format(MainClass.timeFormatter), MainData.get(1),
                    MainData.get(2),"NO","NO","1",MainData.get(3)
            );
            if(MainClass.addGuest(Data)){
                alert.setHeaderText(null);
                alert.setContentText("Check-In Successful!...Have a Wonderful Stay");
                alert.showAndWait();
                Stage window = (Stage) Male.getScene().getWindow();
                window.close();
            }

        }
    }
}
