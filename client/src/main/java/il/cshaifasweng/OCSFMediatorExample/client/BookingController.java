package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.events.BranchDataControllerLoaded;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BookingController implements Initializable {

    /* Reminder:
     * Booking is working fine. Got to update so it limits time from 15 minutes of opening hour
     * and 1 hour before closing.*/
    private int branchID;
    private Branch branch;


    private String bookingDate;
    private String bookingTime;
    private String bookingArea;
    private int bookingNumOfCustomers;
    private int countDeclare = 0;


    public BookingController() {
    }

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
    public void onBranchDataControllerLoaded(BranchDataControllerLoaded event) {
        Platform.runLater(() -> {
            Branch br = event.getBranch();
            branch = br;
            branchID = br.getId();
            initializeDateAndHours(branch);
            if (br.getMap("inside") == null) insideButton.setDisable(true);
            if (br.getMap("outside") == null) outsideButton.setDisable(true);
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

        datePicker.setDayCellFactory(dayCellFactory);


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
    private ComboBox hourComboBox;

    @FXML
    private DatePicker datePicker;
    @FXML
    private Button insideButton;

    @FXML
    private Button outsideButton;

    @FXML
    private Button bookBtn;


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

        init(datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), (String) hourComboBox.getValue(), location, Integer.parseInt(BookingNumCustomersTF.getText()));
        //        String msg = "#checkBooking " + " " +  brId + " " + book.getDate() + " " + book.getTime() + " " + book.getArea() + " " + book.getCustomersNum();
        if(branchID == 0 || bookingDate.equals("") || bookingTime.equals("") || bookingArea.equals("") || bookingNumOfCustomers < 1){
            EventBus.getDefault().post(new Warning("Please fill the required info correctly."));
            return;
        }
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
    void BookingPushSelected(ActionEvent event) {
        // dont forget the id of the booking wesa mnshof kef mn3mlha
        countDeclare = 0;
        bookBtn.setDisable(true);
        Booking book = (Booking) AvailableTimeTable.getSelectionModel().getSelectedItem();
        if(book == null){
            EventBus.getDefault().post(new Warning("Please select from the table to book."));
            return;
        }
        try {
            SimpleClient.getClient().sendToServer(book);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToMain(ActionEvent event) throws IOException {
        App.setRoot("customer");
    }

    @FXML
    private TextField cancelBookingTF;

    @FXML
    void cancelBooking(ActionEvent event) {
        String id = cancelBookingTF.getText();
        if(id.equals("")){
            EventBus.getDefault().post(new Warning("Enter correct complaint ID to cancel it."));
            return;
        }
        String message = "#cancelBooking " + id;
        try{
            SimpleClient.getClient().sendToServer(message);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}


