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
import javafx.scene.layout.AnchorPane;
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
        App.setRoot("customer");
    }


    @Subscribe
    public void onLoginEvent(LoginEvent event) throws IOException {

        Platform.runLater(() -> {
            int privilege = event.getWorker().getPrivilege();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("branch.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            try {
                stage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                e.printStackTrace();
            }

           // BranchController branchController = loader.<BranchController>getController();
           // branchController.initialize2(privilege);
            Stage prevStage = (Stage) id.getScene().getWindow();
            prevStage.close();
            stage.show();
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventBus.getDefault().register(this);
    }
}