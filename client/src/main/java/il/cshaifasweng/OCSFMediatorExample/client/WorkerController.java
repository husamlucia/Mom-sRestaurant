package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.events.BranchEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.MealUpdateEvent;
import il.cshaifasweng.OCSFMediatorExample.client.events.MenuEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.Meal;
import il.cshaifasweng.OCSFMediatorExample.entities.MealUpdate;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class WorkerController implements Initializable {

    private String mealUpdateType;
    private Meal mealToUpdate;
    private Branch branch;
    private int privilege;

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
    private TableView<MealUpdate> mealUpdatesTable;

    @FXML
    private TableColumn<MealUpdate, String> mealUpdatesID;

    @FXML
    private TableColumn<MealUpdate, String> mealUpdatesOldName;

    @FXML
    private TableColumn<MealUpdate, String> mealUpdatesNewName;

    @FXML
    private TableColumn<MealUpdate, String> mealUpdatesOldPrice;

    @FXML
    private TableColumn<MealUpdate, String> mealUpdatesNewPrice;

    @FXML
    private TableColumn mealUpdatesOldPic;

    @FXML
    private TableColumn MealUpdatesNewPic;

    @FXML
    private TableColumn<MealUpdate, String> mealUpdatesOldBranch;

    @FXML
    private TableColumn<MealUpdate, String> mealUpdatesNewBranch;



    @FXML
    private AnchorPane paneMealUpdates;

    @FXML
    private AnchorPane paneManager1;
    @FXML
    private AnchorPane paneManager2;
    @FXML
    private AnchorPane paneDietitian;
    @FXML
    private AnchorPane paneCustomerService;

    @Subscribe
    public void onMenuEvent(MenuEvent event) {
        Platform.runLater(() -> {
            ObservableList<Meal> mealList = FXCollections.observableArrayList();
            mealList.addAll(event.getMenu().getMeals());
            menuTable.setItems(mealList);
        });
    }


    @Subscribe
    public void onMealUpdateEvent(MealUpdateEvent event) {

        Platform.runLater(() -> {

        try{
            ObservableList<MealUpdate> updatesList = FXCollections.observableArrayList();
            List<MealUpdate> meals = event.getMealUpdates();
            updatesList.addAll(meals);
            mealUpdatesTable.setItems(updatesList);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        });
    }


    @Subscribe
    public void onBranchEvent(BranchEvent event) {
        Platform.runLater(() -> {
            ObservableList<Branch> branchList = FXCollections.observableArrayList();
            branchList.addAll(event.getBranches().getBranches());
            branchesTable.setItems(branchList);
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventBus.getDefault().register(this);

        //each cellValueFactory has been set according to the member variables of your entity class
        brIdBranchesCol.setCellValueFactory(new PropertyValueFactory<Branch, Integer>("id"));
        openHourBranchesCol.setCellValueFactory(new PropertyValueFactory<Branch, String>("openHours"));
        CloseHourBranchesCol.setCellValueFactory(new PropertyValueFactory<Branch, String>("closeHours"));


        idMenuCol.setCellValueFactory(new PropertyValueFactory<Meal, Integer>("id"));
        nameMenuCol.setCellValueFactory(new PropertyValueFactory<Meal, String>("name"));
        priceMenuCol.setCellValueFactory(new PropertyValueFactory<Meal, Double>("price"));
        ingredientsMenuCol.setCellValueFactory(new PropertyValueFactory<Meal, List<String>>("ingredients"));


        mealUpdatesID.setCellValueFactory(cellData -> new SimpleStringProperty(
                Integer.toString(cellData.getValue().getOldMeal()!=null?cellData.getValue().getOldMeal().getId():0)));

        mealUpdatesOldName.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getOldMeal()!=null?cellData.getValue().getOldMeal().getName():""));

        mealUpdatesNewName.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getNewMeal()!=null?cellData.getValue().getNewMeal().getName():""));

        mealUpdatesOldPrice.setCellValueFactory(cellData -> new SimpleStringProperty(
                Double.toString(cellData.getValue().getOldMeal()!=null?cellData.getValue().getOldMeal().getPrice():0)));
        mealUpdatesNewPrice.setCellValueFactory(cellData -> new SimpleStringProperty(
                Double.toString(cellData.getValue().getNewMeal()!=null?cellData.getValue().getNewMeal().getPrice():0)));
        mealUpdatesOldBranch.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(
                cellData.getValue().getBranch()!=null?cellData.getValue().getBranch().getId():0)));
        mealUpdatesNewBranch.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getNewBranchId())));


        try {
            SimpleClient.getClient().sendToServer("#getAllBranches");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void initialize2(int privilege) {
        this.privilege = privilege;
        switch(privilege){
            case 1:
                paneDietitian.setVisible(false);
                paneManager1.setVisible(false);
                paneManager2.setVisible(false);
                paneCustomerService.setVisible(false);
                paneMealUpdates.setVisible(false);
                break;
            case 2:
                paneManager1.setVisible(false);
                paneManager2.setVisible(false);
                paneCustomerService.setVisible(false);
                paneMealUpdates.setVisible(false);
            case 3:
                paneDietitian.setVisible(false);
                paneManager1.setVisible(false);
                paneManager2.setVisible(false);
                paneMealUpdates.setVisible(false);
            case 4:
                paneCustomerService.setVisible(false);
                paneDietitian.setVisible(false);
        }

    }

    @FXML
    void addMeal(ActionEvent event) {
        nameTF.setDisable(false);
        ingredientsTF.setDisable(false);
        priceTF.setDisable(false);
        branchIdTF.setDisable(false);
        newMealCheckBox.setSelected(true);
        mealUpdateType = "add";
        mealToUpdate = null;
    }


    @FXML
    void approveMeal(ActionEvent event) {
        MealUpdate mealUpdate = mealUpdatesTable.getSelectionModel().getSelectedItem();
        mealUpdate.setStatus("Approved");
        try {
            SimpleClient.getClient().sendToServer(mealUpdate);
            SimpleClient.getClient().sendToServer("#requestMenu " + branch.getId());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @FXML
    void denyMeal(ActionEvent event) {
        MealUpdate mealUpdate = mealUpdatesTable.getSelectionModel().getSelectedItem();
        mealUpdatesTable.getItems().remove(mealUpdate);
        mealUpdate.setStatus("Denied");
        try {
            SimpleClient.getClient().sendToServer(mealUpdate);

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
        branchIdTF.setDisable(false);


        Meal meal = (Meal) menuTable.getSelectionModel().getSelectedItem();

        nameTF.setText(meal.getName());
        ingredientsTF.setText(meal.getIngredients().toString());
        priceTF.setText(Double.toString(meal.getPrice()));
        branchIdTF.setText(Integer.toString(branch.getId()));
        mealUpdateType = "edit";
        mealToUpdate = meal;
    }

      @FXML
    void removeMeal(ActionEvent event) {
        Meal meal = (Meal) menuTable.getSelectionModel().getSelectedItem();
        mealUpdateType = "remove";
        mealToUpdate = meal;
        int brId = branch.getId();
        MealUpdate mealUpdate = new MealUpdate(meal, null, branch, brId);
          try {
              SimpleClient.getClient().sendToServer(mealUpdate);
          } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
          }
    }


    @FXML
    void saveMeal(ActionEvent event) {

        String name, ingTxt;
        String brId;
        Double price;
        name = nameTF.getText();
        ingTxt = ingredientsTF.getText();
        brId = branchIdTF.getText();
        price = Double.parseDouble(priceTF.getText());

        String msg;


        String[] ing = ingTxt.split("\\s+");
        List<String> ingredients = Arrays.asList(ing);

        Meal oldMeal = mealToUpdate;
        Meal newMeal = new Meal(name, price, ingredients);
        MealUpdate mealUpdate = new MealUpdate(oldMeal, newMeal, branch, Integer.parseInt(brId));
        try {
            SimpleClient.getClient().sendToServer(mealUpdate);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
        this.branch = branchesTable.getSelectionModel().getSelectedItem();

        int id = branch.getId();
        try {
            String message = "#requestMenu " + Integer.toString(id);
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
    void sendMessage(ActionEvent event) {

    }


    @FXML
    void viewUpdates(ActionEvent event) {
        this.branch = branchesTable.getSelectionModel().getSelectedItem();
        String id = Integer.toString(branch.getId());
        try {
            String message = "#requestUpdates " + id;
            SimpleClient.getClient().sendToServer(message);
            System.out.println("Sent message");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void showReports(ActionEvent actionEvent) {
    }


}
