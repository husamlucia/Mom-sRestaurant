package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.Meal;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OrderController implements Initializable {

    @FXML
    private Button orderBtn;

    String buyMessage;
    int buyPrice;

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
    private TableView<Meal> cartTable;

    @FXML
    private TableColumn cartColName;

    @FXML
    private TableColumn cartColPrice;

    @FXML
    private Label totalCost;

    @FXML
    private DatePicker orderDate;

    @FXML
    private TextField orderAddressTF;

    @FXML
    private TextField recipientTF;

    @FXML
    private TextField phoneTF1;

    @FXML
    private TextField recipientTF1;

    @FXML
    private TextField creditTF;

    @FXML
    private CheckBox differentCheckBox;

    @FXML
    private CheckBox pickupCheckBox;

    @FXML
    private TextField phoneTF;


    @FXML
    void goBack(ActionEvent event) throws IOException {
        App.setRoot("customer");
    }

    public void initialize(URL url, ResourceBundle rb) {
        pickupChecked(false);
        differentChecked(false);
        EventBus.getDefault().register(this);
        pickupCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                // TODO Auto-generated method stub
                pickupChecked(newValue);
            }
        });
        differentCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                // TODO Auto-generated method stub
                differentChecked(newValue);
            }
        });
        buyMessage = "order ";
        buyPrice = 0;
        nameCol.setCellValueFactory(new PropertyValueFactory<Meal, String>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Meal, Double>("price"));
        ingredientsCol.setCellValueFactory(new PropertyValueFactory<Meal, List<String>>("ingredients"));

        cartColName.setCellValueFactory(new PropertyValueFactory<Meal, String>("name"));
        cartColPrice.setCellValueFactory(new PropertyValueFactory<Meal, Double>("price"));
    }


    void pickupChecked(boolean newValue){
        //disabling or enabling buttons upon changing state of pickup checkbox
        //b == true -> pickup is checked
        if(newValue){

            orderAddressTF.setDisable(true);
            recipientTF.setDisable(true);
            phoneTF.setDisable(true);
            differentCheckBox.setDisable(true);
            // your checkbox has been ticked.
        }else{

            // your checkbox has been unticked. do stuff...
            // clear the config file
            orderAddressTF.setDisable(true);
            recipientTF.setDisable(true);
            phoneTF.setDisable(false);
            differentCheckBox.setDisable(false);
            differentCheckBox.setSelected(false);
        }

    }

    void differentChecked(boolean newValue){
        //disabling or enabling buttons upon changing state of pickup checkbox
        //b == true -> pickup is checked
        if(newValue){
            phoneTF.setDisable(false);
            recipientTF.setDisable(false);
            // your checkbox has been ticked.
        }else{

            // your checkbox has been unticked. do stuff...
            // clear the config file
            phoneTF.setDisable(true);
            recipientTF.setDisable(true);
        }

    }
    @FXML
    void addToCart(ActionEvent event) {
        Meal meal = menuTable.getSelectionModel().getSelectedItem();
        cartTable.getItems().add(meal);
        Double currCost = Double.parseDouble(totalCost.getText());
        Double toAdd = meal.getPrice();
        String newCost = Double.toString(currCost + toAdd);
        totalCost.setText(newCost);
    }


    @FXML
    void removeFromCart(ActionEvent event) {
        Meal meal = cartTable.getSelectionModel().getSelectedItem();
        cartTable.getItems().remove(meal);
        Double currCost = Double.parseDouble(totalCost.getText());
        Double toAdd = meal.getPrice();
        String newCost = Double.toString(currCost - toAdd);
        totalCost.setText(newCost);
    }

    @Subscribe
    public void onMenuEvent(MenuEvent event){
        Platform.runLater(()->{
            ObservableList<Meal> mealList = FXCollections.observableArrayList();
            mealList.addAll(event.getMenu().getMeals());
            menuTable.setItems(mealList);
        });
    }

    @FXML
    public void order(ActionEvent actionEvent) {
    }
}
