package Controllers.MiniControllers;

import Classes.MainClass;
import Classes.ModelClassLarge;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.HashMap;
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
    public Label ViewAll;

    ObservableList<String> MainData = FXCollections.observableArrayList();
    ObservableList<Map<String, String>> Occupants = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootPane.getStylesheets().remove(0);
        rootPane.getStylesheets().add("file:/"+System.getProperty("user.home").replace("\\", "/" ) + "/currentTheme.css");

    }



    public void getData(ObservableList<String> data, ObservableList<Map<String, String>> people) {
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

    public void fetchData(int Receipt)throws Exception{
        this.SaveBut.setVisible(false);
        this.PrintBut.setOnAction( this::printAddress2);
        ObservableList<ModelClassLarge>  Data = MainClass.FillTableLarge(20, "SELECT Date,Receipt,Name,Phone,Address,isAdult,Gender,Room,CheckInDate,CheckInTime,ToCheckOutDate,ToCheckOutTime,CheckedOutDate,CheckedOutTime,Occupants,Days,Rate,Total,Paid,Balance,Method FROM Receipts WHERE Receipt ="+Receipt+" ");
        this.RNo.setText(String.valueOf(Data.get(0).getCol2()));
        this.Date.setText(String.valueOf(Data.get(0).getCol1()));
        this.Time.setText(String.valueOf(Data.get(0).getCol10()));
        this.Name.setText(String.valueOf(Data.get(0).getCol3()));
        this.Phone.setText(String.valueOf(Data.get(0).getCol4()));
        this.Room.setText(String.valueOf(Data.get(0).getCol8()));
        this.People.setText(String.valueOf(Data.get(0).getCol15()));
        this.Days.setText(String.valueOf(Data.get(0).getCol16()));
        this.Amount.setText(String.valueOf(Data.get(0).getCol18()));
        this.Paid.setText(String.valueOf(Data.get(0).getCol19()));
        this.Balance.setText(String.valueOf(Data.get(0).getCol20()));
        if(!Data.get(0).getCol13().equalsIgnoreCase("NO")){
            this.CheckOut.setText(MainClass.returnDate3Format(LocalDate.parse(String.valueOf( Data.get(0).getCol13()), MainClass.DatabaseDateFormat))+"  "+ Data.get(0).getCol14());
        }

    }

    private void printAddress2(ActionEvent event) {
        XSSFWorkbook workbook;
        XSSFSheet sheet;
        XSSFCellStyle cellStyle;
        XSSFFont font ;
        Map<String, CellStyle> cellStyles = new HashMap();

        try {
            File temp =  new File(System.getProperty("user.home") + "/ReceiptTemp.xlsx");
            Files.copy(new File(System.getProperty("user.home") + "/Receipt.xlsx").toPath(), temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
            workbook = new XSSFWorkbook(temp);

            sheet = workbook.getSheetAt(0);

            cellStyle = workbook.createCellStyle();

            font = workbook.createFont();
            font.setFontHeightInPoints((short) 12);
            font.setFontName("Arial Narrow");
            font.setBold(true);
            cellStyle.setFont(font);
            cellStyles.put("main", cellStyle);

            font = workbook.createFont();
            font.setFontHeightInPoints((short) 16);
            font.setFontName("Bahnschrift Light Condensed");
            cellStyle.setFont(font);
            cellStyles.put("Details", cellStyle);

            cellStyle = workbook.createCellStyle();
            font = workbook.createFont();
            font.setFontHeightInPoints((short) 11);
            font.setFontName("Calibri");
            cellStyle.setFont(font);
            cellStyles.put("normal", cellStyle);

            cellStyle = workbook.createCellStyle();
            font = workbook.createFont();
            font.setFontHeightInPoints((short) 11);
            font.setFontName("Calibri");
            cellStyle.setFont(font);
            cellStyle.setVerticalAlignment(VerticalAlignment.DISTRIBUTED);
            cellStyles.put("address", cellStyle);

            //Receipt No
            Row row = sheet.getRow(4);
            Cell cell = row.createCell(0);
            cell.setCellStyle(cellStyles.get("Details"));
            cell.setCellValue("No:"+this.RNo.getText());

            //DATE
            row = sheet.getRow(6);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyles.get("Details"));
            cell.setCellValue(this.Date.getText()+"     "+this.Time.getText());

            //NAME
            row = sheet.getRow(8);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyles.get("Details"));
            cell.setCellValue(this.Name.getText());

            //PHONE
            row = sheet.getRow(9);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyles.get("Details"));
            cell.setCellValue(this.Phone.getText());

            //ROOM
            row = sheet.getRow(11);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyles.get("Details"));
            cell.setCellValue(this.Room.getText());

            //AMOUNT
            row = sheet.getRow(13);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyles.get("Details"));
            cell.setCellValue(this.Amount.getText());

            //PAID
            row = sheet.getRow(14);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyles.get("Details"));
            cell.setCellValue(this.Paid.getText());

            //BALANCE
            row = sheet.getRow(15);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyles.get("Details"));
            cell.setCellValue(this.Balance.getText());

            //CHECKOUT
            row = sheet.getRow(17);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyles.get("Details"));
            cell.setCellValue(this.CheckOut.getText());

            File destination = new File(System.getProperty("user.home") + "/Desktop/Receipts/"+this.Name.getText()+"-"+this.RNo.getText()+".xlsx");
            FileOutputStream outputStream = new FileOutputStream(System.getProperty("user.home") + "/MyReceipt.xlsx");
            workbook.write(outputStream);
            workbook.close();
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Throwable ignored) {
                }
            } else {
                outputStream.close();
            }

            Files.move(new File(System.getProperty("user.home") + "/MyReceipt.xlsx").toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Desktop desktop = Desktop.getDesktop();
            desktop.open(destination);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    public void printAddress() throws Exception {
        XSSFWorkbook workbook;
        XSSFSheet sheet;
        XSSFCellStyle cellStyle;
        XSSFFont font ;
        Map<String, CellStyle> cellStyles = new HashMap();

        File temp =  new File(System.getProperty("user.home") + "/ReceiptTemp.xlsx");
        Files.copy(new File(System.getProperty("user.home") + "/Receipt.xlsx").toPath(), temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
         workbook = new XSSFWorkbook(temp);

        sheet = workbook.getSheetAt(0);

        cellStyle = workbook.createCellStyle();

        font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setFontName("Arial Narrow");
        font.setBold(true);
        cellStyle.setFont(font);
        cellStyles.put("main", cellStyle);

        font = workbook.createFont();
        font.setFontHeightInPoints((short) 16);
        font.setFontName("Bahnschrift Light Condensed");
        cellStyle.setFont(font);
        cellStyles.put("Details", cellStyle);

        cellStyle = workbook.createCellStyle();
        font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("Calibri");
        cellStyle.setFont(font);
        cellStyles.put("normal", cellStyle);

        cellStyle = workbook.createCellStyle();
        font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("Calibri");
        cellStyle.setFont(font);
        cellStyle.setVerticalAlignment(VerticalAlignment.DISTRIBUTED);
        cellStyles.put("address", cellStyle);

        //Receipt No
        Row row = sheet.getRow(4);
        Cell cell = row.createCell(0);
        cell.setCellStyle(cellStyles.get("Details"));
        cell.setCellValue("No:"+this.RNo.getText());

        //DATE
        row = sheet.getRow(6);
        cell = row.createCell(1);
        cell.setCellStyle(cellStyles.get("Details"));
        cell.setCellValue(this.Date.getText()+"     "+this.Time.getText());

        //NAME
        row = sheet.getRow(8);
        cell = row.createCell(1);
        cell.setCellStyle(cellStyles.get("Details"));
        cell.setCellValue(this.Name.getText());

        //PHONE
        row = sheet.getRow(9);
        cell = row.createCell(1);
        cell.setCellStyle(cellStyles.get("Details"));
        cell.setCellValue(this.Phone.getText());

        //ROOM
        row = sheet.getRow(11);
        cell = row.createCell(1);
        cell.setCellStyle(cellStyles.get("Details"));
        cell.setCellValue(this.Room.getText());

        //AMOUNT
        row = sheet.getRow(13);
        cell = row.createCell(1);
        cell.setCellStyle(cellStyles.get("Details"));
        cell.setCellValue(this.Amount.getText());

        //PAID
        row = sheet.getRow(14);
        cell = row.createCell(1);
        cell.setCellStyle(cellStyles.get("Details"));
        cell.setCellValue(this.Paid.getText());

        //BALANCE
        row = sheet.getRow(15);
        cell = row.createCell(1);
        cell.setCellStyle(cellStyles.get("Details"));
        cell.setCellValue(this.Balance.getText());

        //CHECKOUT
        row = sheet.getRow(17);
        cell = row.createCell(1);
        cell.setCellStyle(cellStyles.get("Details"));
        cell.setCellValue(this.CheckOut.getText());

        File destination = new File(System.getProperty("user.home") + "/Desktop/Receipts/"+this.Name.getText()+"-"+this.RNo.getText()+".xlsx");
        FileOutputStream outputStream = new FileOutputStream(System.getProperty("user.home") + "/MyReceipt.xlsx");
        workbook.write(outputStream);
        workbook.close();
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (Throwable ignored) {
            }
        } else {
            outputStream.close();
        }

        Files.move(new File(System.getProperty("user.home") + "/MyReceipt.xlsx").toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        saveReceipt();
        Desktop desktop = Desktop.getDesktop();
        desktop.open(destination);
    }

}
