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
    void activateFields(ActionEvent event) {
        nameTF.setEnabled(true);
        ingredientsTF.setEnabled(true);
        priceTF.setEnabled(true);
        branchIdTF.setEnabled(true);
    }

    @FXML
    void addMeal(ActionEvent event) {
        String Name, Ingredients;
        int brId;
        double price;
        Name = nameTF.getText();
        Ingredients = ingredientsTF.getText();
        brId = Integer.parseInt(branchIdTF.getText());
        price = Double.parseDouble(priceTF.getText());

        String msg;
        msg = brId + Name + price + Ingredients;
        try {
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }




    }

    @FXML
    void editMeal(ActionEvent event) {

    }

    @FXML
    void goBack(ActionEvent event) {

    }

    @FXML
    void removeMeal(ActionEvent event) {

    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }


}