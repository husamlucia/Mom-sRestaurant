package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.Meal;
import javafx.application.Platform;
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
    private CheckBox sameCheckBox;

    @FXML
    private TextField phoneTF;


    @FXML
    void goBack(ActionEvent event) throws IOException {
        App.setRoot("customer");
    }

    public void initialize(URL url, ResourceBundle rb) {
        EventBus.getDefault().register(this);
        sameCheckBox.selectedProperty().addListener();
        buyMessage = "order ";
        buyPrice = 0;
        nameCol.setCellValueFactory(new PropertyValueFactory<Meal, String>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Meal, Double>("price"));
        ingredientsCol.setCellValueFactory(new PropertyValueFactory<Meal, List<String>>("ingredients"));

        cartColName.setCellValueFactory(new PropertyValueFactory<Meal, String>("name"));
        cartColPrice.setCellValueFactory(new PropertyValueFactory<Meal, Double>("price"));
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
