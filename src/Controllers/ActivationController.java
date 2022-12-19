package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ActivationController implements Initializable {
    public AnchorPane rootPane;
    public JFXTextField Field;
    public JFXButton Button;
    FileWriter writer;
    LocalDate expirationdate = LocalDate.of(2024, 1, 20);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootPane.getStylesheets().remove(0);
        rootPane.getStylesheets().add("file:/"+System.getProperty("user.home").replace("\\", "/" ) + "/currentTheme.css");
    }


    public void actionEvent() throws IOException {
        File activation = new File(System.getProperty("user.home") + "/AppData/Local/LouisianaActivation.txt");
        activation.delete();

        if(Field.getText().contains("alpha")){
            expirationdate = expirationdate.plusYears(1);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful");
            alert.setHeaderText(null);
            alert.setContentText("Activation Code Successful \n  Thanks for your patronage");
            alert.show();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wrong");
            alert.setHeaderText(null);
            alert.setContentText("Activation Code not Correct");
            alert.show();
        }

        if (activation.createNewFile()) {
            writer = new FileWriter(activation);
            writer.write("Expiration Date: "+expirationdate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            writer.close();
        }


    }
}
