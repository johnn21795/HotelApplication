package Controllers.MiniControllers;

import Classes.MainClass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class EditRoomController implements Initializable {


    public JFXTextField RnNo;
    public JFXTextField NwRmNo;
    public JFXButton SaveBut1;
    public Label SaveTxt1;
    public JFXTextField RmTp;
    public JFXButton SaveBut2;
    public Label SaveTxt2;
    public JFXTextField RateField;
    public JFXButton SaveBut3;
    public Label SaveTxt3;
    public JFXButton FinishBut;
    public AnchorPane rootPane;
    public Label SaveTxt4;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootPane.getStylesheets().remove(0);
        rootPane.getStylesheets().add("file:/"+System.getProperty("user.home").replace("\\", "/" ) + "/MainStyle.css");
    }

    public void actionEvent(ActionEvent actionEvent) throws Exception {
        Object source = actionEvent.getSource();
        if(source.equals(NwRmNo) || source.equals(SaveBut1)){
            SaveTxt1.setVisible(true);
            NwRmNo.setDisable(true);
        }
        if(source.equals(RmTp) || source.equals(SaveBut2)){
            SaveTxt2.setVisible(true);
            RmTp.setDisable(true);
        }
        if(source.equals(RateField) || source.equals(SaveBut3)){
            SaveTxt3.setVisible(true);
            RateField.setDisable(true);
        }
        if(source.equals(FinishBut)){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            String text;
            int x;
            try {
                x = Integer.parseInt(RnNo.getText());
                if(!MainClass.getBoolean("SELECT Number FROM RoomList WHERE Number = "+x+"")){
                    text = "Create Room "+x+" ?";
                    alert.setHeaderText(null);
                    alert.setContentText(text);
                    alert.showAndWait();
                    ButtonType result2 = alert.getResult();
                    if(result2.equals(ButtonType.OK)){
                        Map<String, Object> Data = new HashMap<>();
                        Data.put("Number",x);
                        Data.put("Type",MainClass.returnTitleCase(RmTp.getText().trim()));
                        Data.put("Name",MainClass.returnTitleCase(NwRmNo.getText().isEmpty()? "Room"+x : NwRmNo.getText()));
                        Data.put("Rate",Integer.parseInt(RateField.getText()));
                        Data.put("Status","Available");
                        Data.put("Occupant","None");
                        if(MainClass.InsertDB(Data, "RoomList")){
                            SaveTxt4.setVisible(true);
                            RateField.setText("");
                            NwRmNo.setText("");
                            RmTp.setText("");
                        }
                    }else {
                        alert.close();
                    }
                }else{
                    text = "Update Room"+x+" Details? ";
                    alert.setHeaderText(null);
                    alert.setContentText(text);
                    alert.showAndWait();
                    ButtonType result2 = alert.getResult();
                    if(result2.equals(ButtonType.OK)){
                        MainClass.EditDatabase("UPDATE RoomList " +
                                "SET " +
                                "Name = '"+MainClass.returnTitleCase(NwRmNo.getText().isEmpty()? "Room"+x : NwRmNo.getText())+"', " +
                                "Type = '"+MainClass.returnTitleCase(RmTp.getText().trim())+"', "+
                                "Rate = '"+Integer.parseInt(RateField.getText())+"' "+
                                " WHERE Number = "+x+"");
                        SaveTxt4.setVisible(true);
                        RateField.setText("");
                        NwRmNo.setText("");
                        RmTp.setText("");
                    }
                    else {
                        alert.close();
                    }
                }
                MainClass.reloadRoomListTables = true;
                MainClass.reloadRecordsTables = true;
            } catch (NumberFormatException e) {
                text = "Only Number Allowed";
                alert.setHeaderText(null);
                alert.setContentText(text);
                alert.showAndWait();

            }


        }
    }

    public void keyEvet(KeyEvent keyEvent) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        if(RnNo.getText().isEmpty()){
            RateField.setText("");
            NwRmNo.setText("");
            RmTp.setText("");
            return;
        }
        try {
          int  x = Integer.parseInt(RnNo.getText());
           ObservableList<String> Result = MainClass.getObservableList("SELECT Name,Type,Rate FROM RoomList WHERE Number = "+x+"", new String[]{"Name","Type","Rate"});
            if(!Result.isEmpty()){
                NwRmNo.setText(Result.get(0));
                RmTp.setText(Result.get(1));
                RateField.setText(Result.get(2));
            }else{
                RateField.setText("");
                NwRmNo.setText("");
                RmTp.setText("");
            }
        } catch (NumberFormatException e) {
                alert.setHeaderText(null);
                alert.setContentText("Only Number Allowed");
                alert.showAndWait();
        }

        SaveTxt1.setVisible(false);
        SaveTxt2.setVisible(false);
        SaveTxt3.setVisible(false);
        SaveTxt4.setVisible(false);
        RateField.setDisable(false);
        NwRmNo.setDisable(false);
        RmTp.setDisable(false);
    }
}
