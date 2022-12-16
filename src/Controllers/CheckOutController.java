package Controllers;

import Classes.MainClass;
import Controllers.MiniControllers.CheckoutController;
import Controllers.MiniControllers.ReceiptController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.ResourceBundle;

public class CheckOutController implements Initializable {
    public JFXComboBox<String> NameBox;
    public Label Name;
    public Label Phone;
    public Label Address;
    public Label Gender;
    public Label IsAdult;
    public Label RmNumber;
    public Label Days;
    public JFXTextField RoomNo;
    public Label RmNo;
    public Label RmName;
    public Label RmType;
    public Label Rate;
    public Label ChDate;
    public Label ChTime;
    public Label Occupants;
    public Label ChOutDate;
    public Label ChOutTime;
    public Label Receipt;
    public Label TAmount;
    public Label TPaid;
    public Label Via;
    public Label TBalance;
    public JFXTextField Deposit;
    public JFXTextField Method;
    public JFXButton PayBut;
    public JFXButton ChkBut;
    public VBox PayBox;

    ObservableList<String> NameList;
    int PrePaid = 0;
    int TotalAmount = 0;

    public void actionEvent(ActionEvent event)  {
        System.out.println("Starting Action Event "+event.getSource().toString());
        try {
            if(event.getSource().equals(NameBox)){
                NameBox.setOnAction(action->{});
                String selected = NameBox.getSelectionModel().getSelectedItem();
                if(NameBox.getSelectionModel().getSelectedIndex() < 0){
                    return;
                }
                int index;
                try {
                    index = selected.indexOf("  ");
                } catch (Exception ignored) {
                    return;
                }
                if(index < 0){
                    clearUI();
                    return;
                }

                try {
                    String Name = selected.substring(0, index).trim();
                    String Room = selected.substring(selected.indexOf(":")+1);

                    ObservableList<String> Names = FXCollections.observableArrayList();
//                    System.out.println("NameList size "+NameList.size());
                    for(String name: NameList){
                        try {
//                            System.out.println("indexx "+indexx+" name: "+name);
                            index = name.indexOf("  ");
                            Names.add(name.substring(0, index).trim());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    NameBox.setItems(null);
                    NameBox.setItems(Names);


                    NameBox.getSelectionModel().select(Name);

                    loadUI(Name.trim(), Integer.parseInt(Room));
                    NameBox.setOnAction(this::actionEvent);
                } catch (Exception ignored) {

                }

            }
        } catch (Exception ignored) {

        }
        if(event.getSource().equals(PayBut)){
            if(PayBut.getText().equalsIgnoreCase("Edit Payment")){
                Deposit.setDisable(false);
                Method.setDisable(false);
                PayBut.setText("Pay");
                Deposit.setText("");
                Method.setText("");
                return;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("Confirm Balance Payment of "+this.Deposit.getText()+" ?");
            alert.showAndWait();
            ButtonType result2 = alert.getResult();
            if(result2.equals(ButtonType.OK)){
//                (Date,Receipt,Name,Room,Time,Total,Paid,Balance,Method
                ObservableList<String> Data = FXCollections.observableArrayList();
                Data.addAll(
                        LocalDate.now().format(MainClass.DatabaseDateFormat),Receipt.getText(),Name.getText().trim(),RoomNo.getText(), LocalTime.now().format(MainClass.timeFormatter), Deposit.getText(), this.TBalance.getText(),Via.getText()

                );
                try {
                    if(MainClass.InsertPayment(Data)){
                        Deposit.setDisable(true);
                        Method.setDisable(true);
                        PayBut.setText("Edit Payment");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }else {
                alert.close();
            }

        }
    }

    public void keyEvent(KeyEvent event) throws Exception {
        if(event.getSource().equals(NameBox)){
            NameBox.setOnAction(action->{});
            String service = NameBox.getEditor().getText();
            if(NameBox.getEditor().getText().isEmpty()){
                clearUI();
                return;
            }
            service = service.charAt(0)=='0'? service.substring(1) : service;
            String sql = "SELECT Name,Phone,Room FROM Occupants WHERE CheckedOutDate = 'NO' AND Name Like '%" + service + "%' or Phone Like '%" + service + "%' ";

            if(event.getCode().isArrowKey()){
                return;
            }
            if(event.getCode().name().equalsIgnoreCase("enter")){
                NameBox.disarm();
                return;
            }
            if (NameBox.getValue().equalsIgnoreCase("")) {
                clearUI();
                NameBox.show();
                NameBox.setVisibleRowCount(6);
            } else {
                NameBox.show();
                loadServiceList(sql);
            }
            NameBox.setVisibleRowCount(6);
            NameBox.setOnAction(this::actionEvent);

        }
        if(event.getSource().equals(Deposit)){
            if(Deposit.getText().isEmpty()){
                return;
            }
            int Paid;

            try {
                Paid = Integer.parseInt(Deposit.getText());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setContentText("Only Number Allowed");
                alert.showAndWait();
                return;
            }
            int Balance = TotalAmount - (Paid+PrePaid);
            if(Balance < 0){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setContentText("Excess Amount Entered");
                alert.showAndWait();
                Paid = 0;
                Balance = TotalAmount - PrePaid;
                this.TPaid.setText(String.valueOf(PrePaid));
                this.Deposit.setText(String.valueOf(Paid));
                this.TBalance.setText(String.valueOf(Balance));
                return;
            }
            this.TPaid.setText(String.valueOf((Paid+PrePaid)));
            this.TBalance.setText(String.valueOf(Balance));
        }
        if(event.getSource().equals(Method)) {
            this.Via.setText(MainClass.returnTitleCase(Method.getText()));
        }
        if(event.getSource().equals(RoomNo)){
            if(RoomNo.getText().isEmpty()){
                clearUI();
                return;
            }

            if(event.getCode().isArrowKey()){
                return;
            }
            int room = Integer.parseInt(RoomNo.getText());

            loadUI("", room);

        }
    }

    public void loadServiceList(String sql)throws Exception{
        NameBox.disarm();
        NameBox.setVisibleRowCount(6);
        NameBox.setItems(null);
        NameList = MainClass.getCheckOut(sql, new String[]{"Name", "Room"});
        NameBox.setItems(NameList);
        NameBox.arm();
    }

    public void mouseEvent(MouseEvent mouseEvent) {
    }


    public void checkOut() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        String Name = this.Name.getText().trim();
        int room = Integer.parseInt(this.RmNo.getText());
        try {
            if(Integer.parseInt(this.Occupants.getText())>1){
                Stage stage = new Stage();
                FXMLLoader Loaders = new FXMLLoader();
                Parent root1 = Loaders.load(getClass().getResource("/SubPanes/Checkout.fxml").openStream());
                CheckoutController checkoutController;
                checkoutController = Loaders.getController();
                checkoutController.getRoom(this.RmNo.getText());
                Scene scene = new Scene(root1);
                stage.setScene(scene);
                stage.initStyle(StageStyle.UTILITY);
                stage.showAndWait();
                    if(!Deposit.getText().isEmpty()) {
                        ObservableList<String> Data = FXCollections.observableArrayList();
                        Data.addAll(
                                LocalDate.now().format(MainClass.DatabaseDateFormat), Receipt.getText(), this.Name.getText().trim(), RoomNo.getText(), LocalTime.now().format(MainClass.timeFormatter), Deposit.getText(), this.TBalance.getText(), Via.getText()
                        );
                        try {
                            if (MainClass.InsertPayment(Data)) {
                                Deposit.setDisable(true);
                                Method.setDisable(true);
                                PayBut.setText("Edit Payment");
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                clearUI();
                MainClass.reloadRecordsTables = true;
                MainClass.reloadRoomListTables = true;
            }else {
                alert.setHeaderText(null);
                alert.setContentText("Confirm Checkout of "+this.Name.getText().trim()+" ? ");
                alert.setOnCloseRequest(event1 -> {

                });
                alert.showAndWait();
                ButtonType result2 = alert.getResult();
                if(result2.equals(ButtonType.OK)){
                    if(!Deposit.getText().isEmpty()){
                        ObservableList<String> Data = FXCollections.observableArrayList();
                        Data.addAll(
                                LocalDate.now().format(MainClass.DatabaseDateFormat),Receipt.getText(),this.Name.getText().trim(),RoomNo.getText(), LocalTime.now().format(MainClass.timeFormatter), Deposit.getText(), this.TBalance.getText(),Via.getText()
                        );
                        try {
                            if(MainClass.InsertPayment(Data)){
                                Deposit.setDisable(true);
                                Method.setDisable(true);
                                PayBut.setText("Edit Payment");
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if(MainClass.setCheckOut(Name, room)){
                        alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText(null);
                        alert.setContentText("CheckOut Successful...Thanks for Your Patronage");
                        alert.showAndWait();
                        clearUI();
                        MainClass.reloadRecordsTables = true;
                        MainClass.reloadRoomListTables = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }




    }

    public void clearUI(){
        NameBox.disarm();
        NameBox.setItems(null);
        NameBox.getEditor().setText("");
        NameBox.arm();

        Name.setText("");
        Phone.setText("");
        Address.setText("");
        Gender.setText("");
        IsAdult.setText("");
        RmNumber.setText("0");
        RoomNo.setText("");
        Days.setText("0");
        RmName.setText("");
        RmNo.setText("");
        RmType.setText("");
        Rate.setText("");
        ChDate.setText("");
        ChTime.setText("");
        TAmount.setText("");
        TPaid.setText("");
        Via.setText("");
        TBalance.setText("");
        Deposit.setText("");
        Method.setText("");
        Occupants.setText("");
        ChOutDate.setText("");
        ChOutTime.setText("");
        Receipt.setText("");
        PayBox.setVisible(false);
        Deposit.setDisable(false);
        Method.setDisable(false);
        PayBut.setText("Pay");
        PrePaid = 0;
        TotalAmount = 0;

    }
    public void loadUI(String name, int Room) throws Exception {
        Map<String,String> Data = MainClass.getCheckOutData(name, Room);
        if(Data.get("Name")==null){
            return;
        }
        if(name.equalsIgnoreCase("")){
            System.out.println("Room:  "+Room+" Name: "+name+" Receipt: "+Data.get("Receipt"));
            NameList = MainClass.getCheckOut("SELECT Name,Room FROM Occupants WHERE Room = "+Room+" AND Receipt = "+Data.get("Receipt")+"", new String[]{"Name", "Room"});
            System.out.println(NameList);
            System.out.println("NameList:  "+NameList.size());
            NameBox.setOnAction(action->{});
            NameBox.setItems(null);
            NameBox.setItems(NameList);
            NameBox.getEditor().setText(NameList.get(0).substring(0, NameList.get(0).indexOf("  ")).trim());
            NameBox.setOnAction(this::actionEvent);
        }
        Name.setText(Data.get("Name"));
        Phone.setText(Data.get("Phone"));
        Address.setText(Data.get("Address"));
        Gender.setText(Data.get("Gender"));
        IsAdult.setText(Data.get("isAdult"));
        RmNumber.setText(Data.get("Room"));
        Days.setText(Data.get("Days"));
        RmName.setText(Data.get("RoomName"));
        RmNo.setText(Data.get("Room"));
        RoomNo.setText(Data.get("Room"));
        RmType.setText(Data.get("Type"));
        Rate.setText(Data.get("Rate"));
        ChDate.setText(MainClass.returnDate3Format(LocalDate.parse(Data.get("CheckInDate"), MainClass.DatabaseDateFormat)) );
        ChTime.setText(Data.get("CheckInTime"));
        TAmount.setText(Data.get("Total"));
        TPaid.setText(Data.get("Paid"));
        PrePaid = Integer.parseInt(Data.get("Paid"));
        TotalAmount = Integer.parseInt(Data.get("Total"));
        Via.setText(Data.get("Method"));
        TBalance.setText(Data.get("Balance"));
        Deposit.setText("");
        Method.setText("");
        Occupants.setText(Data.get("Occupants"));
        LocalDate chkout = LocalDate.parse(Data.get("ToCheckOutDate"), MainClass.DatabaseDateFormat);
        LocalTime time = LocalTime.parse(Data.get("ToCheckOutTime"), MainClass.timeFormatter);
        if(chkout.isAfter(LocalDate.now())){
            ChOutDate.setTextFill(Color.GREEN);ChOutTime.setTextFill(Color.GREEN);
        }else {
            ChOutDate.setTextFill(Color.rgb(200,0,0));ChOutTime.setTextFill(Color.rgb(200,0,0));
        }
        if(chkout.isEqual(LocalDate.now())){
            ChOutDate.setTextFill(Color.GREEN);
            ChOutTime.setTextFill(time.isAfter(LocalTime.now())? Color.GREEN :Color.rgb(200,0,0));
        }

        ChOutDate.setText(MainClass.returnDate3Format(chkout));
        ChOutTime.setText(Data.get("ToCheckOutTime"));
        Receipt.setText(Data.get("Receipt"));
        PayBox.setVisible(Integer.parseInt(Data.get("Balance")) > 0);
        if(Integer.parseInt(Data.get("Balance")) > 0){
            Deposit.requestFocus();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clearUI();
    }
}
