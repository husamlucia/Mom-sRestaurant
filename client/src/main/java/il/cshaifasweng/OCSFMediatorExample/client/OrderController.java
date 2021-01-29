package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.events.BranchDataControllerLoaded;
import il.cshaifasweng.OCSFMediatorExample.client.events.MenuEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
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
import javafx.scene.image.*;
import javafx.util.Callback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrderController implements Initializable {

    private Branch branch;

    @FXML
    private Button orderBtn;

    String buyMessage;
    double buyPrice;

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
    private TableColumn<Meal, ImageInfo> picCol;

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
        buyPrice = 0;
        totalCost.setText(Double.toString(buyPrice));
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
        buyPrice = 0;
        nameCol.setCellValueFactory(new PropertyValueFactory<Meal, String>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Meal, Double>("price"));
        ingredientsCol.setCellValueFactory(new PropertyValueFactory<Meal, List<String>>("ingredients"));

        cartColName.setCellValueFactory(new PropertyValueFactory<Meal, String>("name"));
        cartColPrice.setCellValueFactory(new PropertyValueFactory<Meal, Double>("price"));

        picCol.setCellValueFactory(new PropertyValueFactory<Meal, ImageInfo>("image"));

        picCol.setCellFactory(param -> new TableCell<Meal, ImageInfo>() {

            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(ImageInfo item, boolean empty) {
                super.updateItem(item, empty);
                imageView.setFitHeight(150);
                imageView.setFitWidth(150);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    imageView.setImage(byteArrayToImage(item));
                    setGraphic(imageView);
                }
                this.setItem(item);
            }
        });

    }




    private ImageInfo imageToByteArray(Image i) {
        PixelReader pr = i.getPixelReader();
        WritablePixelFormat<ByteBuffer> wf = PixelFormat.getByteBgraInstance();

        byte[] buffer = new byte[(int) (i.getWidth() * i.getHeight() * 4)];

        pr.getPixels(0, 0, (int) i.getWidth(), (int) i.getHeight(), wf, buffer, 0, (int) (i.getWidth()) * 4);
        return new ImageInfo(buffer, (int) i.getWidth(), (int) i.getHeight());
    }

    private Image byteArrayToImage(ImageInfo imageArray) {

        WritablePixelFormat<ByteBuffer> wf = PixelFormat.getByteBgraInstance();
        WritableImage writableimage = new WritableImage(imageArray.getWidth(), imageArray.getHeight());
        PixelWriter pixelWriter = writableimage.getPixelWriter();
        pixelWriter.setPixels(0, 0, imageArray.getWidth(), imageArray.getHeight(), wf, imageArray.getImage(), 0, 4 * imageArray.getWidth());
        return writableimage;
    }



    @Subscribe
    public void onBranchDataControllerLoaded(BranchDataControllerLoaded event) {
        Platform.runLater(() -> {
            Branch br = event.getBranch();
            branch = br;
            initializeDateAndHours(branch);
            if(br.getPurpleLetter().isDeliveryAllowed() == false){
                pickupChecked(true);
                pickupCheckBox.setDisable(true);
            }
            else if(!br.getPurpleLetter().isPickupAllowed()){
                pickupCheckBox.setDisable(true);
            }
        });
    }


    public void initializeDateAndHours(Branch branch) {

        String openh = branch.getOpenHours();
        String closeh = branch.getCloseHours();
        PurpleLetter p = branch.getPurpleLetter();
        LocalDate qStart = LocalDate.now().minusDays(1);
        LocalDate qEnd = LocalDate.now().minusDays(1);
        boolean quarantine = p.isQuarantine();
        if(quarantine){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            qStart = LocalDate.parse(p.getQuarantineStartDate(), formatter).minusDays(1);;
            qEnd = LocalDate.parse(p.getQuarantineEndDate(), formatter).plusDays(1);
        }
        LocalDate minDate = LocalDate.now();
        final Callback<DatePicker, DateCell> dayCellFactory;

        LocalDate finalQStart = qStart;
        LocalDate finalQEnd = qEnd;
        dayCellFactory = (final DatePicker datePicker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(minDate)) { //Disable all dates after required date
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); //To set background on different color
                } else if (quarantine) {
                    if (item.isAfter(finalQStart) && item.isBefore(finalQEnd)) {
                        setDisable(true);
                        setStyle("-fx-background-color: #ffc0cb;");                    }
                }
            }
        };

        orderDate.setDayCellFactory(dayCellFactory);


        LocalTime curr = LocalTime.parse(openh).plusMinutes(15);
        LocalTime last = LocalTime.parse(closeh).minusMinutes(59);
        String currString;
        List<String> available = new ArrayList<>();
        while (curr.isBefore(last)) {
            currString = curr.toString();
            curr = curr.plusMinutes(15);
            available.add(currString);
        }
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll(available);
        hourComboBox.setItems(list);

    }



    void pickupChecked(boolean newValue){
        double deliveryCost = 10;
        //disabling or enabling buttons upon changing state of pickup checkbox
        //b == true -> pickup is checked
        if(newValue){
            totalCost.setText(Double.toString(Double.parseDouble(totalCost.getText()) - deliveryCost ));
            orderAddressTF.setDisable(true);
            recipientTF.setDisable(true);
            phoneTF.setDisable(true);
            differentCheckBox.setDisable(true);
            // your checkbox has been ticked.
        }else{

            // your checkbox has been unticked. do stuff...
            // clear the config file
            totalCost.setText(Double.toString(Double.parseDouble(totalCost.getText()) + deliveryCost ));
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
        double newCost = meal.getPrice();
        buyPrice += newCost;
        String newCostStr = Double.toString(buyPrice);
        totalCost.setText(newCostStr);
    }


    @FXML
    void removeFromCart(ActionEvent event) {
        Meal meal = cartTable.getSelectionModel().getSelectedItem();
        cartTable.getItems().remove(meal);
        double newCost = meal.getPrice();
        buyPrice -= newCost;
        if (cartTable.getItems().isEmpty()) buyPrice = 0;
        String newCostStr = Double.toString(buyPrice);
        totalCost.setText(newCostStr);
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
    private ComboBox hourComboBox;
    @FXML
    public void order(ActionEvent actionEvent) {

        List<Meal> meals = new ArrayList<Meal>(cartTable.getItems());
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
        String hour = (String) hourComboBox.getValue();
        String recipientName = "";
        String recipientPhone = "";
        if(different){
            recipientName = recipientTF.getText();
            recipientPhone = phoneTF.getText();
        }

        Order order = new Order(branch, meals, pickup, different, new CustomerDetails(customerName, customerPhone, creditCard), recipientName, recipientPhone,recipientAddress,Double.parseDouble(price), date, hour);
//        String message = "#order " + pickuptxt + ' ' + differenttxt + ' ' + date + ' ' + hour + ' ' + customerName + ' ' + customerPhone + ' ' +
//                creditCard + ' ' + price + ' ' + recipientName + ' ' + recipientPhone + ' ' + recipientAddress + ' ' + mealIds;
//        //#order pickup different date customer_name customer_phone credit_Card price recipientName recipientPhone recipientAddress MealIDs
        try{
            SimpleClient.getClient().sendToServer(order);
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


    @FXML
    private TextField cancelOrderTF;

    @FXML
    void cancelOrder(ActionEvent event) {
        String id = cancelOrderTF.getText();
        String message = "#cancelOrder " + id;
        try{
            SimpleClient.getClient().sendToServer(message);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
