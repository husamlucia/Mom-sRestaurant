package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.Meal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class SecondaryController implements Initializable {
    @FXML
    private TableView<Meal> menuTable1;

    @FXML
    private TableColumn menuColID1;

    @FXML
    private TableColumn menuColName1;

    @FXML
    private TableColumn menuColPrice1;

    @FXML
    private TableColumn menuColIng1;

    @FXML
    private TextField nameTF;

    @FXML
    private TextField ingredientsTF;

    @FXML
    private TextField priceTF;

    @FXML
    private TextField branchIdTF;

    @FXML
    private CheckBox newMealCheckBox;


    public void initialize(URL url, ResourceBundle rb) {
        //each cellValueFactory has been set according to the member variables of your entity class
        menuColID1.setCellValueFactory(new PropertyValueFactory<Meal, Integer>("id"));
        menuColName1.setCellValueFactory(new PropertyValueFactory<Meal, String>("name"));
        menuColPrice1.setCellValueFactory(new PropertyValueFactory<Meal, Double>("price"));
        menuColIng1.setCellValueFactory(new PropertyValueFactory<Meal, List<String>>("ingredients"));

        getAllMeals();
    }

    public void getAllMeals(){
        try {
            SimpleClient.getClient().sendToServer("#getAllMeals");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    void addMeal(ActionEvent event) {
        nameTF.setDisable(false);
        ingredientsTF.setDisable(false);
        priceTF.setDisable(false);
        branchIdTF.setDisable(false);
        newMealCheckBox.setSelected(true);
    }

    @FXML
    void save(ActionEvent event) {
        String Name, Ingredients;
        String brId;
        String price;
        Name = nameTF.getText();
        Ingredients = ingredientsTF.getText();
        brId = branchIdTF.getText();
        price = priceTF.getText();

        String msg;

        if(newMealCheckBox.isSelected()){
            msg = "#addMeal " + brId + ' ' + Name + ' ' + price + ' ' + Ingredients;
        }
        else{
            msg = "#updateMeal " + brId + ' ' + Name + ' ' + price + ' ' + Ingredients;
        }


        try {
            SimpleClient.getClient().sendToServer(msg);
            getAllMeals();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @FXML
    void editMeal(ActionEvent event) {
        nameTF.setDisable(false);
        ingredientsTF.setDisable(false);
        priceTF.setDisable(false);
        branchIdTF.setDisable(true);
        Meal meal = menuTable1.getSelectionModel().getSelectedItem();

        nameTF.setText(meal.getName());
        ingredientsTF.setText(meal.getIngredients().toString());
        priceTF.setText(Double.toString(meal.getPrice()));
        branchIdTF.setText(Integer.toString(meal.getId()));
        newMealCheckBox.setSelected(false);
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        App.setRoot("primary");
    }

    @FXML
    void removeMeal(ActionEvent event) {
        Meal meal = menuTable1.getSelectionModel().getSelectedItem();
        int brId = meal.getId();
        String  msg = "#removeMeal " + brId;
        try {
            SimpleClient.getClient().sendToServer(msg);
            getAllMeals();

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