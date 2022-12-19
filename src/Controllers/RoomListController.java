package Controllers;

import Classes.MainClass;
import Classes.ModelClassLarge;
import Controllers.MiniControllers.AddGuestController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;



public class RoomListController implements Initializable {
    public TableView<ModelClassLarge> RoomListTable;
    public TableColumn<ModelClassLarge, ?> RoomNoCol;
    public TableColumn<ModelClassLarge, ?> IDCol;
    public TableColumn<ModelClassLarge, ?> NoCol;
    public TableColumn<ModelClassLarge, ?> RoomCol;
    public TableColumn<ModelClassLarge, ?> TypeCol;
    public TableColumn<ModelClassLarge, ?> RateCol;
    public TableColumn<ModelClassLarge, ?> StatusCol;
    public TableColumn<ModelClassLarge, ?> OccupantCol;
    public TableColumn<ModelClassLarge, ?> OtherCol;
    public JFXComboBox<String> StatusBox1;
    public JFXButton SearchBut1;
    public TableView<ModelClassLarge> GuestListTable;
    public TableColumn<ModelClassLarge, ?> NoCol2;
    public TableColumn<ModelClassLarge, ?> DateCol2;
    public TableColumn<ModelClassLarge, ?> ReceiptCol2;
    public TableColumn<ModelClassLarge, ?> NameCol2;
    public TableColumn<ModelClassLarge, ?> PhoneCol2;
    public TableColumn<ModelClassLarge, ?> GenderCol2;
    public TableColumn<ModelClassLarge, ?> CheckOutCol2;
    public TableColumn<ModelClassLarge, ?> CheckInCol2;
    public TableColumn<ModelClassLarge, ?> RoomCol2;
    public TableColumn<ModelClassLarge, ?> AdultCol2;
    public JFXTextField Search2;
    public JFXButton SearchBut2;
    public JFXButton CheckBut;
    public JFXTextField Search1;
    public JFXButton AddBut;
    public JFXButton OutBut;

    private  MainPanelController mainPanelController;

    public RoomListController(){
        try {
            FirstLoad();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            StatusBox1.setItems(FXCollections.observableArrayList("All","Available","Occupied"));
            StatusBox1.getSelectionModel().select(0);
//            FirstLoad();
        }catch (Exception e){
            e.printStackTrace();

        }

    }
    public void FirstLoad() {
        final ObservableList<ModelClassLarge>[] data = new ObservableList[]{FXCollections.observableArrayList(),FXCollections.observableArrayList()};
        Service<Void> keepAlive = new Service<Void>() {
            @Override
            public Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    public Void call() {
                        try {
                            data[0] = MainClass.FillTableLarge(6, "SELECT Number,Name,Type,Rate,Status,Occupant FROM RoomList");
                            data[1] = MainClass.FillTableLarge(12, "SELECT Date,Receipt,Name,Phone,Gender,isAdult,Room,CheckInDate,CheckInTime,ToCheckOutDate,ToCheckOutTime,CheckedOutDate FROM Occupants WHERE CheckedOutDate = 'NO' ");

                            for(ModelClassLarge m : data[1]){
                                String checkIn = MainClass.returnDate3Format(LocalDate.parse(m.getCol8(), MainClass.DatabaseDateFormat)) +"  "+m.getCol9();
                                String checkOut = MainClass.returnDate3Format(LocalDate.parse(m.getCol10(), MainClass.DatabaseDateFormat)) +"  "+m.getCol11();
                                m.setCol1( LocalDate.parse(m.getCol1(), MainClass.DatabaseDateFormat).format(MainClass.UIDateFormat));
                                m.setCol4( "0"+m.getCol4());
                                m.setCol8(checkIn);
                                m.setCol9(checkOut);
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
            LoadGuestList(data[1]);
            LoadRoomList(data[0]);
            ReloadTableService();
        });
        keepAlive.setOnFailed(event -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Could Not Load RoomList");
            alert.setContentText("Could Not Load RoomList");
            alert.show();
        });
        keepAlive.restart();
    }
    public void ReloadTableService() {
        MainClass.reloadRoomListTables = false;
        final boolean[] isOnline = {false};
        Service<Void> keepAlive = new Service<Void>() {
            @Override
            public Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    public Void call() throws Exception {
                        while (true) {
                            isOnline[0] = MainClass.reloadRoomListTables;
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

    public void keyEvent(KeyEvent keyEvent) throws Exception {
        if(keyEvent.getSource().equals(Search1)){
            String query = Search1.getText();
            LoadRoomList(MainClass.FillTableLarge(6, "SELECT Number,Name,Type,Rate,Status,Occupant FROM RoomList WHERE Type Like '%"+query+"%' or Name Like '%"+query+"%' or Occupant Like '%"+query+"%' or Rate Like '%"+query+"%'"));
        }
        if(keyEvent.getSource().equals(Search2)){
            String query = Search2.getText();
            if(Search2.getText().isEmpty()){
                return;
            }
            String sql;
            try {
                int room = Integer.parseInt(query);
                sql = "SELECT Date,Receipt,Name,Phone,Gender,isAdult,Room,CheckInDate,CheckInTime,ToCheckOutDate,ToCheckOutTime,CheckedOutDate FROM (SELECT * FROM Occupants WHERE CheckedOutDate = 'NO') WHERE  Phone Like '%"+query+"%' OR Room = "+room+" OR Receipt = "+room+" ";
            } catch (NumberFormatException ignore) {
                sql = "SELECT Date,Receipt,Name,Phone,Gender,isAdult,Room,CheckInDate,CheckInTime,ToCheckOutDate,ToCheckOutTime,CheckedOutDate FROM (SELECT * FROM Occupants WHERE CheckedOutDate = 'NO') WHERE Name Like '%"+query+"%' ";
            }

            ObservableList<ModelClassLarge> data = MainClass.FillTableLarge(12, sql);
            for(ModelClassLarge m : data){
                String checkIn = MainClass.returnDate3Format(LocalDate.parse(m.getCol8(), MainClass.DatabaseDateFormat)) +"  "+m.getCol9();
                String checkOut = MainClass.returnDate3Format(LocalDate.parse(m.getCol10(), MainClass.DatabaseDateFormat)) +"  "+m.getCol11();
                m.setCol1( LocalDate.parse(m.getCol1(), MainClass.DatabaseDateFormat).format(MainClass.UIDateFormat));
                m.setCol4( "0"+m.getCol4());
                m.setCol8(checkIn);
                m.setCol9(checkOut);
            }
            LoadGuestList(data);
        }
    }

    public void actionEvent(ActionEvent event) throws Exception {
        if(event.getSource().equals(SearchBut1)){
            String query = Search1.getText();
            if(Search1.getText().isEmpty()){
                LoadRoomList(MainClass.FillTableLarge(6, "SELECT Number,Name,Type,Rate,Status,Occupant FROM RoomList"));
            }else {
                LoadRoomList(MainClass.FillTableLarge(6, "SELECT Number,Name,Type,Rate,Status,Occupant FROM RoomList WHERE Type Like '%"+query+"%' or Name Like '%"+query+"%' or Occupant Like '%"+query+"%' or Rate Like '%"+query+"%'"));
            }
        }
        if(event.getSource().equals(SearchBut2)){
            String query = Search2.getText();
            if(Search2.getText().isEmpty()){
                ObservableList<ModelClassLarge> data = MainClass.FillTableLarge(12, "SELECT Date,Receipt,Name,Phone,Gender,isAdult,Room,CheckInDate,CheckInTime,ToCheckOutDate,ToCheckOutTime,CheckedOutDate FROM Occupants WHERE CheckedOutDate = 'NO' ");
                for(ModelClassLarge m : data){
                    String checkIn = MainClass.returnDate3Format(LocalDate.parse(m.getCol8(), MainClass.DatabaseDateFormat)) +"  "+m.getCol9();
                    String checkOut = MainClass.returnDate3Format(LocalDate.parse(m.getCol10(), MainClass.DatabaseDateFormat)) +"  "+m.getCol11();
                    m.setCol1( LocalDate.parse(m.getCol1(), MainClass.DatabaseDateFormat).format(MainClass.UIDateFormat));
                    m.setCol4( "0"+m.getCol4());
                    m.setCol8(checkIn);
                    m.setCol9(checkOut);
                }
                LoadGuestList(data);
            }else {
                ObservableList<ModelClassLarge> data = MainClass.FillTableLarge(12, "SELECT Date,Receipt,Name,Phone,Gender,isAdult,Room,CheckInDate,CheckInTime,ToCheckOutDate,ToCheckOutTime,CheckedOutDate FROM Occupants WHERE CheckedOutDate = 'NO' AND Phone Like '%"+query+"%' or Name Like '%"+query+"%' AND Room = "+query+" AND Receipt = "+query+" ");
                for(ModelClassLarge m : data){
                    String checkIn = MainClass.returnDate3Format(LocalDate.parse(m.getCol8(), MainClass.DatabaseDateFormat)) +"  "+m.getCol9();
                    String checkOut = MainClass.returnDate3Format(LocalDate.parse(m.getCol10(), MainClass.DatabaseDateFormat)) +"  "+m.getCol11();
                    m.setCol1( LocalDate.parse(m.getCol1(), MainClass.DatabaseDateFormat).format(MainClass.UIDateFormat));
                    m.setCol4( "0"+m.getCol4());
                    m.setCol8(checkIn);
                    m.setCol9(checkOut);
                }
                LoadGuestList(data);
            }
        }
        if(event.getSource().equals(StatusBox1)){
            switch (StatusBox1.getSelectionModel().getSelectedItem()){
                case "All":
                    LoadRoomList(MainClass.FillTableLarge(6, "SELECT Number,Name,Type,Rate,Status,Occupant FROM RoomList"));
                    break;
                case "Occupied":
                    LoadRoomList(MainClass.FillTableLarge(6, "SELECT Number,Name,Type,Rate,Status,Occupant FROM RoomList WHERE Status = 'Occupied' "));
                    break;
                case "Reserved":
                    LoadRoomList(MainClass.FillTableLarge(6, "SELECT Number,Name,Type,Rate,Status,Occupant FROM RoomList WHERE Status = 'Reserved' "));
                    break;
                case "Available":
                default:
                    LoadRoomList(MainClass.FillTableLarge(6, "SELECT Number,Name,Type,Rate,Status,Occupant FROM RoomList WHERE Status = 'Available' "));
                    break;

            }
        }
        if(event.getSource().equals(CheckBut)){
            mainPanelController.setRoom(Integer.parseInt(RoomListTable.getSelectionModel().getSelectedItem().getCol1()));
            MainPanelController.checkIn = true;
        }
        if(event.getSource().equals(OutBut)){
            String Name = GuestListTable.getSelectionModel().getSelectedItem().getCol3();
            int Room = Integer.parseInt(GuestListTable.getSelectionModel().getSelectedItem().getCol7());
            mainPanelController.setCheckOutUI(Name, Room);

        }
        if(event.getSource().equals(AddBut)){
            try {
                Stage stage = new Stage();
                FXMLLoader Loaders = new FXMLLoader();
                Parent root1 = Loaders.load(getClass().getResource("/SubPanes/AddGuest.fxml").openStream());
                AddGuestController addGuestController;
                addGuestController = Loaders.getController();

                ObservableList<String> Data = FXCollections.observableArrayList();
                ModelClassLarge selected = GuestListTable.getSelectionModel().getSelectedItem();
                LocalDate checkOut = LocalDate.parse(selected.getCol9().split("  ")[0].replace("nd", "").replace("th", "").replace("rd", ""), MainClass.format3);
                //Room,CheckOutDate,CheckoutTime,Receipt,
                System.out.println();
                Data.addAll(
                        selected.getCol7(),checkOut.format(MainClass.DatabaseDateFormat),selected.getCol9().split("  ")[1], selected.getCol2()
                );
                addGuestController.getInfo(Data);

                Scene scene = new Scene(root1);
                stage.setScene(scene);
                stage.initStyle(StageStyle.UTILITY);
                stage.showAndWait();
                MainClass.reloadRoomListTables = true;

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    private void LoadRoomList(ObservableList<ModelClassLarge> data){
        RoomListTable.setItems(null);
        NoCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        RoomNoCol.setCellValueFactory(new PropertyValueFactory<>("col1"));
        RoomCol.setCellValueFactory(new PropertyValueFactory<>("col2"));
        TypeCol.setCellValueFactory(new PropertyValueFactory<>("col3"));
        RateCol.setCellValueFactory(new PropertyValueFactory<>("col4"));
        StatusCol.setCellValueFactory(new PropertyValueFactory<>("col5"));
        OccupantCol.setCellValueFactory(new PropertyValueFactory<>("col6"));
        OtherCol.setCellValueFactory(new PropertyValueFactory<>("col7"));
        RoomListTable.setItems(data);
    }
    public void LoadGuestList(ObservableList<ModelClassLarge> data)  {
        GuestListTable.setItems(null);
        NoCol2.setCellValueFactory(new PropertyValueFactory<>("Id"));
        DateCol2.setCellValueFactory(new PropertyValueFactory<>("col1"));
        ReceiptCol2.setCellValueFactory(new PropertyValueFactory<>("col2"));
        NameCol2.setCellValueFactory(new PropertyValueFactory<>("col3"));
        PhoneCol2.setCellValueFactory(new PropertyValueFactory<>("col4"));
        GenderCol2.setCellValueFactory(new PropertyValueFactory<>("col5"));
        AdultCol2.setCellValueFactory(new PropertyValueFactory<>("col6"));
        RoomCol2.setCellValueFactory(new PropertyValueFactory<>("col7"));
        CheckInCol2.setCellValueFactory(new PropertyValueFactory<>("col8"));
        CheckOutCol2.setCellValueFactory(new PropertyValueFactory<>("col9"));
        GuestListTable.setItems(data);

    }


    public void myParent(MainPanelController mainPanelController) {
        this.mainPanelController = mainPanelController;
    }
}
