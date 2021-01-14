package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.events.BookingControllerLoaded;
import il.cshaifasweng.OCSFMediatorExample.entities.BookingEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.Booking;
import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BookingController implements Initializable {

    /* Reminder:
    * Booking is working fine. Got to update so it limits time from 15 minutes of opening hour
    * and 1 hour before closing.*/
    private int branchID;
    private boolean branchHasInside;
    private boolean branchHasOutside;

    private String bookingDate;
    private String bookingTime;
    private String bookingArea;
    private int bookingNumOfCustomers;
    private int countDeclare = 0;

    AnchorPane anchorpane;

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
        colTime.setCellValueFactory(new PropertyValueFactory<Booking, String>("time"));
        colInOut.setCellValueFactory(new PropertyValueFactory<Booking, String>("area"));
        colCustomersNum.setCellValueFactory(new PropertyValueFactory<Booking, String>("customerNum"));
    }

    @Subscribe
    public void onBookingControllerLoaded(BookingControllerLoaded event) {
        Platform.runLater(() -> {
            Branch br = event.getBranch();
            branchID = br.getId();
            if(br.getMap("inside") == null) insideButton.setDisable(true);
            if(br.getMap("outside") == null) outsideButton.setDisable(true);
        });
    }

    @Subscribe
    public void onBookingEvent(BookingEvent event) {
        //Called when selecting branch to book to: shows options from 1 hour forward of current date and hour.
        //Also called when showing available options for booking time or suggestions.
        //BookingEvent contains List<Booking> that we set to the table.
        Platform.runLater(() -> {
            ObservableList<Booking> bookingList = FXCollections.observableArrayList();
            bookingList.addAll(event.getBookings());
            AvailableTimeTable.setItems(bookingList);
        });

    }

    @FXML
    private Button insideButton;

    @FXML
    private Button outsideButton;

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
    private TableColumn colCustomersNum;

    @FXML
    private TextField BookingNameTF;

    @FXML
    private TextField BookingIdTF;

    @FXML
    void BookingCancel(ActionEvent event) {
        String message = "#removeBook " + bookingDate + ' ' + bookingTime + ' ' + bookingArea + ' ' + bookingNumOfCustomers;
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


    void checkAvailability(String location) {

        init(BookingDateTF.getText(), BookingTimeTF.getText(), location, Integer.parseInt(BookingNumCustomersTF.getText()));
        //        String msg = "#checkBooking " + " " +  brId + " " + book.getDate() + " " + book.getTime() + " " + book.getArea() + " " + book.getCustomersNum();
        String message = "#checkBooking " + branchID + ' ' + bookingDate + ' ' + bookingTime + ' ' + bookingArea + ' ' + bookingNumOfCustomers;
        //Checking for availability of Bookings in the bookingDate and bookingTime we present.
        //We also need to send Branch ID of the branch we're accessing.
        try {
                SimpleClient.getClient().sendToServer(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }


        @FXML
        void BookingPushSelected(ActionEvent event){
            // dont forget the id of the booking wesa mnshof kef mn3mlha
            countDeclare = 0;
            bookBtn.setDisable(true);
            Booking book = (Booking) AvailableTimeTable.getSelectionModel().getSelectedItem();
            try {
                SimpleClient.getClient().sendToServer(book);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @FXML
        void goToMain (ActionEvent event) throws IOException {
            App.setRoot("customer");
        }

    }


