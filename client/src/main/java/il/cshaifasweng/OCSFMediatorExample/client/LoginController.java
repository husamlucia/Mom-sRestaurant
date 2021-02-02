package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.events.LoginEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {


    @FXML
    private TextField id;

    @FXML
    private TextField password;


    @FXML
    void initWorkers(ActionEvent event){
        try {
            SimpleClient.getClient().sendToServer("#initRestaurant");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void checkLogin(ActionEvent event) {
        String workerId = id.getText();
        String password = this.password.getText();
        if(workerId.length() != 9 || password.equals("")){
            EventBus.getDefault().post(new Warning("Invalid ID or Password, please try again."));
            return;
        }
        String message = "#login " + workerId + ' ' + password;
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void loadMain(ActionEvent event) throws IOException {
        App.setRoot("customer");
    }


    @Subscribe
    public void onLoginEvent(LoginEvent event) throws IOException {

        Platform.runLater(() -> {
            int privilege = event.getWorker().getPrivilege();
            int branch = event.getWorker().getBranchId();
            String fxml="common";
            String message = "#requestBranch " + branch;
            switch(privilege){
                case 1:
                    fxml = "common";
                    break;
                case 2:
                    fxml = "dietitian";
                    message = "#getAllBranches";
                    break;
                case 3:
                    fxml = "customerService";
                    message = "#getAllBranches";
                    break;
                case 4:
                    fxml = "manager";
                    break;
                case 5:
                    fxml = "manager";
                    message = "#getAllBranches";
                    break;
            }
            try{
                App.setRoot(fxml);
                SimpleClient.getClient().sendToServer(message);
                EventBus.getDefault().unregister(this);
                EventBus.getDefault().post(event);
            }catch(IOException e){
                e.printStackTrace();
            }
        });

    }


    @FXML
    private TextField host;

    @FXML
    private TextField port;

    @FXML
    public void clientConnect(ActionEvent event) throws IOException {
        App.connection(host.getText(), Integer.parseInt(port.getText()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventBus.getDefault().register(this);
    }
}