package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class LoginController{



    @FXML
    private TextField id;

    @FXML
    private PasswordField pass;


    @FXML
    void checkLogin(ActionEvent event) {
        String workerId = id.getText();
        String password = pass.getText();
        String message = "#login " + workerId + ' ' + password;
        try{
            SimpleClient.getClient().sendToServer(message);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void loadMain(ActionEvent event) throws IOException {
        App.setRoot("main");
    }


    @Subscribe
    void onLoginEvent(LoginEvent event) throws IOException {
        int privilege = event.getWorker().getPrivilege();
        BranchController branchController = new BranchController(privilege);
        FXMLLoader loader = new FXMLLoader();
        loader.setController(branchController);
        loader.load();
    }
}
