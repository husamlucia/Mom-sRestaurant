package il.cshaifasweng.OCSFMediatorExample.client;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    private int privilage;

    public CustomerController(int privilege) {
    }

    @FXML
    private Button editMealBtn;

    @FXML
    private Button bookPlaceBtn;

    @FXML
    private Button orderBtn;

    @FXML
    private Button cancelOrderBtn;

    @FXML
    private Button complainBtn;

    @FXML
    private Button addMealBtn;

    @FXML
    private Button showReportsBtn;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        switch (privilage) {
            case 1:// hostess
                bookPlaceBtn.setDisable(false);
                orderBtn.setDisable(true);
                cancelOrderBtn.setDisable(true);
                editMealBtn.setDisable(true);
                addMealBtn.setDisable(true);
                showReportsBtn.setDisable(true);
                complainBtn.setDisable(true);
                break;
            case 2:// dietnit
                bookPlaceBtn.setDisable(true);
                orderBtn.setDisable(true);
                cancelOrderBtn.setDisable(true);
                editMealBtn.setDisable(false);
                addMealBtn.setDisable(false);
                showReportsBtn.setDisable(true);
                complainBtn.setDisable(true);
                break;
            case 3://customer Service
                bookPlaceBtn.setDisable(true);
                orderBtn.setDisable(true);
                cancelOrderBtn.setDisable(true);
                editMealBtn.setDisable(true);
                addMealBtn.setDisable(true);
                showReportsBtn.setDisable(true);
                complainBtn.setDisable(true);
                break;
            case 4:
                bookPlaceBtn.setDisable(true);
                orderBtn.setDisable(true);
                cancelOrderBtn.setDisable(true);
                editMealBtn.setDisable(true);
                addMealBtn.setDisable(true);
                showReportsBtn.setDisable(false);
                complainBtn.setDisable(true);
                break;


        }
    }
}
