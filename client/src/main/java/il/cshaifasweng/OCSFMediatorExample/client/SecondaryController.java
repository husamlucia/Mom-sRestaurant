package il.cshaifasweng.OCSFMediatorExample.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private TextField nameTF;

    @FXML
    private TextField ingredientsTF;

    @FXML
    private TextField priceTF;

    @FXML
    private TextField branchIdTF;

    @FXML
    void addMeal(ActionEvent event) {
        nameTF.setText("");
        ingredientsTF.setText("");
        priceTF.setText("");
        branchIdTF.setText("");
        nameTF.setEnabled(true);
        ingredientsTF.setEnabled(true);
        priceTF.setEnabled(true);
        branchIdTF.setEnabled(true);
    }

    @FXML
    void save(ActionEvent event) {
        String Name, Ingredients;
        int brId;
        double price;
        Name = nameTF.getText();
        Ingredients = ingredientsTF.getText();
        brId = Integer.parseInt(branchIdTF.getText());
        price = Double.parseDouble(priceTF.getText());

        String msg;

        msg = "#addMeal " + ' ' + brId + ' ' + Name + ' ' + price + ' ' + Ingredients;

        try {
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }




    }

    @FXML
    void editMeal(ActionEvent event) {
        nameTF.setEnabled(true);
        ingredientsTF.setEnabled(true);
        priceTF.setEnabled(true);
        branchIdTF.setEnabled(true);
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        App.setRoot("primary");
    }

    @FXML
    void removeMeal(ActionEvent event) {
        int brId = Integer.parseInt(branchIdTF.getText());
        String  msg = "#removeMeal"+  " "+brId;
        try {
            SimpleClient.getClient().sendToServer(msg);//hihi
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

//    @FXML
//    private void switchToPrimary() throws IOException {
//        App.setRoot("primary");
//    }


}