package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.events.BranchEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.client.events.MenuEvent;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class WorkerController implements Initializable {

    private Meal mealToUpdate;
    private Branch branch;
    private int privilege;

    @FXML
    private TableView<Branch> branchTable;

    @FXML
    private TableColumn<Branch, Integer> brIdCol;

    @FXML
    private TableColumn<Branch, String> brOpenCol;

    @FXML
    private TableColumn<Branch, String> brCloseCol;

    @FXML
    private TableView<Meal> menuTable;


    @FXML
    private TableColumn<Meal, Integer> mealIdCol;

    @FXML
    private TableColumn<Meal, String> mealNameCol;

    @FXML
    private TableColumn<Meal, List<String>> mealIngCol;

    @FXML
    private TableColumn<Meal, Double> mealPriceCol;

    @FXML
    private TableColumn<Meal, ImageInfo> mealImageCol;



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
    private TableColumn<MealUpdate, Meal> mealUpdatesOldImage;

    @FXML
    private TableColumn<MealUpdate, Meal> mealUpdatesNewImage;

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

            try {
                ObservableList<MealUpdate> updatesList = FXCollections.observableArrayList();
                List<MealUpdate> meals = event.getMealUpdates();
                updatesList.addAll(meals);
                mealUpdatesTable.setItems(updatesList);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }


    @Subscribe
    public void onBranchEvent(BranchEvent event) {
        Platform.runLater(() -> {
            ObservableList<Branch> branchList = FXCollections.observableArrayList();
            branchList.addAll(event.getBranches().getBranches());
            branchTable.setItems(branchList);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventBus.getDefault().register(this);

        //each cellValueFactory has been set according to the member variables of your entity class
        brIdCol.setCellValueFactory(new PropertyValueFactory<Branch, Integer>("id"));
        brOpenCol.setCellValueFactory(new PropertyValueFactory<Branch, String>("openHours"));
        brCloseCol.setCellValueFactory(new PropertyValueFactory<Branch, String>("closeHours"));


        mealIdCol.setCellValueFactory(new PropertyValueFactory<Meal, Integer>("id"));
        mealNameCol.setCellValueFactory(new PropertyValueFactory<Meal, String>("name"));
        mealPriceCol.setCellValueFactory(new PropertyValueFactory<Meal, Double>("price"));
        mealIngCol.setCellValueFactory(new PropertyValueFactory<Meal, List<String>>("ingredients"));




        mealUpdatesID.setCellValueFactory(cellData -> new SimpleStringProperty(
                Integer.toString(cellData.getValue().getOldMeal() != null ? cellData.getValue().getOldMeal().getId() : 0)));

        mealUpdatesOldName.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getOldMeal() != null ? cellData.getValue().getOldMeal().getName() : ""));

        mealUpdatesNewName.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getNewMeal() != null ? cellData.getValue().getNewMeal().getName() : ""));

        mealUpdatesOldPrice.setCellValueFactory(cellData -> new SimpleStringProperty(
                Double.toString(cellData.getValue().getOldMeal() != null ? cellData.getValue().getOldMeal().getPrice() : 0)));
        mealUpdatesNewPrice.setCellValueFactory(cellData -> new SimpleStringProperty(
                Double.toString(cellData.getValue().getNewMeal() != null ? cellData.getValue().getNewMeal().getPrice() : 0)));
        mealUpdatesOldBranch.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(
                cellData.getValue().getBranch() != null ? cellData.getValue().getBranch().getId() : 0)));
        mealUpdatesNewBranch.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getNewBranchId())));


        mealImageCol.setCellValueFactory(new PropertyValueFactory<Meal, ImageInfo>("image"));

        mealImageCol.setCellFactory(param -> new TableCell<Meal, ImageInfo>() {

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

        mealUpdatesOldImage.setCellValueFactory(new PropertyValueFactory<MealUpdate, Meal>("oldMeal"));
        mealUpdatesOldImage.setCellFactory(param -> new TableCell<MealUpdate, Meal>() {

            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Meal item, boolean empty) {
                super.updateItem(item, empty);
                imageView.setFitHeight(150);
                imageView.setFitWidth(150);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    imageView.setImage(byteArrayToImage(item.getImage()));
                    setGraphic(imageView);
                }
                this.setItem(item);
            }
        });

        mealUpdatesNewImage.setCellValueFactory(new PropertyValueFactory<MealUpdate, Meal>("newMeal"));
        mealUpdatesNewImage.setCellFactory(param -> new TableCell<MealUpdate, Meal>() {

            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Meal item, boolean empty) {
                super.updateItem(item, empty);
                imageView.setFitHeight(150);
                imageView.setFitWidth(150);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    imageView.setImage(byteArrayToImage(item.getImage()));
                    setGraphic(imageView);
                }
                this.setItem(item);
            }
        });

        try {
            SimpleClient.getClient().sendToServer("#getAllBranches");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    public void initialize2(int privilege) {
        this.privilege = privilege;
        switch (privilege) {
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
    private Button chooseImageButton;
    @FXML
    private ImageView mealImageView;

    @FXML
    void chooseImage(ActionEvent event) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pick image for meal");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*jpg"));
        File file = fileChooser.showOpenDialog(chooseImageButton.getScene().getWindow());
        Image image = new Image(new FileInputStream(file));
        mealImageView.setImage(image);
    }

    public void addOrSaveMealClicked(boolean clicked) {
        nameTF.setDisable(!clicked);
        ingredientsTF.setDisable(!clicked);
        priceTF.setDisable(!clicked);
        branchIdTF.setDisable(!clicked);
        mealImageView.setImage(null);
        mealToUpdate = null;
    }

    public void resetMealDetailsFields() {
        nameTF.setText("");
        ingredientsTF.setText("");
        priceTF.setText("");
        branchIdTF.setText("");

    }

    @FXML
    void addMeal(ActionEvent event) {
        addOrSaveMealClicked(true);
        resetMealDetailsFields();
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
        mealImageView.setImage(byteArrayToImage(meal.getImage()));
        mealToUpdate = meal;
    }

    @FXML
    void removeMeal(ActionEvent event) {
        Meal meal = (Meal) menuTable.getSelectionModel().getSelectedItem();
        mealToUpdate = meal;
        int brId = branch.getId();
        createAndSendMealUpdateToServer(meal, null, branch, brId);
        MealUpdate mealUpdate = new MealUpdate(meal, null, branch, brId);

    }


    private ImageInfo imageToByteArray(Image i){
        PixelReader pr = i.getPixelReader();
        WritablePixelFormat<ByteBuffer> wf = PixelFormat.getByteBgraInstance();

        byte[] buffer = new byte[(int) (i.getWidth() * i.getHeight() *4)];

        pr.getPixels(0, 0, (int) i.getWidth(), (int) i.getHeight(), wf, buffer, 0, (int) (i.getWidth())*4);
        return new ImageInfo(buffer, (int) i.getWidth(), (int) i.getHeight()) ;
    }

    private Image byteArrayToImage(ImageInfo imageArray){

        WritablePixelFormat<ByteBuffer> wf = PixelFormat.getByteBgraInstance();
        WritableImage writableimage = new WritableImage(imageArray.getWidth(), imageArray.getHeight());
        PixelWriter pixelWriter = writableimage.getPixelWriter();
        pixelWriter.setPixels(0, 0, imageArray.getWidth(), imageArray.getHeight(), wf, imageArray.getImage(), 0, 4*imageArray.getWidth());
        return writableimage;
    }

    @FXML
    void saveMeal(ActionEvent event) throws FileNotFoundException {
        String name, ingTxt, brId;
        Double price;

        name = nameTF.getText();
        ingTxt = ingredientsTF.getText();
        brId = branchIdTF.getText();
        price = Double.parseDouble(priceTF.getText());

        String[] ing = ingTxt.split("\\s+");
        List<String> ingredients = Arrays.asList(ing);

        ImageInfo image = imageToByteArray(mealImageView.getImage());
        mealImageView.setImage(null);
        mealImageView.setImage(byteArrayToImage(image));

        Meal oldMeal = mealToUpdate;
        Meal newMeal = new Meal(name, price, ingredients, image);
        createAndSendMealUpdateToServer(oldMeal, newMeal, branch, Integer.parseInt(brId));
    }

    public void createAndSendMealUpdateToServer(Meal oldMeal, Meal newMeal, Branch oldBranch, int newBranchId) {
        MealUpdate mealUpdate = new MealUpdate(oldMeal, newMeal, branch, newBranchId);
        try {
            SimpleClient.getClient().sendToServer(mealUpdate);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
            String message = "#requestComplaints " + branchTable.getSelectionModel().getSelectedItem().getId();
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    void requestMenu(ActionEvent event) {
        this.branch = branchTable.getSelectionModel().getSelectedItem();

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
            String message = "#requestMap " + branchTable.getSelectionModel().getSelectedItem().getId();
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
        this.branch = branchTable.getSelectionModel().getSelectedItem();
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
