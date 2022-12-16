package Controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SettingsController {
    public JFXButton EditRoomBut;


    public void actionEvent(ActionEvent actionEvent) {
        if(actionEvent.getSource().equals(EditRoomBut)){
            try {
                Stage stage = new Stage();
                FXMLLoader Loaders = new FXMLLoader();
                Parent root1 = Loaders.load(getClass().getResource("/SubPanes/EditRoom.fxml").openStream());
                Scene scene = new Scene(root1);
                stage.setScene(scene);
                stage.initStyle(StageStyle.UTILITY);
                stage.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();

            }
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
}
