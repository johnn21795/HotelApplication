package Controllers;

import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class SummaryController implements Initializable {

    public PieChart RoomChart;
    public PieChart IncomeChart;
    public PieChart CheckInChart;
    public PieChart CheckOutChart;
    public JFXDatePicker RoomDate;
    public Label RoomLabel;
    public JFXDatePicker IncomeDate;
    public Label IncomeLabel;
    public JFXDatePicker CheckInDate;
    public JFXDatePicker CheckOutDate;
    public JFXDatePicker SummaryDate;
    public Label AvailableRm;
    public Label OcccupiedRm;
    public Label ReservedRm;
    public Label Cash;
    public Label Transfer;
    public Label Debit;
    public Label CnMen;
    public Label CnWomen;
    public Label CnChildren;
    public Label CoMen;
    public Label CoWomen;
    public Label CoChildren;

    public void actionEvent(ActionEvent actionEvent) {
    }

    public void keyEvent(KeyEvent keyEvent) {
    }

    public void mouseEvent(MouseEvent mouseEvent) {
    }

    public void dragStart(MouseEvent mouseEvent) {
    }

    public void dragAction(MouseEvent mouseEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PieChart.Data a = new PieChart.Data("Cash 70%", 0.7);
        PieChart.Data b = new PieChart.Data("Transfer 20%", 0.2);
        PieChart.Data c = new PieChart.Data("Debit 10%", 0.1);

        PieChart.Data d = new PieChart.Data("Available 70%", 0.7);
        PieChart.Data e = new PieChart.Data("Occupied 20%", 0.2);
        PieChart.Data f = new PieChart.Data("Reserved 10%", 0.1);

        PieChart.Data g = new PieChart.Data("Cash 70%", 0.7);
        PieChart.Data h = new PieChart.Data("Transfer 20%", 0.2);
        PieChart.Data i = new PieChart.Data("Debit 10%", 0.1);

        PieChart.Data j = new PieChart.Data("Cash 70%", 0.7);
        PieChart.Data k = new PieChart.Data("Transfer 20%", 0.2);
        PieChart.Data l = new PieChart.Data("Debit 10%", 0.1);


        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(d,e,f);
        ObservableList<PieChart.Data> data2 = FXCollections.observableArrayList(a,b,c);
        ObservableList<PieChart.Data> data3 = FXCollections.observableArrayList(g,h,i);
        ObservableList<PieChart.Data> data4 = FXCollections.observableArrayList(j,k,l);
        IncomeChart.dataProperty().set(data2);
        CheckInChart.dataProperty().set(data3);
        CheckOutChart.dataProperty().set(data4);
        RoomChart.dataProperty().set(data);
    }
}
