package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.Meal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;

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
    private TableColumn<Branch, Integer> brIdBranchesCol;

    @FXML
    private TableColumn<Branch, String> openHourBranchesCol;

    @FXML
    private TableColumn<Branch, String> CloseHourBranchesCol;

    @FXML
    private TableView<Meal> menuTable;


    @FXML
    private TableColumn<Meal, Integer> idMenuCol;

    @FXML
    private TableColumn<Meal, String> nameMenuCol;

    @FXML
    private TableColumn<Meal, List<String>> ingredientsMenuCol;

    @FXML
    private TableColumn<?, ?> picMenuCol;


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
        Meal meal = menuTable.getSelectionModel().getSelectedItem();
        int mealId = meal.getId();
        String  msg = "#approveMeal " + mealId;
        try {
            SimpleClient.getClient().sendToServer(msg);
            getAllMeals();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }



    @FXML
    void denyMeal(ActionEvent event) {
        Meal meal = menuTable.getSelectionModel().getSelectedItem();
        int mealId = meal.getId();
        String  msg = "#denyMeal " + mealId;
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

        // App.setRoot("lockDown");
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

        try {
            String message = "#requestComplaints " + branchesTable.getSelectionModel().getSelectedItem().getId();
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

        try {
            String message = "#requestMap " + branchesTable.getSelectionModel().getSelectedItem().getId();
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
        EventBus.getDefault().register(this);
        //each cellValueFactory has been set according to the member variables of your entity class
        brIdBranchesCol.setCellValueFactory(new PropertyValueFactory<Branch, Integer>("id"));
        openHourBranchesCol.setCellValueFactory(new PropertyValueFactory<Branch, String>("openHours"));
        CloseHourBranchesCol.setCellValueFactory(new PropertyValueFactory<Branch, String>("closeHours"));


        try{
            SimpleClient.getClient().sendToServer("#getAllBranches");
        }catch(IOException e){
            e.printStackTrace();
        }


    }
    public void initialize2(int privilege){

    }
}
