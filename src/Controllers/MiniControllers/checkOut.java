package Controllers.MiniControllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class checkOut {
    public final SimpleIntegerProperty Id;
    public final SimpleStringProperty col1;
    public final SimpleBooleanProperty checked;

    public checkOut(int id, String col1, boolean checked) {
        Id = new SimpleIntegerProperty(id);
        this.col1 = new SimpleStringProperty(col1);
        this.checked = new SimpleBooleanProperty(checked);
    }

    public int getId() {
        return Id.get();
    }

    public void setId(int id) {
        this.Id.set(id);
    }

    public String getCol1() {
        return col1.get();
    }

    public void setCol1(String col1) {
        this.col1.set(col1);
    }

    public boolean getChecked() {
        return checked.get();
    }

    public void setChecked(boolean checked) {
        this.checked.set(checked);
    }

}
