package il.cshaifasweng.OCSFMediatorExample.client;


import il.cshaifasweng.OCSFMediatorExample.client.events.BranchDataControllerLoaded;
import il.cshaifasweng.OCSFMediatorExample.client.events.BranchEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomerDetails;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @FXML
    private TextArea complaintTF;

    @FXML
    private TextField customerPhoneTF;

    @FXML
    private TextField customerNameTF;

    @FXML
    private TextField creditTF;

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
            EventBus.getDefault().post(new BranchDataControllerLoaded((Branch) br));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void goBack(ActionEvent event) throws IOException {
        App.setRoot("login");
    }


    @FXML
    void book(ActionEvent event) throws IOException {
        //Assume we need a table of 1 at least.
        //"#getAvailableHours " + bookingDate + ' ' + bookingTime + ' ' + bookingArea + ' ' + bookingNumOfCustomers;

        Branch br = brTable.getSelectionModel().getSelectedItem();

        if(br==null) return;
        int branchID = br.getId();

        String datetime;
        //dd-MM-yyyy
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime now = LocalDateTime.now();
        datetime = dtf.format(now);

        //"#getAvailableHours " + branchId + bookingDate + ' ' + bookingTime + ' ' + bookingArea + ' ' + bookingNumOfCustomers;
        String message = "#checkBooking " + Integer.toString(branchID) + ' ' + datetime + ' ' + "both" + ' ' +  '1';
        //SimpleClient.getClient().sendToServer(message);
        App.setRoot("booking");
        EventBus.getDefault().post(new BranchDataControllerLoaded((Branch) br));
    }


    @FXML
    void complain(ActionEvent event) {

        String name=customerNameTF.getText();
        String phone=customerPhoneTF.getText();
        String credit=creditTF.getText();
        CustomerDetails customerDetails=new CustomerDetails(name,phone,credit);

        String complaintMsg=complaintTF.getText();
        String datetime;
        //dd-MM-yyyy
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime now = LocalDateTime.now();
        datetime = dtf.format(now);
        Branch br = brTable.getSelectionModel().getSelectedItem();
        //int branchID = br!=null?br.getId():0;
        Complaint complaint=new Complaint(datetime,complaintMsg,customerDetails,br);

        try {
            SimpleClient.getClient().sendToServer(complaint);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Subscribe
    public void onBranchEvent(BranchEvent event){
        Platform.runLater(()->{
            ObservableList<Branch> branchList = FXCollections.observableArrayList();
            branchList.addAll(event.getBranches().getBranches());
            brTable.setItems(branchList);
        });
    }

    @FXML
    void goToWorker(ActionEvent event) {
        try {
            App.setRoot("worker");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void createMaps(ActionEvent event) {
        try {
            String message = "#createmapswithtables";
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
