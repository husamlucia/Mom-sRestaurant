package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OccupationMap implements Serializable {
    List<SimpleTable> tables;


    public OccupationMap(){
        this.tables = new ArrayList<>();
    }

    public List<SimpleTable> getTables() {
        return tables;
    }

    public void setTables(List<SimpleTable> tables) {
        this.tables = tables;
    }

    void addTable(SimpleTable table){
        this.tables.add(table);
    }
}
