package Controllers.MiniControllers;

import Classes.MainClass;
import Classes.ModelClassLarge;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

public class PaySummaryController implements Initializable {
    public AnchorPane rootPane;
    public Label StartDate;
    public Label EndDate;
    public TableView<ModelClassLarge> Table;
    public TableColumn<ModelClassLarge , ?> NoCol;
    public TableColumn<ModelClassLarge , ?> MetCol;
    public TableColumn<ModelClassLarge , ?> TotCol;
    public TableColumn<ModelClassLarge , ?> PaidCol;
    public TableColumn<ModelClassLarge , ?> BalCol;
    public Label GTotal;
    public Label GPaid;

    ObservableList<ModelClassLarge> TableData = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootPane.getStylesheets().remove(0);
        rootPane.getStylesheets().add("file:/"+System.getProperty("user.home").replace("\\", "/" ) + "/MainStyle.css");
    }

    public void getInfo(LocalDate startDate, LocalDate endDate)throws Exception{
        StartDate.setText(startDate.format(MainClass.UIDateFormat));
        EndDate.setText(endDate.format(MainClass.UIDateFormat));

       TableData = MainClass.FillTableLarge(4,"SELECT Method,sum(Total) as Total,sum(paid) as Paid,sum(balance) as Balance FROM Payments where Date between '"+ startDate.format(MainClass.DatabaseDateFormat)+"' and '"+ endDate.format(MainClass.DatabaseDateFormat)+"' group by Method  ");
        int GrandTotal =0;
        int GrandPaid =0;
        System.out.println(TableData);

        for(int x = 0; x < TableData.size(); x++){
            GrandTotal += Integer.parseInt(TableData.get(x).getCol2());
            GrandPaid += Integer.parseInt(TableData.get(x).getCol3());

            Table.setItems(null);
            NoCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
            MetCol.setCellValueFactory(new PropertyValueFactory<>("Col1"));
            TotCol.setCellValueFactory(new PropertyValueFactory<>("Col2"));
            PaidCol.setCellValueFactory(new PropertyValueFactory<>("Col3"));
            BalCol.setCellValueFactory(new PropertyValueFactory<>("Col4"));
            Table.setItems(TableData);

            GTotal.setText(String.valueOf(GrandTotal));
            GPaid.setText(String.valueOf(GrandPaid));

        }






    }
}

