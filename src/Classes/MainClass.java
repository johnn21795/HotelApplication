package Classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MainClass {

    public static DateTimeFormatter DatabaseDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter UIDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static DateTimeFormatter format3 = DateTimeFormatter.ofPattern("d MMM yyyy");
    public static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");


    public static boolean reloadRecordsTables = false;
    public static boolean reloadRoomListTables = false;

    public static boolean clearCheckInUI = false;





    //  GLOBAL METHODS

    //Database Void Methods
    public static void EditDatabase(String sql) throws Exception {
        if(sql.equalsIgnoreCase("")){
            return;
        }
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = ConnectDB.Main();
            ps = con.prepareStatement(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
    ex.printStackTrace();
        } finally {
            assert ps != null;
            ps.execute();
            con.close();
        }

    }
    public static boolean InsertDB(Map<String, Object> Data, String Table )throws Exception{
        boolean isSuccessful = false;
        Connection con = null;
        PreparedStatement ps = null;
        AtomicInteger count = new AtomicInteger(1);
        StringBuilder sql = new StringBuilder("INSERT INTO " + Table + " (");
        try {
            con = ConnectDB.Main();
            Data.forEach( (k,v) -> sql.append(k).append(","));
            sql.append(") VALUES (");
            Data.forEach( (k,v) -> sql.append("?").append(","));
            sql.append(")");
            String finalSQl = sql.toString().replace(",)", ")");
            System.out.println("Final SQl "+finalSQl);
            assert con != null;
            ps = con.prepareStatement(finalSQl);
            PreparedStatement finalPs = ps;
            Data.forEach( (k, v) -> {
                try {
                    finalPs.setObject(count.get(), v);
                    count.getAndIncrement();
                } catch (SQLException e) {
                  e.printStackTrace();
                }
            });
            isSuccessful = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            assert ps != null;
            ps.execute();
            con.close();
        }
        return isSuccessful;

    }
    public static boolean UpdateDB(Map<String, Object> Data, String Table, String key )throws Exception{
        boolean isSuccessful = false;
        Connection con = null;
        PreparedStatement ps = null;
        AtomicInteger count = new AtomicInteger(1);
//        String sql = "UPDATE Inventory SET "+field+" = '"+value+"' WHERE ItemNo = '"+item+"' ";
        StringBuilder sql = new StringBuilder("UPDATE " + Table + " SET ");
        try {
            con = ConnectDB.Main();
            Data.forEach( (k,v) -> sql.append(k).append(" = ").append("'").append(v).append("', "));
            sql.append("  WHERE ").append(key).append(" = ");
            Data.forEach( (k,v) -> sql.append("?").append(","));
            sql.append(")");
            String finalSQl = sql.toString().replace(",)", ")");
            System.out.println("Final SQl "+finalSQl);
            assert con != null;
            ps = con.prepareStatement(finalSQl);
            PreparedStatement finalPs = ps;
            Data.forEach( (k, v) -> {
                try {
                    finalPs.setObject(count.get(), v);
                    count.getAndIncrement();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            isSuccessful = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            assert ps != null;
            ps.execute();
            con.close();
        }
        return isSuccessful;

    }
    public static ObservableList<ModelClassLarge> FillTableLarge(int columns, String sql)throws Exception{
        System.out.println(sql);
        Connection con;
        con  =ConnectDB.Main();
        ObservableList<Object> column ;
        ObservableList<ModelClassLarge> Data = FXCollections.observableArrayList();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            column = FXCollections.observableArrayList();
            int num = rs.getRow();
            for(int x =0; x <columns; x++){
                String y = rs.getString(x+1);
                column.add((y == null?  "" :y));
            }
            if(column.size() < 21 ){
                int y = 21 - column.size();
                for(int z =0; z < y; z++){
                    column.add("");
                }
            }

            // Commenting Out AutoDate Conversion
//            try {
//                String olddate = column.get(5).toString();
//                String newDate = LocalDate.parse(olddate, DatabaseDateFormat).format(UIDateFormat);
//                column.set(5, newDate);
//
//            } catch (Exception ignored) {
//
//            }
//            try {
//                String olddate = column.get(3).toString();
//                String newDate = LocalDate.parse(olddate, DatabaseDateFormat).format(UIDateFormat);
//                column.set(3, newDate);
//
//            } catch (Exception ignored) {
//
//            }
            ModelClassLarge modelClass = new ModelClassLarge(num, column.get(0).toString(), column.get(1).toString(), column.get(2).toString(),
                    column.get(3).toString(), column.get(4).toString(), column.get(5).toString(), column.get(6).toString(), column.get(7).toString(), column.get(8).toString(), column.get(9).toString(),column.get(10).toString(),column.get(11).toString(),column.get(12).toString(),column.get(13).toString(),column.get(14).toString(),column.get(15).toString(),column.get(16).toString(),column.get(17).toString(),column.get(18).toString(),column.get(19).toString(),column.get(20).toString(), false);

            Data.add(modelClass);
        }
        con.close();
        return Data;
    }

    //Return Methods
    public static Boolean getBoolean(String sql) throws Exception{
        boolean result;
        Connection con;
        con = ConnectDB.Main();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        result = rs.next();
        con.close();
        return result;

    }
    public static Map<String, String> getMap(String sql, String Key, String Value) throws Exception{
        Map<String, String> Data = new HashMap<>();
        Connection con;
        con =ConnectDB.Main();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String key = rs.getString(Key);
            String value = rs.getString(Value);
            Data.put(key, value);
        }
        con.close();
        return Data;
    }

    public static String getString(String sql, String Column) throws Exception {
        System.out.println(sql);
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String x = "";
        con  =ConnectDB.Main();
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            x = rs.getString(Column);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            assert rs != null;
            assert con != null;
            rs.close();
            ps.close();
            con.close();
        }
        return x;
    }
    public static int getInt(String sql, String Column) throws Exception {
        Connection con;
        con  =ConnectDB.Main();
        PreparedStatement ps = con.prepareStatement(sql);
        System.out.println(sql);
        ResultSet rs = ps.executeQuery();
        int x = rs.getInt(Column);
        rs.close();
        ps.close();
        con.close();
        return x;
    }
    public static ObservableList<String> getObservableList(String sql, String[] columns) throws Exception {
        ObservableList<String> Data = FXCollections.observableArrayList();
        Connection con;
        con = ConnectDB.Main();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            for (String column: columns){
                Data.add(rs.getString(column));
            }
        }
        con.close();
        return Data;
    }


    //Other Methods
    //multiple selection for table
    public static void copySelectionToClipboard(final TableView<?> table) {
        final Set<Integer> rows = new TreeSet<>();
        for (final TablePosition tablePosition : table.getSelectionModel().getSelectedCells()) {
            rows.add(tablePosition.getRow());
        }
        final StringBuilder strb = new StringBuilder();
        boolean firstRow = true;
        for (final Integer row : rows) {
            if(row == 0){
                continue;
            }
            if (!firstRow) {
                strb.append('\n');
            }
            firstRow = false;
            boolean firstCol = true;
            for (final TableColumn<?, ?> column : table.getColumns()) {
                if (!column.getId().equalsIgnoreCase("NoCol") && !column.getId().equalsIgnoreCase("NoCol3")) {
                    if (!firstCol) {
                        strb.append('\t');
                    }
                    firstCol = false;
                    final Object cellData = column.getCellData(row);
                    strb.append(cellData == null ? "" : cellData.toString());
                }

            }
        }
        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(strb.toString());
        Clipboard.getSystemClipboard().setContent(clipboardContent);
    }
    public static String returnTitleCase(String text){
        StringBuilder result = new StringBuilder();
        try {
            String[] words = text.split(" ");
            for(String x : words){
                String capital = x.substring(0, 1).toUpperCase();
                String small = x.substring(1).toLowerCase();
                result.append(capital).append(small).append(" ");
            }
        } catch (Exception e) {
            result = new StringBuilder(text);
        }
        return result.toString().trim();
    }
    public static String returnDate3Format(LocalDate date){
        String subD;
        int Day = date.getDayOfMonth();
        if(Day == 1|| Day ==21 || Day ==31){
            subD = "st";
        }
        else if(Day == 2|| Day ==22){
            subD = "nd";
        }
        else if(Day == 3|| Day ==23){
            subD = "rd";
        }else{
            subD = "th";
        }
        return date.format(MainClass.format3).replaceFirst(" ", subD+" ");
    }
    static String returnCommaValue(String text){
        String result = "N";
        if(text.length() == 3){
            return result + text;
        }
        try {
            String capital = text.substring(text.length()-3);
            String small = text.substring(0, text.length()-3).toLowerCase();
            result+= small+","+capital;

        } catch (Exception e) {
            result += text;
        }

        return result;
    }



    //Application Methods
    public static boolean InsertCheckIn(ObservableList<String> data) throws SQLException {
        String sql = "INSERT INTO CheckIns (Date,Receipt,Name,Phone,Address,isAdult,Gender,Room,CheckInDate,CheckInTime,ToCheckOutDate,ToCheckOutTime,CheckedOutDate,CheckedOutTime,Days) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); ";
        boolean isSuccessful = false;
        Connection con = null;
        try {
            con = ConnectDB.Main();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, data.get(0));
            ps.setObject(2, data.get(1));
            ps.setObject(3, data.get(2));
            ps.setObject(4, data.get(3));
            ps.setObject(5, data.get(4));
            ps.setObject(6, data.get(5));
            ps.setObject(7, data.get(6));
            ps.setObject(8, data.get(7));
            ps.setObject(9, data.get(8));
            ps.setObject(10, data.get(9));
            ps.setObject(11, data.get(10));
            ps.setObject(12, data.get(11));
            ps.setObject(13, data.get(12));
            ps.setObject(14, data.get(13));
            ps.setObject(15, data.get(14));
            ps.execute();
            isSuccessful = true;
            con.close();
        } catch (Exception e) {
            assert con != null;
            con.close();
            e.printStackTrace();
        }
        return isSuccessful;
    }

    public static boolean InsertReceipt(ObservableList<String> data, ObservableList<Map<String, String>> occupants) throws SQLException {
        String sql = "INSERT INTO Receipts (Date,Name,Phone,Address,isAdult,Gender,Room,CheckInDate,CheckInTime,ToCheckOutDate,ToCheckOutTime,CheckedOutDate,CheckedOutTime,Occupants,Days,Rate,Total,Paid,Method,Balance) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); ";
        boolean isSuccessful = false;
        Connection con =null;
        try {
            con = ConnectDB.Main();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, data.get(0));
            ps.setObject(2, data.get(1));
            ps.setObject(3, data.get(2));
            ps.setObject(4, data.get(3));
            ps.setObject(5, data.get(4));
            ps.setObject(6, data.get(5));
            ps.setObject(7, data.get(6));
            ps.setObject(8, data.get(7));
            ps.setObject(9, data.get(8));
            ps.setObject(10, data.get(9));
            ps.setObject(11, data.get(10));
            ps.setObject(12, data.get(11));
            ps.setObject(13, data.get(12));
            ps.setObject(14, data.get(13));
            ps.setObject(15, data.get(14));
            ps.setObject(16, data.get(15));
            ps.setObject(17, data.get(16));
            ps.setObject(18, data.get(17));
            ps.setObject(19, data.get(18));
            ps.setObject(20, data.get(19));
            ps.execute();
            ps.close();

            sql = "INSERT INTO Payments (Date,Receipt,Name,Room,Time,Total,Paid,Balance,Method) VALUES(?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setObject(1, data.get(0));
            ps.setObject(2, data.get(20));
            ps.setObject(3, data.get(1));
            ps.setObject(4, data.get(6));
            ps.setObject(5, data.get(8));
            ps.setObject(6, data.get(16));
            ps.setObject(7, data.get(17));
            ps.setObject(8, data.get(19));
            ps.setObject(9, data.get(18));
            ps.execute();
            ps.close();

            if(occupants.size() > 0){
                for (Map<String, String> occupant : occupants) {
                    sql = "INSERT INTO Occupants (Date,Name,Phone,Address,IsAdult,Gender,Room,CheckInDate,CheckInTime,ToCheckOutDate,ToCheckOutTime,CheckedOutDate,CheckedOutTime,Days,Receipt) " +
                            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    ps = con.prepareStatement(sql);
                    ps.setObject(1, data.get(0));
                    ps.setObject(2, MainClass.returnTitleCase(occupant.get("FName") + " " + MainClass.returnTitleCase(occupant.get("LName"))));
                    ps.setObject(3,Objects.equals(occupant.get("Phone"), "") ? data.get(2) : occupant.get("Phone"));
                    ps.setObject(4, Objects.equals(occupant.get("Address"), "") ? data.get(3) : occupant.get("Address"));
                    ps.setObject(5, occupant.get("isAdult"));
                    ps.setObject(6, occupant.get("Gender"));
                    ps.setObject(7, data.get(6));
                    ps.setObject(8, data.get(7));
                    ps.setObject(9, data.get(8));
                    ps.setObject(10, data.get(9));
                    ps.setObject(11, data.get(10));
                    ps.setObject(12, data.get(11));
                    ps.setObject(13, data.get(12));
                    ps.setObject(14, data.get(14));
                    ps.setObject(15, data.get(20));
                    ps.execute();

                }
                ps.close();


            }

            isSuccessful = true;
            con.close();

        } catch (Exception e) {
            con.close();
            e.printStackTrace();
        }
        return isSuccessful;
    }

    public static ObservableList<String> getCheckOut(String sql, String[] columns) throws Exception {
        ObservableList<String> Data = FXCollections.observableArrayList();
        Connection con;
        con = ConnectDB.Main();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

                Data.add(rs.getString(columns[0])+"  Room:"+rs.getString(columns[1]));

        }
        con.close();
        return Data;
    }

    public static Map<String, String> getCheckOutData(String name, int room) throws  Exception {
        String sql = "SELECT Name,Phone,Address,Gender,isAdult,Room,Days,CheckInDate,CheckInTime,ToCheckOutDate,ToCheckOutTime,Receipt FROM (SELECT * FROM Occupants WHERE CheckedOutDate = 'NO') WHERE  Name = '"+name+"' AND Room = "+room+"";
        if(name.equalsIgnoreCase("")){
            sql = "SELECT Name,Phone,Address,Gender,isAdult,Room,Days,CheckInDate,CheckInTime,ToCheckOutDate,ToCheckOutTime,Receipt FROM (SELECT * FROM Occupants WHERE CheckedOutDate = 'NO') WHERE Room = "+room+"";
        }

        String sql2 = "SELECT Name,Type,Rate FROM RoomList WHERE Number = "+room+" ";

        Connection con = ConnectDB.Main();
        Map<String, String> Data = new HashMap<>();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            Data.put("Name", rs.getString("Name"));
            Data.put("Phone", rs.getString("Phone"));
            Data.put("Address", rs.getString("Address"));
            Data.put("Gender", rs.getString("Gender"));
            Data.put("isAdult", rs.getString("isAdult"));
            Data.put("Room", rs.getString("Room"));
            Data.put("Days", rs.getString("Days"));
            Data.put("CheckInDate", rs.getString("CheckInDate"));
            Data.put("CheckInTime", rs.getString("CheckInTime"));
            Data.put("ToCheckOutDate", rs.getString("ToCheckOutDate"));
            Data.put("ToCheckOutTime", rs.getString("ToCheckOutTime"));
            Data.put("Receipt", rs.getString("Receipt"));
        }
        System.out.println("checkin Data "+Data);
        rs.close();
        ps.close();

        String sql3 = "SELECT Occupants,Total,Paid,Method,Balance FROM Receipts WHERE Receipt = "+Data.get("Receipt")+" ";


        ps = con.prepareStatement(sql2);
        rs = ps.executeQuery();
        if(rs.next()){
            Data.put("RoomName", rs.getString("Name"));
            Data.put("Type", rs.getString("Type"));
            Data.put("Rate", rs.getString("Rate"));
        }
        rs.close();
        ps.close();

        ps = con.prepareStatement(sql3);
        rs = ps.executeQuery();
        if(rs.next()){
            Data.put("Occupants", rs.getString("Occupants"));
            Data.put("Total", rs.getString("Total"));
            Data.put("Paid", rs.getString("Paid"));
            Data.put("Method", rs.getString("Method"));
            Data.put("Balance", rs.getString("Balance"));
        }
        rs.close();
        ps.close();
        con.close();

        return Data;
    }



    public static boolean InsertPayment(ObservableList<String> data) throws Exception {
        boolean isSuccessful = false;
        try {
//            (Date,Receipt,Name,Room,Time,Total,Paid,Balance,Method
            String sql;
            Connection con = ConnectDB.Main();
            sql = "INSERT INTO Payments (Date,Receipt,Name,Room,Time,Total,Paid,Balance,Method) VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, data.get(0));
            ps.setObject(2, data.get(1));
            ps.setObject(3, data.get(2));
            ps.setObject(4, data.get(3));
            ps.setObject(5, data.get(4));
            ps.setObject(6, data.get(5));
            ps.setObject(7, data.get(6));
            ps.setObject(8, data.get(7));
            ps.setObject(9, data.get(8));
            ps.execute();
            ps.close();
            isSuccessful = true;
            con.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

       return isSuccessful;
    }

    public static boolean EditPayment(ObservableList<String> data) throws Exception {
        boolean isSuccessful = false;
        try {
//            (Date,Receipt,Name,Room,Total,Paid
            String sql;
            Connection con = ConnectDB.Main();
            sql = "DELETE FROM Payments WHERE  Date = ? AND Receipt = ? AND Name = ? AND Room = ? AND Total = ? AND  Paid = ?     ";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, data.get(0));
            ps.setInt(2, Integer.parseInt(data.get(1)));
            ps.setObject(3, data.get(2));
            ps.setInt(4, Integer.parseInt(data.get(3)));
            ps.setInt(5, Integer.parseInt(data.get(4)));
            ps.setInt(6, Integer.parseInt(data.get(5)));
            ps.execute();
            ps.close();
            isSuccessful = true;
            con.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

        return isSuccessful;
    }

    public static boolean UpdateRoomList(ObservableList<String> data) throws Exception {
        boolean isSuccessful = false;
        try {
            String sql = "SELECT TimesBooked,DaysBooked,TotalAmount FROM RoomList WHERE Number = "+Integer.parseInt(data.get(8))+"";
            System.out.println(sql);
            Connection con = ConnectDB.Main();
            PreparedStatement ps =con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int DaysBooked = 0;
            int TotalAmount = 0;
            if(rs.next()){
                DaysBooked = rs.getInt("DaysBooked");
                TotalAmount = rs.getInt("TotalAmount");
            }
            rs.close();
            ps.close();

            sql ="UPDATE RoomList SET " +
                    "TimesBooked = TimesBooked +1 , " +
                    "DaysBooked = ? + "+DaysBooked+", " +
                    "TotalAmount = ? + "+TotalAmount+", " +
                    "CheckInDate = ?, CheckInTime = ?, ToCheckOutDate = ?,ToCheckOutTime = ?,Status = 'Occupied', Occupant = ? WHERE Number = "+Integer.parseInt(data.get(8))+"";
            System.out.println(sql);
            ps = con.prepareStatement(sql);
            ps.setObject(1,Integer.parseInt(data.get(0)));
            ps.setObject(2,Integer.parseInt(data.get(1)));
            ps.setObject(3,data.get(3));
            ps.setObject(4,data.get(4));
            ps.setObject(5,data.get(5));
            ps.setObject(6,data.get(6));
            ps.setObject(7,data.get(7));
            ps.execute();
            con.close();
            isSuccessful = true;
        } catch (Exception e) {
           e.printStackTrace();
        }
        return isSuccessful;
    }

    public static int getReceipt() throws  Exception {
        int newReceipt = 1;
        Connection con = ConnectDB.Main();
        String sql = "SELECT Receipt+1 as Receipt FROM Receipts ORDER BY Receipt Desc LIMIT 1";
        PreparedStatement ps =  con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            newReceipt = rs.getInt("Receipt");
        }
        con.close();

        return newReceipt;
    }


    public static boolean setCheckOut(String name, int room) throws Exception{
        boolean isSuccessful = false;
        Connection con = ConnectDB.Main();
        try {
            String sql = "UPDATE Checkins SET CheckedOutDate = ? , CheckedOutTime = ? WHERE Name = ? AND  Room = ?";
            System.out.println(sql);
            String Date = LocalDate.now().format(MainClass.DatabaseDateFormat);
            String Time = LocalTime.now().format(MainClass.timeFormatter);
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, Date);
            ps.setObject(2, Time);
            ps.setObject(3, name);
            ps.setObject(4, room);
            ps.execute();
            sql = "UPDATE Occupants SET CheckedOutDate = ? , CheckedOutTime = ? WHERE Name = ? AND  Room = ?";
            ps = con.prepareStatement(sql);
            ps.setObject(1, Date);
            ps.setObject(2, Time);
            ps.setObject(3, name);
            ps.setObject(4, room);
            ps.execute();
            sql = "UPDATE Receipts SET CheckedOutDate = ? , CheckedOutTime = ? WHERE Name = ? AND  Room = ?";
            ps = con.prepareStatement(sql);
            ps.setObject(1, Date);
            ps.setObject(2, Time);
            ps.setObject(3, name);
            ps.setObject(4, room);
            ps.execute();
            sql = "UPDATE RoomList SET CheckedOutDate = ? , CheckedOutTime = ?, Status = 'Available', Occupant='None' WHERE Number = ?";
            ps = con.prepareStatement(sql);
            ps.setObject(1, Date);
            ps.setObject(2, Time);
            ps.setObject(3, room);
            ps.execute();
            con.close();
            isSuccessful = true;
        } catch (Exception e) {
            con.close();
e.printStackTrace();
        }


        return isSuccessful;
    }

    public static boolean addGuest(ObservableList<String> data) throws Exception{
        boolean isSuccessful = false;
        try {
            Connection con = ConnectDB.Main();
            String sql = "UPDATE Receipts SET Occupants = Occupants + 1 WHERE CheckedOutDate = 'NO' AND Room = "+data.get(7)+"";
            con.prepareStatement(sql);
            PreparedStatement ps = con.prepareStatement(sql);
            ps.execute();

             sql = "INSERT INTO Occupants (Date,Name,Phone,Address,IsAdult,Gender,Room,CheckInDate,CheckInTime,ToCheckOutDate,ToCheckOutTime,CheckedOutDate,CheckedOutTime,Days,Receipt) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
             ps = con.prepareStatement(sql);
            ps.setObject(1, data.get(0));
            ps.setObject(2, MainClass.returnTitleCase(data.get(1) + " " + MainClass.returnTitleCase(data.get(2))));
            ps.setObject(3,data.get(3));
            ps.setObject(4, data.get(4));
            ps.setObject(5, data.get(5));
            ps.setObject(6, data.get(6));
            ps.setObject(7, data.get(7));
            ps.setObject(8, data.get(8));
            ps.setObject(9, data.get(9));
            ps.setObject(10, data.get(10));
            ps.setObject(11, data.get(11));
            ps.setObject(12, data.get(12));
            ps.setObject(13, data.get(13));
            ps.setObject(14, data.get(14));
            ps.setObject(15, data.get(15));
            ps.execute();



            con.close();
            isSuccessful = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccessful;
    }
}
