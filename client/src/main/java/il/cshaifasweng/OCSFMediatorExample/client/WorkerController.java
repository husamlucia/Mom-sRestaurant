package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.Meal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class WorkerController implements Initializable {

    @FXML
    private CheckBox newMealCheckBox;



    @FXML
    private TableView<Branch> branchesTable;

    @FXML
    private TableColumn<?, ?> brIdBranchesCol;

    @FXML
    private TableColumn<?, ?> openHourBranchesCol;

    @FXML
    private TableColumn<?, ?> CloseHourBranchesCol;

    @FXML
    private TableView<?> menuTable;


    @FXML
    private TableColumn<Meal, Integer> idMenuCol;

    @FXML
    private TableColumn<Meal, String> nameMenuCol;

    @FXML
    private TableColumn<Meal, List<String>> ingredientsMenuCol;

    @FXML
    private TableColumn<?, ?> picMenuCol;

    @FXML
    private TableColumn<?, ?> networkMealMenuCol;

    @FXML
    private TableColumn<Meal, Double> priceMenuCol;

    @FXML
    private TableColumn<?, ?> waitingApprovalMenuCol;

    @FXML
    private Button showMenuBtn;

    @FXML
    private Button showComplaintsBtn;

    @FXML
    private TableView<?> complaintsTable;

    @FXML
    private TableColumn<?, ?> numOfComplainComplaintsCol;

    @FXML
    private TableColumn<?, ?> descriptionComplaintsCol;

    @FXML
    private Button lockdownInstructionBtn;

    @FXML
    private Button refundBtn;

    @FXML
    private Button sendMessageBtn;

    @FXML
    private Button approveBtn;

    @FXML
    private Button addMealBtn;

    @FXML
    private Button removeMealBtn;

    @FXML
    private Button editMealBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private TextField nameTF;

    @FXML
    private TextField ingredientsTF;

    @FXML
    private TextField priceTF;

    @FXML
    private TextField branchIdTF;

    @FXML
    private Button denyBtn;

    @FXML
    private Button showRestaurantMapBtn;

    @FXML
    private TableView<?> retaurantMapTable;

    @FXML
    private TableColumn<?, ?> tableNumberMapCol;

    @FXML
    private TableColumn<?, ?> numOfSeatsMapCol;

    @FXML
    private TableColumn<?, ?> reservationsMapCol;

    @FXML
    void addMeal(ActionEvent event) {
        nameTF.setDisable(false);
        ingredientsTF.setDisable(false);
        priceTF.setDisable(false);
        branchIdTF.setDisable(false);
        newMealCheckBox.setSelected(true);
    }

    @FXML
    void approveMeal(ActionEvent event) {

    }



    @FXML
    void denyMeal(ActionEvent event) {

    }

    @FXML
    void editMeal(ActionEvent event) {
        nameTF.setDisable(false);
        ingredientsTF.setDisable(false);
        priceTF.setDisable(false);
        branchIdTF.setDisable(true);
        Meal meal = (Meal) menuTable.getSelectionModel().getSelectedItem();

        nameTF.setText(meal.getName());
        ingredientsTF.setText(meal.getIngredients().toString());
        priceTF.setText(Double.toString(meal.getPrice()));
        branchIdTF.setText(Integer.toString(meal.getId()));
        newMealCheckBox.setSelected(false);
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
            App.setRoot("login");
    }

    @FXML
    void goToLockdownInstructions(ActionEvent event) {

    }

    @FXML
    void refund(ActionEvent event) {

    }

    @FXML
    void removeMeal(ActionEvent event) {
        Meal meal = (Meal) menuTable.getSelectionModel().getSelectedItem();
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

    @FXML
    void requestComplaints(ActionEvent event) {

    }

    @FXML
    void requestMenu(ActionEvent event) {
        try {
            String message = "#requestMenu " + branchesTable.getSelectionModel().getSelectedItem().getId();
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    void requestRestaurantMap(ActionEvent event) {

    }

    @FXML
    void saveMeal(ActionEvent event) {
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

    private void getAllMeals() {
        try {
            SimpleClient.getClient().sendToServer("#getAllMeals");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    void sendMessage(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //each cellValueFactory has been set according to the member variables of your entity class
        idMenuCol.setCellValueFactory(new PropertyValueFactory<Meal, Integer>("id"));
        nameMenuCol.setCellValueFactory(new PropertyValueFactory<Meal, String>("name"));
        priceMenuCol.setCellValueFactory(new PropertyValueFactory<Meal, Double>("price"));
        ingredientsMenuCol.setCellValueFactory(new PropertyValueFactory<Meal, List<String>>("ingredients"));

        getAllMeals();


    }
    public void initialize2(int privilege){

    }
}
