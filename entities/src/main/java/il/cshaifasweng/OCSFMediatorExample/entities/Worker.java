package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;

@Entity
@Table(name="workers")
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int privilege;

    @Column(name="govId", unique = true, nullable=false)
    private String govId;

    private String fullName;
    private String password;

    public Worker(int privilege, String govId, String fullName, String password) {
        this.privilege = privilege;
        this.govId = govId;
        this.fullName = fullName;
        this.password = password;
    }
}
