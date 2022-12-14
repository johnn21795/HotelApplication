package Classes;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ModelClassLarge {
    private final SimpleIntegerProperty Id;
    private final SimpleStringProperty col1;
    private final SimpleStringProperty col2;
    private final SimpleStringProperty col3;
    private final SimpleStringProperty col4;
    private final SimpleStringProperty col5;
    private final SimpleStringProperty col6;
    private final SimpleStringProperty col7;
    private final SimpleStringProperty col8;
    private final SimpleStringProperty col9;
    private final SimpleStringProperty col10;
    private final SimpleStringProperty col11;
    private final SimpleStringProperty col12;
    private final SimpleStringProperty col13;
    private final SimpleStringProperty col14;
    private final SimpleStringProperty col15;
    private final SimpleStringProperty col16;
    private final SimpleStringProperty col17;
    private final SimpleStringProperty col18;
    private final SimpleStringProperty col19;
    private final SimpleStringProperty col20;
    private final SimpleStringProperty col21;

    private final SimpleBooleanProperty checked;



    public ModelClassLarge(int id, String col1, String col2, String col3, String col4, String col5, String col6, String col7, String col8, String col9, String col10, String col11, String col12, String col13, String col14, String col15, String col16, String col17, String col18, String col19, String col20, String col21, boolean checked){
        Id = new SimpleIntegerProperty(id);
        this.col1 = new SimpleStringProperty(col1);
        this.col2 = new SimpleStringProperty(col2);
        this.col3 = new SimpleStringProperty(col3);
        this.col4 = new SimpleStringProperty(col4);
        this.col5 = new SimpleStringProperty(col5);
        this.col6 = new SimpleStringProperty(col6);
        this.col7 = new SimpleStringProperty(col7);
        this.col8 = new SimpleStringProperty(col8);
        this.col9 = new SimpleStringProperty(col9);
        this.col10 = new SimpleStringProperty(col10);
        this.col11 = new SimpleStringProperty(col11);
        this.col12 =  new SimpleStringProperty(col12);
        this.col13 =  new SimpleStringProperty(col13);
        this.col14 =  new SimpleStringProperty(col14);
        this.col15 =  new SimpleStringProperty(col15);
        this.col16 =  new SimpleStringProperty(col16);
        this.col17 =  new SimpleStringProperty(col17);
        this.col18 =  new SimpleStringProperty(col18);
        this.col19 =  new SimpleStringProperty(col19);
        this.col20 =  new SimpleStringProperty(col20);
        this.col21 =  new SimpleStringProperty(col21);
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

    public String getCol2() {
        return col2.get();
    }

    public void setCol2(String col2) {
        this.col2.set(col2);
    }

    public String getCol3() {
        return col3.get();
    }


    public void setCol3(String col3) {
        this.col3.set(col3);
    }

    public String getCol4() {
        return col4.get();
    }

    public void setCol4(String col4) {
        this.col4.set(col4);
    }

    public String getCol5() {
        return col5.get();
    }

    public void setCol5(String col5) {
        this.col5.set(col5);
    }

    public String getCol6() {
        return col6.get();
    }

    public void setCol6(String col6) {
        this.col6.set(col6);
    }

    public String getCol7() {
        return col7.get();
    }


    public void setCol7(String col7) {
        this.col7.set(col7);
    }
    public String getCol8() {
        return col8.get();
    }
    public void setCol8(String col8) {
        this.col8.set(col8);
    }
    public String getCol9() {
        return col9.get();
    }
    public void setCol9(String col9) {
        this.col9.set(col9);
    }
    public String getCol10() {
        return col10.get();
    }
    public void setCol10(String col10) {
        this.col10.set(col10);
    }

    public String getCol11() {
        return col11.get();
    } public void setCol11(String col11) {
        this.col11.set(col11);
    }
    public String getCol12() {
        return col12.get();
    } public void setCol12(String col12) {
        this.col12.set(col12);
    }
    public String getCol13() {
        return col13.get();
    } public void setCol13(String col13) {
        this.col13.set(col13);
    }
    public String getCol14() {
        return col14.get();
    } public void setCol14(String col14) {
        this.col14.set(col14);
    }
    public String getCol15() {
        return col15.get();
    } public void setCol15(String col15) {
        this.col15.set(col15);
    }
    public String getCol16() {
        return col16.get();
    } public void setCol16(String col16) {
        this.col16.set(col16);
    }
    public String getCol17() {
        return col17.get();
    } public void setCol17(String col17) {
        this.col17.set(col17);
    }
    public String getCol18() {
        return col18.get();
    } public void setCol18(String col18) {
        this.col18.set(col18);
    }
    public String getCol19() {
        return col19.get();
    } public void setCol19(String col19) {
        this.col19.set(col19);
    }
    public String getCol20() {
        return col20.get();
    } public void setCol20(String col20) {
        this.col20.set(col20);
    }
    public String getCol21() {
        return col21.get();
    } public void setCol21(String col21) {
        this.col21.set(col21);
    }

    public boolean getChecked() {
        return checked.get();
    }
    public void setChecked(boolean checked) {
        this.checked.set(checked);
    }
    public BooleanProperty checkedProperty(){
        return checked;
    }
}
