package Controllers.MiniControllers;

import Classes.MainClass;
import Controllers.RecordsController;
import Controllers.RoomListController;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

public class ReceiptController implements Initializable {
    public AnchorPane rootPane;
    public Label Date;
    public Label Time;
    public Label Name;
    public Label Phone;
    public Label Room;
    public Label People;
    public Label Days;
    public Label Amount;
    public Label Paid;
    public Label Balance;
    public JFXButton PrintBut;
    public Label RNo;
    public Label CheckOut;
    public JFXButton SaveBut;

    ObservableList<String> MainData = FXCollections.observableArrayList();
    ObservableList<Map<String, String>> Occupants = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Init");
        rootPane.getStylesheets().remove(0);
        rootPane.getStylesheets().add("file:/"+System.getProperty("user.home").replace("\\", "/" ) + "/currentTheme.css");



    }

    private void LoadUI() {
        System.out.println("Load UI");
    }

    public void actionEvent(ActionEvent event) {

    }

    public void getData(ObservableList<String> data, ObservableList<Map<String, String>> people) {
        System.out.println("Get Data");
        MainData = data;
        Occupants = people;
        this.RNo.setText(MainData.get(0));
        this.Date.setText(MainData.get(1));
        this.Time.setText(MainData.get(2));
        this.Name.setText(MainClass.returnTitleCase(MainData.get(3)+" "+MainClass.returnTitleCase(MainData.get(4))));
        this.Phone.setText(MainData.get(5));
        this.Room.setText(MainData.get(6));
        this.People.setText(MainData.get(7));
        this.Days.setText(MainData.get(8));
        this.Amount.setText(MainData.get(9));
        this.Paid.setText(MainData.get(10));
        this.Balance.setText(MainData.get(11));
        this.CheckOut.setText(MainClass.returnDate3Format(LocalDate.parse(MainData.get(12), MainClass.DatabaseDateFormat))+"  "+ MainData.get(13));
    }

    public void saveReceipt() throws Exception {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Saving Receipt Please Wait");
        alert.show();

        //Check-Ins
        ObservableList<String> Data = FXCollections.observableArrayList();
//        Date,Receipt,Name,Phone,Address,
//        isAdult,Gender,Room,CheckInDate,CheckInTime,
//        ToCheckOutDate,ToCheckOutTime,CheckedOutDate,CheckedOutTime,Days
        Data.addAll(
                LocalDate.now().format(MainClass.DatabaseDateFormat),RNo.getText(),Name.getText().trim(),Phone.getText(),MainData.get(15),
                MainData.get(16), MainData.get(17),Room.getText(),LocalDate.now().format(MainClass.DatabaseDateFormat),MainData.get(2),
                MainData.get(12), MainData.get(13),"NO","NO", Days.getText()
        );
       if(MainClass.InsertCheckIn(Data)){
           System.out.println("Check in Room List Successful ");
           alert.close();
           alert.setHeaderText(null);
           alert.setContentText("Check in Successful");
           alert.show();
       }

        //Receipts
         Data = FXCollections.observableArrayList();
//        Date,Name,Phone,Address,isAdult,
//        Gender,Room,CheckInDate,CheckInTime,ToCheckOutDate,
//        ToCheckOutTime,CheckedOutDate,CheckedOutTime,Occupants,Days,
//        Rate,Total,Paid,Method,Balance,
//        Receipt
        Data.addAll(
                LocalDate.now().format(MainClass.DatabaseDateFormat),Name.getText().trim(),Phone.getText(),MainData.get(15), MainData.get(16),
                MainData.get(17),Room.getText(),LocalDate.now().format(MainClass.DatabaseDateFormat),MainData.get(2), MainData.get(12),
                MainData.get(13),"NO","NO",this.People.getText(), Days.getText(),
                MainData.get(18),Amount.getText(), Paid.getText(),MainData.get(14),Balance.getText(),
                RNo.getText()

        );
        if(MainClass.InsertReceipt(Data, Occupants)){
            System.out.println("InsertReceipt Room List Successful ");
            alert.close();
            alert.setHeaderText(null);
            alert.setContentText("InsertReceipt in Successful");
            alert.show();
            MainClass.reloadRecordsTables = true;
            MainClass.reloadRoomListTables = true;
        }

        //RoomList
        Data = FXCollections.observableArrayList();
        //Days,Total,Receipt,CheckInDate,CheckInTime,
        //CheckedOutDate,CheckedOutTime,Name
//        7
        Data.addAll(
                Days.getText(), Amount.getText(),RNo.getText(),LocalDate.now().format(MainClass.DatabaseDateFormat),MainData.get(2),
                MainData.get(12), MainData.get(13),Name.getText().trim(),Room.getText()
        );
        if(MainClass.UpdateRoomList(Data)){
            System.out.println("Updating Room List Successful ");
            MainClass.clearCheckInUI = true;
            alert.close();
            alert.setHeaderText(null);
            alert.setContentText("Check-In Successful!...Have a Wonderful Stay");
            alert.showAndWait();
            Stage window = (Stage) Days.getScene().getWindow();
            window.close();
        }





    }
}
