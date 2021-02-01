package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.events.LoginEvent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
            SimpleClient.getClient().sendToServer("#addDefaultWorkers");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void checkLogin(ActionEvent event) {
        String workerId = id.getText();
        String password = this.password.getText();
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
            }catch(IOException e){
                e.printStackTrace();
            }
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventBus.getDefault().register(this);
    }
}