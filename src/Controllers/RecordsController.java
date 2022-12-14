package Controllers;

import Classes.MainClass;
import Classes.ModelClassLarge;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class RecordsController implements Initializable {
    public TableView<ModelClassLarge> RoomTable;
    public TableColumn<ModelClassLarge, ?> NoCol4;
    public TableColumn<ModelClassLarge, ?> RoomCol4;
    public TableColumn<ModelClassLarge, ?> StatusCol4;
    public TableColumn<ModelClassLarge, ?> TimesCol4;
    public TableColumn<ModelClassLarge, ?> TotalDaysCol;
    public TableColumn<ModelClassLarge, ?> RateCol4;
    public TableColumn<ModelClassLarge, ?> AmountCol4;
    public TableColumn<ModelClassLarge, ?> CheckInCol4;
    public TableColumn<ModelClassLarge, ?> ToCheckOutCol4;
    public TableColumn<ModelClassLarge, ?> isCheckOutCol4;
    public TableColumn<ModelClassLarge, ?> RoomNameCol4;
    public JFXTextField SearchField4;
    public JFXButton SearchBut4;
    public JFXDatePicker StartDate4;
    public JFXDatePicker EndDate4;
    public TableView<ModelClassLarge> CheckInTable;
    public TableColumn<ModelClassLarge, ?> NoCol1;
    public TableColumn<ModelClassLarge, ?> DateCol1;
    public TableColumn<ModelClassLarge, ?> ReceiptCol1;
    public TableColumn<ModelClassLarge, ?> NameCol1;
    public TableColumn<ModelClassLarge, ?> PhoneCol1;
    public TableColumn<ModelClassLarge, ?> AdultCol1;
    public TableColumn<ModelClassLarge, ?> GenderCol1;
    public TableColumn<ModelClassLarge, ?> RoomCol1;
    public TableColumn<ModelClassLarge, ?> CheckInCol1;
    public TableColumn<ModelClassLarge, ?> CheckOutCol1;
    public TableColumn<ModelClassLarge, ?> isCheckOutCol1;
    public TableView<ModelClassLarge> PaymentsTable;
    public TableColumn<ModelClassLarge, ?> NoCol2;
    public TableColumn<ModelClassLarge, ?> DateCol2;
    public TableColumn<ModelClassLarge, ?> ReceiptCol2;
    public TableColumn<ModelClassLarge, ?> NameCol2;

    public TableColumn<ModelClassLarge, ?> RoomCol2;

    public TableColumn<ModelClassLarge, ?> TotalCol2;
    public TableColumn<ModelClassLarge, ?> PaidCol2;
    public TableColumn<ModelClassLarge, ?> BalanceCol2;
    public JFXTextField SearchField2;
    public JFXButton SearchBut2;
    public JFXDatePicker StartDate2;
    public JFXDatePicker EndDate2;
    public TableView<ModelClassLarge> ReceiptTable;
    public TableColumn<ModelClassLarge, ?> NoCol3;
    public TableColumn<ModelClassLarge, ?> DateCol3;
    public TableColumn<ModelClassLarge, ?> NameCol3;
    public TableColumn<ModelClassLarge, ?> ReceiptCol3;
    public TableColumn<ModelClassLarge, ?> PhoneCol3;
    public TableColumn<ModelClassLarge, ?> AdultCol3;
    public TableColumn<ModelClassLarge, ?> GenderCol3;
    public TableColumn<ModelClassLarge, ?> AddressCol3;
    public TableColumn<ModelClassLarge, ?> RoomCol3;
    public TableColumn<ModelClassLarge, ?> CheckInCol3;
    public TableColumn<ModelClassLarge, ?> CheckOutCol3;
    public TableColumn<ModelClassLarge, ?> isCheckOutCol3;
    public TableColumn<ModelClassLarge, ?> OccupantsCol3;
    public TableColumn<ModelClassLarge, ?> DaysCol3;
    public TableColumn<ModelClassLarge, ?> TotalCol3;
    public TableColumn<ModelClassLarge, ?> PaidCol3;
 
    public TableColumn<ModelClassLarge, ?> BalanceCol3;
    public TableColumn<ModelClassLarge, ?> MethodCol3;
    public JFXTextField SearchField12;
    public JFXButton SearchBut;
    public JFXDatePicker StartDate12;
    public JFXDatePicker EndDate;
    public JFXDatePicker EndDate3;
    public JFXDatePicker StartDate3;
    public JFXButton SearchBut3;
    public JFXTextField SearchField3;
    public JFXTextField SearchField1;
    public JFXButton SearchBut1;
    public JFXDatePicker StartDate1;
    public JFXDatePicker EndDate1;
    public TableColumn<ModelClassLarge, ?> TimeCol2;
    public TableColumn<ModelClassLarge, ?> MethodCol2;


    public  RecordsController() {
        try {
            FirstLoad();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void actionEvent(ActionEvent actionEvent) throws Exception {
        if(actionEvent.getSource().equals(SearchBut4)){
            ObservableList<ModelClassLarge> Data = MainClass.FillTableLarge(8, "SELECT Number,Status,TimesBooked,DAysBooked,Rate,TotalAmount,CheckInDate || ' ' ||CheckInTime as CheckedIn,Name FROM RoomList ");
            for(ModelClassLarge m : Data){
                if(m.getCol7().contains("NO")){
                    m.setCol7("");
                }
            } LoadRoomTable(Data);
        }

    }

    public void keyEvent(KeyEvent keyEvent) {
    }

    public void mouseEvent(MouseEvent mouseEvent) {
    }

    public void dragStart(MouseEvent mouseEvent) {
    }

    public void dragAction(MouseEvent mouseEvent) {
    }

    public void FirstLoad() {
        final ObservableList<ModelClassLarge>[] Data = new ObservableList[]{FXCollections.observableArrayList(),FXCollections.observableArrayList(),FXCollections.observableArrayList(),FXCollections.observableArrayList()};
        Service<Void> keepAlive = new Service<Void>() {
            @Override
            public Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    public Void call() {
                        System.out.println("Loading Room Table");
                        try {

                            Data[1] = MainClass.FillTableLarge(10, "SELECT Date,Receipt,Name,Phone,Room,CheckInDate,CheckInTime,ToCheckOutDate,ToCheckOutTime, CheckedOutDate FROM CheckIns ");
                            Data[2] = MainClass.FillTableLarge(9, "SELECT Date,Receipt,Name,Room,Time,Total,Paid,Balance,Method FROM Payments ");
                            Data[3] = MainClass.FillTableLarge(20, "SELECT Date,Receipt,Name,Phone,Address,isAdult,Gender,Room,CheckInDate,CheckInTime,ToCheckOutDate,ToCheckOutTime,CheckedOutDate,CheckedOutTime,Occupants,Days,Rate,Total,Paid,Method,Balance FROM Receipts ");
                            Data[0] = MainClass.FillTableLarge(8, "SELECT Number,Status,TimesBooked,DaysBooked,Rate,TotalAmount,CheckInDate || ' ' ||CheckInTime as CheckedIn,Name FROM RoomList  ");
                            for(ModelClassLarge m : Data[0]){
                                if(m.getCol7().contains("NO")){
                                    m.setCol7("");
                                }
                            }
                            for(ModelClassLarge m : Data[1]){
                                String checkIn = MainClass.returnDate3Format(LocalDate.parse(m.getCol6(), MainClass.DatabaseDateFormat)) +"  "+m.getCol7();
                                String checkOut = MainClass.returnDate3Format(LocalDate.parse(m.getCol8(), MainClass.DatabaseDateFormat)) +"  "+m.getCol9();
                                m.setCol1( LocalDate.parse(m.getCol1(), MainClass.DatabaseDateFormat).format(MainClass.UIDateFormat));
                                m.setCol4( "0"+m.getCol4());
                                m.setCol6(checkIn);
                                m.setCol7(checkOut);
                            }
                            for(ModelClassLarge m : Data[2]){
                                m.setCol1( LocalDate.parse(m.getCol1(), MainClass.DatabaseDateFormat).format(MainClass.UIDateFormat));
                            }
                            for(ModelClassLarge m : Data[3]){
                                String checkIn = MainClass.returnDate3Format(LocalDate.parse(m.getCol9(), MainClass.DatabaseDateFormat)) +"  "+m.getCol10();
                                String checkOut = MainClass.returnDate3Format(LocalDate.parse(m.getCol11(), MainClass.DatabaseDateFormat)) +"  "+m.getCol12();
                                m.setCol1( LocalDate.parse(m.getCol1(), MainClass.DatabaseDateFormat).format(MainClass.UIDateFormat));
                                m.setCol4( "0"+m.getCol4());
                                m.setCol9(checkIn);
                                m.setCol10(checkOut);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
            }

        };
        keepAlive.setOnSucceeded(event -> {
            try {
                LoadRoomTable(Data[0]);
                LoadCheckInTable(Data[1]);
                LoadPaymentTable(Data[2]);
                LoadReceiptsTable(Data[3]);
                ReloadTableService();
            } catch (Exception e) {
               e.printStackTrace();
            }
        });
        keepAlive.restart();
    }

    public void ReloadTableService() {
        MainClass.reloadRecordsTables = false;
        final boolean[] isOnline = {false};
        Service<Void> keepAlive = new Service<Void>() {
            @Override
            public Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    public Void call() throws Exception {
                        while (true) {
                            isOnline[0] = MainClass.reloadRecordsTables;
                            if(isOnline[0]){
                               break;
                            }
                            Thread.sleep(1000);
                        }
                        return null;
                    }
                };
            }

        };
        keepAlive.setOnSucceeded(event -> {
            try {
                FirstLoad();
            } catch (Exception e) {
               e.printStackTrace();
            }
        });
        keepAlive.restart();
    }



    public void LoadRoomTable(ObservableList<ModelClassLarge> data)throws Exception   {
        RoomTable.setItems(null);
        NoCol4.setCellValueFactory(new PropertyValueFactory<>("Id"));
        RoomCol4.setCellValueFactory(new PropertyValueFactory<>("col1"));
        StatusCol4.setCellValueFactory(new PropertyValueFactory<>("col2"));
        TimesCol4.setCellValueFactory(new PropertyValueFactory<>("col3"));
        TotalDaysCol.setCellValueFactory(new PropertyValueFactory<>("col4"));
        RateCol4.setCellValueFactory(new PropertyValueFactory<>("col5"));
        AmountCol4.setCellValueFactory(new PropertyValueFactory<>("col6"));
        CheckInCol4.setCellValueFactory(new PropertyValueFactory<>("col7"));
        RoomNameCol4.setCellValueFactory(new PropertyValueFactory<>("col8"));
        RoomTable.setItems(data);

    }
    public void LoadCheckInTable(ObservableList<ModelClassLarge> data)throws Exception   {
        CheckInTable.setItems(null);
        NoCol1.setCellValueFactory(new PropertyValueFactory<>("Id"));
        DateCol1.setCellValueFactory(new PropertyValueFactory<>("col1"));
        ReceiptCol1.setCellValueFactory(new PropertyValueFactory<>("col2"));
        NameCol1.setCellValueFactory(new PropertyValueFactory<>("col3"));
        PhoneCol1.setCellValueFactory(new PropertyValueFactory<>("col4"));
        RoomCol1.setCellValueFactory(new PropertyValueFactory<>("col5"));
        CheckInCol1.setCellValueFactory(new PropertyValueFactory<>("col6"));
        CheckOutCol1.setCellValueFactory(new PropertyValueFactory<>("col7"));
        isCheckOutCol1.setCellValueFactory(new PropertyValueFactory<>("col10"));
        CheckInTable.setItems(data);

    }
    public void LoadPaymentTable(ObservableList<ModelClassLarge> data)throws Exception   {
        PaymentsTable.setItems(null);
//        Date,Receipt,Name,Room,Time,Total,Paid,Balance,Method
        NoCol2.setCellValueFactory(new PropertyValueFactory<>("Id"));
        DateCol2.setCellValueFactory(new PropertyValueFactory<>("col1"));
        ReceiptCol2.setCellValueFactory(new PropertyValueFactory<>("col2"));
        NameCol2.setCellValueFactory(new PropertyValueFactory<>("col3"));
        RoomCol2.setCellValueFactory(new PropertyValueFactory<>("col4"));
        TimeCol2.setCellValueFactory(new PropertyValueFactory<>("col5"));
        TotalCol2.setCellValueFactory(new PropertyValueFactory<>("col6"));
        PaidCol2.setCellValueFactory(new PropertyValueFactory<>("col7"));
        BalanceCol2.setCellValueFactory(new PropertyValueFactory<>("col8"));
        MethodCol2.setCellValueFactory(new PropertyValueFactory<>("col9"));

        PaymentsTable.setItems(data);
    }

    public void LoadReceiptsTable(ObservableList<ModelClassLarge> data)throws Exception   {
        ReceiptTable.setItems(null);
        NoCol3.setCellValueFactory(new PropertyValueFactory<>("Id"));
        DateCol3.setCellValueFactory(new PropertyValueFactory<>("col1"));
        ReceiptCol3.setCellValueFactory(new PropertyValueFactory<>("col2"));
        NameCol3.setCellValueFactory(new PropertyValueFactory<>("col3"));
        PhoneCol3.setCellValueFactory(new PropertyValueFactory<>("col4"));
        AddressCol3.setCellValueFactory(new PropertyValueFactory<>("col5"));
        AdultCol3.setCellValueFactory(new PropertyValueFactory<>("col6"));
        GenderCol3.setCellValueFactory(new PropertyValueFactory<>("col7"));
        RoomCol3.setCellValueFactory(new PropertyValueFactory<>("col8"));
        CheckInCol3.setCellValueFactory(new PropertyValueFactory<>("col9"));
        CheckOutCol3.setCellValueFactory(new PropertyValueFactory<>("col10"));
        isCheckOutCol3.setCellValueFactory(new PropertyValueFactory<>("col13"));
        OccupantsCol3.setCellValueFactory(new PropertyValueFactory<>("col15"));
        DaysCol3.setCellValueFactory(new PropertyValueFactory<>("col16"));
        TotalCol3.setCellValueFactory(new PropertyValueFactory<>("col17"));
        PaidCol3.setCellValueFactory(new PropertyValueFactory<>("col18"));
        MethodCol3.setCellValueFactory(new PropertyValueFactory<>("col19"));
        BalanceCol3.setCellValueFactory(new PropertyValueFactory<>("col20"));


        ReceiptTable.setItems(data);
    }
}
