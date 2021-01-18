package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.events.MenuEvent;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
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
    private TextField creditTF;

    @FXML
    private CheckBox differentCheckBox;

    @FXML
    private CheckBox pickupCheckBox;

    @FXML
    private TextField phoneTF;

    @FXML
    private TableColumn<Meal, Image> picCol;

    @FXML
    private TableColumn<?, ?> networkMealCol;

    @FXML
    private TextField customerPhoneTF;

    @FXML
    private TextField customerNameTF;


    @FXML
    void goBack(ActionEvent event) throws IOException {
        App.setRoot("customer");
    }

    public void initialize(URL url, ResourceBundle rb) {
        EventBus.getDefault().register(this);
        pickupChecked(false);
        differentChecked(false);
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

        picCol.setCellFactory(new Callback<TableColumn<Meal, Image>,TableCell<Meal,Image>>(){

            @Override
            public TableCell<Meal,Image> call(TableColumn<Meal,Image> param) {
                final ImageView imageview = new ImageView();
                imageview.setFitHeight(150);
                imageview.setFitWidth(150);
                TableCell<Meal,Image> cell = new TableCell<Meal, Image>(){
                    public void updateItem(Meal item, boolean empty) {
                        if(item!=null){
                            byte[] byteImg = item.getImage().getImage();
                            int height = item.getImage().getHeight();
                            int width = item.getImage().getWidth();
                            imageview.setImage(new Image(new ByteArrayInputStream(byteImg)));
                        }
                    }

                };
                cell.setGraphic(imageview);
                return cell;
            }

        });

    }


    void pickupChecked(boolean newValue){
        int deliveryCost = 10;
        //disabling or enabling buttons upon changing state of pickup checkbox
        //b == true -> pickup is checked
        if(newValue){
            totalCost.setText(Integer.toString(Integer.parseInt(totalCost.getText()) +deliveryCost ));
            orderAddressTF.setDisable(true);
            recipientTF.setDisable(true);
            phoneTF.setDisable(true);
            differentCheckBox.setDisable(true);
            // your checkbox has been ticked.
        }else{

            // your checkbox has been unticked. do stuff...
            // clear the config file
            totalCost.setText(Integer.toString(Integer.parseInt(totalCost.getText()) - deliveryCost ));
            orderAddressTF.setDisable(false);
            recipientTF.setDisable(true);
            phoneTF.setDisable(true);
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
        if (cartTable.getItems().isEmpty()) newCost = "0";
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

        List<Meal> meals = cartTable.getItems();
        String mealIds = "";
        for(Meal meal: meals){
            mealIds += meal.getId() + " ";
        }

        boolean pickup = pickupCheckBox.isSelected();
        String pickuptxt = pickup==true?"1":"0";
        boolean different = differentCheckBox.isSelected();
        String differenttxt = different==true?"1":"0";
        String customerName =  customerNameTF.getText();
        String customerPhone = customerPhoneTF.getText();
        String creditCard = creditTF.getText();
        String recipientAddress = "";
        String price = totalCost.getText();
        if(!pickup){
            recipientAddress = orderAddressTF.getText();
        }
        String date = orderDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String recipientName = "";
        String recipientPhone = "";
        if(different){
            recipientName = recipientTF.getText();
            recipientPhone = phoneTF.getText();
        }
        String message = "#order " + pickuptxt + ' ' + differenttxt + ' ' + date + ' ' + customerName + ' ' + customerPhone + ' ' +
                creditCard + ' ' + price + ' ' + recipientName + ' ' + recipientPhone + ' ' + recipientAddress + ' ' + mealIds;
        //#order pickup different date customer_name customer_phone credit_Card price recipientName recipientPhone recipientAddress MealIDs
        try{
            SimpleClient.getClient().sendToServer(message);
        }catch (IOException e){
            e.printStackTrace();
        }
        //String message = "#order pickup/delivery different date customername phonenumber creditcard price optional:recipientname optional:recipientphone optional:address meals:
        //substring=7
        //if pickup=1 -> offset = 6
        //if pickup=0 ->
        //  if different=0 -> offset = 7
        //  if different -> offset = 9
    }
}
