package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="workers")
public class Worker implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int privilege;

    private boolean loggedIn;

    @Column(name="govId", unique = true, nullable=false)
    private String govId;

    int branchId;

    public Worker(int privilege, String govId, int branchId, String fullName, String password) {
        this.privilege = privilege;
        this.govId = govId;
        this.branchId = branchId;
        this.fullName = fullName;
        this.password = password;
        this.loggedIn=false;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrivilege() {
        return privilege;// 1-> hostess, 2-> dietnit, 3-> customerService, 4-> mainManager, 5-> branchMan
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public String getGovId() {
        return govId;
    }

    public void setGovId(String govId) {
        this.govId = govId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String fullName;
    private String password;

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public Worker(){

    }
}
