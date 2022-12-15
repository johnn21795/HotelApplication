package Controllers;

import Classes.MainClass;
import Controllers.MiniControllers.ReceiptController;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class CheckInController implements Initializable {
    public JFXTextField FName;
    public JFXTextField LName;
    public JFXTextField Phone;
    public JFXTextField Address;
    public RadioButton Male;
    public RadioButton Female;
    public JFXCheckBox isAdult;
    public JFXComboBox<String> OccupantBox;
    public JFXButton SaveBut;
    public Label TotOccupants;
    public JFXButton AddPersonBut;
    public JFXTextField RoomNo;
    public Label Days;
    public Label RmNo;
    public Label RmName;
    public Label RmType;
    public Label Rate;
    public Label Status;
    public JFXDatePicker DPicker;
    public Label ChDate;
    public Label ChTime;
    public JFXTimePicker TPicker;
    public Label TAmount;
    public Label TPaid;
    public Label Via;
    public Label TBalance;
    public JFXTextField Deposit;
    public JFXTextField Method;
    public JFXButton PayBut;
    public JFXButton ChkBut;
    public JFXButton RstBut;
    public Label ReceiptNo;

    static int Receipt = 1;

    public static JFXTextField RoomNo2;


    ObservableList<Map<String, String>> People = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {

            clearUI();
            Male.selectedProperty().set(!Female.isSelected());
            RoomNo2 = RoomNo;
            //Get Receipt Number

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setRoom(int Room)throws Exception{
        RoomNo2.setText(String.valueOf(Room));
    }


    public void actionEvent(ActionEvent actionEvent) throws Exception {
        if(actionEvent.getSource().equals(DPicker)){
            LocalDate date = DPicker.getValue();
            LocalDate subDate =date;
            if(subDate.isBefore(LocalDate.now())){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setContentText("CheckOut Date Cannot be Before Today's Date");
                alert.showAndWait();
                return;
            }
            int Days = 0;
            while(!subDate.isEqual(LocalDate.now())){
                subDate = subDate.minusDays(1);
                Days++;
            }
            Days = Math.max(Days, 1);
            int Rate = Integer.parseInt(this.Rate.getText());
            int TotalAmount = Rate * Days;

            ChDate.setText(MainClass.returnDate3Format(date));
            this.Days.setText(String.valueOf(Days));
            this.TAmount.setText(String.valueOf(TotalAmount));
            this.TPaid.setText("0");
            this.TBalance.setText(String.valueOf(TotalAmount));

        }
        if(actionEvent.getSource().equals(TPicker)){
            LocalTime time = TPicker.getValue();
            TPicker._24HourViewProperty().set(true);
            ChTime.setText(time.format(MainClass.timeFormatter));
        }
        if(actionEvent.getSource().equals(Male)){
            Female.selectedProperty().set(!Male.isSelected());
        }
        if(actionEvent.getSource().equals(Female)){
            Male.selectedProperty().set(!Female.isSelected());
        }
        if(actionEvent.getSource().equals(SaveBut)){
            if(SaveBut.getText().equalsIgnoreCase("Save")){
                Map<String,String> person = new HashMap<>();
                person.put("FName", FName.getText());
                person.put("LName", LName.getText());
                person.put("Phone", Phone.getText());
                person.put("Address", Address.getText());
                person.put("Gender", Male.isSelected()? "Male" : "Female");
                person.put("isAdult", isAdult.isSelected()? "Adult": "Child");
                People.add(person);
                ObservableList<String> persons = FXCollections.observableArrayList();
                People.forEach((M)-> persons.add(M.get("FName")+" "+M.get("LName")));
                OccupantBox.setItems(persons);
                TotOccupants.setText(String.valueOf(People.size()));
                OccupantBox.setVisible(People.size() > 1);
                FName.setDisable(true);
                LName.setDisable(true);
                Phone.setDisable(true);
                Address.setDisable(true);
                SaveBut.setText("Edit");
            } else if (SaveBut.getText().equalsIgnoreCase("Update")) {
                Map<String,String> person = new HashMap<>();
                person.put("FName", FName.getText());
                person.put("LName", LName.getText());
                person.put("Phone", Phone.getText());
                person.put("Address", Address.getText());
                person.put("Gender", Male.isSelected()? "Male" : "Female");
                person.put("isAdult", isAdult.isSelected()? "Adult": "Child");
                int Selected = 0;
                try{
                    Selected = OccupantBox.getSelectionModel().getSelectedIndex();
                }catch (Exception ignored){}
                People.set(Math.max(Selected, 0), person);

                FName.setDisable(true);
                LName.setDisable(true);
                Phone.setDisable(true);
                Address.setDisable(true);
                SaveBut.setText("Edit");

            } else {
                FName.setDisable(false);
                LName.setDisable(false);
                Phone.setDisable(false);
                Address.setDisable(false);
                SaveBut.setText("Update");
            }
            People.forEach(System.out::println);
            return;

        }
        if(actionEvent.getSource().equals(OccupantBox)){
            int selected = 0;
            try{
                selected = OccupantBox.getSelectionModel().getSelectedIndex();
            }catch (Exception ignored){}
            if(selected < 0){
                return;
            }
            Map<String,String> person;
            person = People.get(selected);
            FName.setText(person.get("FName"));
            LName.setText(person.get("LName"));
            Phone.setText(person.get("Phone"));
            Address.setText(person.get("Address"));
            if(Objects.equals(person.get("Gender"), "Male")){
                Male.selectedProperty().set(true);
                Female.selectedProperty().set(false);
            }else {
                Female.selectedProperty().set(true);
                Male.selectedProperty().set(false);
            }
            isAdult.selectedProperty().set(Objects.equals(person.get("isAdult"), "Adult"));

        }
        if(actionEvent.getSource().equals(AddPersonBut)){
            FName.setDisable(false);
            LName.setDisable(false);
            Phone.setDisable(false);
            Address.setDisable(false);
            FName.setText("");
            LName.setText("");
            Phone.setText("");
            Address.setText("");
            SaveBut.setText("Save");
        }
        if(actionEvent.getSource().equals(RstBut)){

            clearUI();
        }
        if(actionEvent.getSource().equals(PayBut)){
            if(PayBut.getText().equalsIgnoreCase("Edit Payment")){
                Deposit.setDisable(false);
                Method.setDisable(false);
                PayBut.setText("Pay");
                TPaid.setText("0");
                Deposit.setText("");
                Method.setText("");
                return;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("Confirm Payment of "+this.TPaid.getText()+" ?");
            alert.showAndWait();
            ButtonType result2 = alert.getResult();
            if(result2.equals(ButtonType.OK)){
                Deposit.setDisable(true);
                Method.setDisable(true);
                PayBut.setText("Edit Payment");
            }else {
                alert.close();

            }

        }

    }

    public void keyEvent(KeyEvent keyEvent) throws Exception{
        if(keyEvent.getSource().equals(RoomNo)){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            RmNo.setText(RoomNo.getText());
            if(RoomNo.getText().isEmpty()){
                Days.setText("0");
                RmName.setText("");
                RmNo.setText("");
                RmType.setText("");
                Status.setText("");
                Rate.setText("");
                ChDate.setText("");
                ChTime.setText("");
                this.TAmount.setText("0");
                this.TPaid.setText("0");
                this.TBalance.setText("0");
                return;
            }
            try {
                int  x = Integer.parseInt(RoomNo.getText());
                ObservableList<String> Result = MainClass.getObservableList("SELECT Name,Type,Rate,Status FROM RoomList WHERE Number = "+x+"", new String[]{"Name","Type","Rate","Status"});
                if(!Result.isEmpty()){
                    RmName.setText(Result.get(0));
                    RmType.setText(Result.get(1));
                    Rate.setText(Result.get(2));
                    Status.setText(Result.get(3));
                    Color fill = Color.rgb(200,0,0);
                    switch (Result.get(3)){
                        case "Available":
                            fill = Color.rgb(0,150,0);
                            PayBut.setDisable(false);
                            ChkBut.setDisable(false);
                            break;
                        case "Occupied":
                            fill = Color.rgb(200,0,0);
                            PayBut.setDisable(true);
                            ChkBut.setDisable(true);
                            break;
                        case "Reserved":
                            fill = Color.rgb(200,150,0);
                            break;
                    }
                    Status.setTextFill(fill);
                }else{
                    RmName.setText("");
                    RmType.setText("");
                    Rate.setText("");
                    Status.setText("");
                }
            } catch (NumberFormatException e) {
                alert.setHeaderText(null);
                alert.setContentText("Only Number Allowed");
                alert.showAndWait();
            }


        }
        if(keyEvent.getSource().equals(Deposit)){
            if(Deposit.getText().isEmpty()){
                return;
            }
            if(TAmount.getText().isEmpty() || TAmount.getText().equalsIgnoreCase("")){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setContentText("Select CheckOut Date First");
                alert.showAndWait();
                return;
            }
            int Total = Integer.parseInt(this.TAmount.getText());
            int Paid = 0;


            try {
                Paid = Integer.parseInt(Deposit.getText());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setContentText("Only Number Allowed");
                alert.showAndWait();
                return;
            }
            int Balance = Total - Paid;
            if(Balance < 0){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setContentText("Excess Amount Entered");
                alert.showAndWait();
                Paid = 0;
                Balance = Total - Paid;
                this.TPaid.setText(String.valueOf(Paid));
                this.Deposit.setText(String.valueOf(Paid));
                this.TBalance.setText(String.valueOf(Balance));
                return;
            }
            this.TPaid.setText(String.valueOf(Paid));
            this.TBalance.setText(String.valueOf(Balance));
        }
        if(keyEvent.getSource().equals(Method)) {
            this.Via.setText(MainClass.returnTitleCase(Method.getText()));
        }
    }

    public void clearUI() throws Exception {
        Receipt = MainClass.getReceipt();
        People.clear();
        FName.setText("");
        LName.setText("");
        Phone.setText("");
        Address.setText("");
        Male.selectedProperty().set(true);
        Female.selectedProperty().set(false);
        Deposit.setDisable(false);
        Method.setDisable(false);
        FName.setDisable(false);
        LName.setDisable(false);
        Phone.setDisable(false);
        Address.setDisable(false);
        OccupantBox.setItems(null);
        OccupantBox.setVisible(false);
        TotOccupants.setText("0");
        SaveBut.setText("Save");
        RoomNo.setText("");
        Days.setText("0");
        RmName.setText("");
        RmNo.setText("");
        RmType.setText("");
        Status.setText("");
        Rate.setText("");
        ChDate.setText("");
        ChTime.setText("");
        TAmount.setText("");
        TPaid.setText("");
        Via.setText("");
        TBalance.setText("");
        Deposit.setText("");
        Method.setText("");
        ReceiptNo.setText(String.valueOf(Receipt));

    }

    public void checkIn() {
        if(OccupantBox.isVisible()){
            OccupantBox.getSelectionModel().select(0);
        }
        Alert alert = new Alert(Alert.AlertType.WARNING);
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
        if(Objects.equals(TotOccupants.getText(), "0")){
            alert.setContentText("Save Guest First");
            alert.setHeaderText(null);
            alert.showAndWait();
            SaveBut.requestFocus();
            return;
        }
        if(Objects.equals(TPaid.getText(), "0")){
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Customer has not Made Payment Continue");
            alert.setHeaderText(null);
            alert.showAndWait();
            ButtonType result2 = alert.getResult();
            if(result2.equals(ButtonType.OK)){

            }else {
                alert.close();
                return;
            }

        }
        if(Objects.equals(Rate.getText(), "")){
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Enter a Valid Room No");
            alert.showAndWait();
            return;
        }
        if(Objects.equals(ChDate.getText(), "")){
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Select a Check-Out Date");
            alert.showAndWait();
            ButtonType result2 = alert.getResult();
            if(result2.equals(ButtonType.OK)){

            }else {
                alert.close();
                return;
            }
        }
        if(Objects.equals(ChTime.getText(), "")){
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Select a Check-Out Time");
            alert.showAndWait();
            return;
        }


            try {
                Stage stage = new Stage();
                FXMLLoader Loaders = new FXMLLoader();
                Parent root1 = Loaders.load(getClass().getResource("../SubPanes/Receipt.fxml").openStream());
                ReceiptController receiptController;
                receiptController = Loaders.getController();
                int invoiceNo = Integer.parseInt(ReceiptNo.getText());
                ObservableList<String> Data = FXCollections.observableArrayList();
                //Receipt, CheckInDate,CheckInTime,Name,LastName,
                //Phone,Room,Occupants,Days,TotalAmount,
                //TotalPaid,Balance,CheckOutDate,CheckoutTime,PayMethod,
                //Address,isAdult,Gender,Rate,Method
                Data.addAll(
                        String.valueOf(invoiceNo),MainClass.returnDate3Format(LocalDate.now()), LocalTime.now().format(MainClass.timeFormatter), FName.getText().trim(), LName.getText().trim(),
                        Phone.getText(), RoomNo.getText(), TotOccupants.getText(), Days.getText(), TAmount.getText(),
                        TPaid.getText(), TBalance.getText(), DPicker.getValue().format(MainClass.DatabaseDateFormat), ChTime.getText(), Via.getText(),
                        Address.getText(), isAdult.isSelected()? "Adult" : "Child", Male.isSelected()? "Male" : "Female",Rate.getText()
                );
                receiptController.getData(Data, People);

                Scene scene = new Scene(root1);
                stage.setScene(scene);
                stage.initStyle(StageStyle.UTILITY);
                stage.showAndWait();
                if(MainClass.clearCheckInUI ){
                    clearUI();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
    }
}
