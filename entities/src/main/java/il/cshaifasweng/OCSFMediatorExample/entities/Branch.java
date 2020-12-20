package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name= "branch")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private List<dateTime> openningsHours;

    private Menu menu;

    String localManeger;


    public Branch() {
    }

}
