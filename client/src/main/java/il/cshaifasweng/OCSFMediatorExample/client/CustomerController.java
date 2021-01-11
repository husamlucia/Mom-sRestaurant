package il.cshaifasweng.OCSFMediatorExample.client;


import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.Meal;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    String buyMessage;
    int buyPrice;
    @FXML
    private TableView<Branch> brTable;

    @FXML
    private TableColumn colBrId;

    @FXML
    private TableColumn colOpenH;

    @FXML
    private TableColumn colCloseH;



    @FXML
    private TableView<Meal> menuTable;

    @FXML
    private TableColumn nameCol;

    @FXML
    private TableColumn ingredientsCol;

    @FXML
    private TableColumn priceCol;

    @FXML
    private TableColumn idCol;



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
    void showMenu(ActionEvent event) {
        try {
            Branch br = brTable.getSelectionModel().getSelectedItem();
            int branchID = br!=null?br.getId():0;
            String message = "#requestMenu " + Integer.toString(branchID);
            SimpleClient.getClient().sendToServer(message);
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
    @FXML
    void addToCart(ActionEvent event) {
        Meal meal = menuTable.getSelectionModel().getSelectedItem();
        buyMessage += meal.getId() + ' ';
    }

    public void initialize(URL url, ResourceBundle rb) {
        EventBus.getDefault().register(this);
        buyMessage = "order ";
        buyPrice = 0;
        //each cellValueFactory has been set according to the member variables of your entity class
        colBrId.setCellValueFactory(new PropertyValueFactory<Branch, Integer>("id"));
        colOpenH.setCellValueFactory(new PropertyValueFactory<Branch, String>("openHours"));
        colCloseH.setCellValueFactory(new PropertyValueFactory<Branch, String>("closeHours"));


        nameCol.setCellValueFactory(new PropertyValueFactory<Meal, String>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Meal, Double>("price"));
        ingredientsCol.setCellValueFactory(new PropertyValueFactory<Meal, List<String>>("ingredients"));

        try{
            SimpleClient.getClient().sendToServer("#getAllBranches");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onMenuEvent(MenuEvent event){
        Platform.runLater(()->{
            ObservableList<Meal> mealList = FXCollections.observableArrayList();
            mealList.addAll(event.getMenu().getMeals());
            menuTable.setItems(mealList);
        });
    }

    @Subscribe
    public void onBranchEvent(BranchEvent event){
        Platform.runLater(()->{
            ObservableList<Branch> branchList = FXCollections.observableArrayList();
            branchList.addAll(event.getBranches().getBranches());
            brTable.setItems(branchList);
        });
    }

}
