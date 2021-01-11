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
import javafx.scene.control.Label;
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


    @FXML
    private TableView<Branch> brTable;

    @FXML
    private TableColumn colBrId;

    @FXML
    private TableColumn colOpenH;

    @FXML
    private TableColumn colCloseH;



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


    public void initialize(URL url, ResourceBundle rb) {
        EventBus.getDefault().register(this);
        //each cellValueFactory has been set according to the member variables of your entity class
        colBrId.setCellValueFactory(new PropertyValueFactory<Branch, Integer>("id"));
        colOpenH.setCellValueFactory(new PropertyValueFactory<Branch, String>("openHours"));
        colCloseH.setCellValueFactory(new PropertyValueFactory<Branch, String>("closeHours"));

        try{
            SimpleClient.getClient().sendToServer("#getAllBranches");
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    @FXML
    void showMenu(ActionEvent event) {
        try {
            Branch br = brTable.getSelectionModel().getSelectedItem();
            int branchID = br!=null?br.getId():0;
            String message = "#requestMenu " + Integer.toString(branchID);
            SimpleClient.getClient().sendToServer(message);
            App.setRoot("order");
        } catch (IOException e) {
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
    void complain(ActionEvent event) {

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
