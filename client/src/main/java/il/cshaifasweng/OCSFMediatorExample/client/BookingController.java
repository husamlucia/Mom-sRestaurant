package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Meal;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class BookingController {
    private String bookingDate ;
    private String bookingTime ;
    private String bookingArea ;
    private int bookingNumOfCustomers ;
    private int countDeclare = 0;


    void init(String date,String time,String area, int number){
        this.bookingDate = date;
        this.bookingTime = time;
        this.bookingArea = area;
        this.bookingNumOfCustomers = number;
    }

    @FXML
    private Button bookBtn;

    @FXML
    private TextField BookingDateTF;

    @FXML
    private TextField BookingTimeTF;

    @FXML
    private TextField BookingNumCustomersTF;

    @FXML
    private TableView<?> AvailableTimeTable;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colTime;

    @FXML
    private TableColumn<?, ?> colInOut;

    @FXML
    private TextField BookingNameTF;

    @FXML
    private TextField BookingIdTF;

    @FXML
    void BookingCancel(ActionEvent event) {
        String message = "#removeBook " + bookingDate + ' ' + bookingTime + ' ' + bookingArea + ' ' + bookingNumOfCustomers;
        try{
            SimpleClient.getClient().sendToServer(message);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void BookingDeclare(ActionEvent event) {
        countDeclare++;
        if(countDeclare == this.bookingNumOfCustomers){
            bookBtn.setDisable(false);
        }
    }

    @FXML
    void BookingGetInAvailable(ActionEvent event) {
        init(BookingDateTF.getText(),BookingTimeTF.getText(),"inside",Integer.parseInt(BookingNumCustomersTF.getText()));
        String message = "#getAvailableHours " + bookingDate + ' ' + bookingTime + ' ' + bookingArea + ' ' + bookingNumOfCustomers;
        try{
            SimpleClient.getClient().sendToServer(message);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void BookingGetOutAvailable(ActionEvent event) {
        init(BookingDateTF.getText(),BookingTimeTF.getText(),"outside",Integer.parseInt(BookingNumCustomersTF.getText()));

        String message = "#getAvailableHours " + bookingDate + ' ' + bookingTime + ' ' + bookingArea + ' ' + bookingNumOfCustomers;
        try{
            SimpleClient.getClient().sendToServer(message);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void BookingPushSelected(ActionEvent event) {
        Book book =  AvailableTimeTable.getSelectionModel().getSelectedItem();
        String  msg = "#removeBook " + " " + book.getDate + " " + book.getTime + " " + book.getArea + " " + book.getCustomersNum;
        try {
            SimpleClient.getClient().sendToServer(msg);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    void goToMain(ActionEvent event) throws IOException {
        App.setRoot("main");
    }

}
