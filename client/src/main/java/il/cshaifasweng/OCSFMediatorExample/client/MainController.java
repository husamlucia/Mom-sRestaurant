package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.Meal;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TableView<Branch> table;

    @FXML
    private TableColumn colBrId;

    @FXML
    private TableColumn colOpenH;

    @FXML
    private TableColumn colCloseH;


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

    @Subscribe
    public void onBranchEvent(BranchEvent event){
        Platform.runLater(()->{
            ObservableList<Branch> branchList = FXCollections.observableArrayList();
            branchList.addAll(event.getBranches().getBranches());
            table.setItems(branchList);
            System.out.println("YO");
        });
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
    void goToLogin(ActionEvent event) throws IOException{
        App.setRoot("login");
    }

    @FXML
    void order(ActionEvent event) {

    }

    @FXML
    void viewMenu(ActionEvent event) {
        Branch branch = table.getSelectionModel().getSelectedItem();
        int branchId = branch.getId();
        String message = "#requestMenu " + branchId;
        try{
            SimpleClient.getClient().sendToServer(message);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Subscribe
    void onMenuEvent(MenuEvent event){

    }
}
