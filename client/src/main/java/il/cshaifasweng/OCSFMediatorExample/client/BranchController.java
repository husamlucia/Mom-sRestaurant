package il.cshaifasweng.OCSFMediatorExample.client;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class BranchController{

    @FXML
    void book(ActionEvent event) {

    }

    @FXML
    void cancelOrder(ActionEvent event) {

    }

    @FXML
    void complain(ActionEvent event) {

    }

    @FXML
    void order(ActionEvent event) {

    }


    public void initialize2(int privilege) {
        if(privilege == 1) System.out.println("Hostess");
        if(privilege==2) System.out.println("Samer");
    }
}
