package il.cshaifasweng.OCSFMediatorExample.entities;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="maps")
public class Mapp implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="map_id")
    private int id;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="branch_id")
    private Branch branch;

    @OneToMany(mappedBy = "map",cascade=CascadeType.ALL)
    private List<Tablee> tables;

    private String area;

    private int maxCapacity;


    public Mapp(Branch br, String area) {
        this.branch = br;
        this.tables = new ArrayList<>();
        this.area = area;
        this.maxCapacity=0;
        br.setMap(area, this);
    }

    public Mapp(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public void addTable(Tablee table){
        this.tables.add(table);
        maxCapacity += table.getCapacity();
    }


    public List<Tablee> getTables() {
        return tables;
    }

    public void setTables(List<Tablee> tables) {
        this.tables = tables;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
}
