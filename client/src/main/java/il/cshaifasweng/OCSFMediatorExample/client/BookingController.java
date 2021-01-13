package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Booking;
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
import java.util.ResourceBundle;

public class BookingController implements Initializable {


    private int branchID;
    private String bookingDate;
    private String bookingTime;
    private String bookingArea;
    private int bookingNumOfCustomers;
    private int countDeclare = 0;

    void init(String date, String time, String area, int number) {
        this.bookingDate = date;
        this.bookingTime = time;
        this.bookingArea = area;
        this.bookingNumOfCustomers = number;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventBus.getDefault().register(this);

        colDate.setCellValueFactory(new PropertyValueFactory<Booking, String>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<Booking, String>("hour"));
        colInOut.setCellValueFactory(new PropertyValueFactory<Booking, String>("area"));
    }

//    @Subscribe
//    public void on
    @FXML
    private Button bookBtn;

    @FXML
    private TextField BookingDateTF;

    @FXML
    private TextField BookingTimeTF;

    @FXML
    private TextField BookingNumCustomersTF;

    @FXML
    private TableView<Booking> AvailableTimeTable;

    @FXML
    private TableColumn colDate;

    @FXML
    private TableColumn colTime;

    @FXML
    private TableColumn colInOut;

    @FXML
    private TextField BookingNameTF;

    @FXML
    private TextField BookingIdTF;

    @FXML
    private TextField BookingCancel;

    @FXML
    private TextField BookingNumber; // reservation number (after reservation)

    @FXML
    private Label availabilityLabel;

    @FXML
    void BookingCancel(ActionEvent event) {
        int booking_num = Integer.parseInt(BookingCancel.getText());
        String message = "#removeBook " + " " + booking_num;
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void BookingDeclare(ActionEvent event) {
        countDeclare++;
        if (countDeclare == this.bookingNumOfCustomers) {
            bookBtn.setDisable(false);
        }
    }

    @FXML
    void BookingGetInAvailable(ActionEvent event) {

        checkAvailability("inside");
    }



    @FXML
    void BookingGetOutAvailable(ActionEvent event) {
        checkAvailability("outside");

    }

    @Subscribe
    void onBookingEvent(BookingEvent event){
        //Called when selecting branch to book to: shows options from 1 hour forward of current date and hour.
        //Also called when showing available options for booking time or suggestions.
        //BookingEvent contains List<Booking> that we set to the table.
        Platform.runLater(()->{
            ObservableList<Booking> bookingList = FXCollections.observableArrayList();
            bookingList.addAll(event.getBookings());
            AvailableTimeTable.setItems(bookingList);
            branchID = bookingList.get(0).getBranch().getId();
        });

    }
    void checkAvailability(String location){
        init(BookingDateTF.getText(), BookingTimeTF.getText(), location, Integer.parseInt(BookingNumCustomersTF.getText()));
        //        String msg = "#checkBooking " + " " +  brId + " " + book.getDate() + " " + book.getTime() + " " + book.getArea() + " " + book.getCustomersNum();
        String message = "#checkBooking " + branchID + bookingDate + ' ' + bookingTime + ' ' + bookingArea + ' ' + bookingNumOfCustomers;
        //Checking for availability of Bookings in the bookingDate and bookingTime we present.
        //We also need to send Branch ID of the branch we're accessing.
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void BookingPushSelected(ActionEvent event) {
        // dont forget the id of the booking wesa mnshof kef mn3mlha

        Booking book = (Booking) AvailableTimeTable.getSelectionModel().getSelectedItem();
        int brId = book.getBranch().getId();

        //should be addBooking
        //we presume that we already received list of bookings from server
// String message = "#checkBooking " + Integer.toString(branchID) + ' ' + datetime + ' ' + "both" + ' ' +  '1';
        String msg = "#saveBooking " + " " +  brId + " " + book.getDate() + " " + book.getTime() + " " + book.getArea() + " " + book.getCustomersNum();
        try {
            SimpleClient.getClient().sendToServer(msg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

//    @FXML
//    void goToMain(ActionEvent event) throws IOException {
//        App.setRoot("main");
//    }


