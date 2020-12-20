package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PrimaryController implements Initializable {
	@FXML
	private TableView<?> menuTable;

	@FXML
	private TableColumn<?, ?> menuColID;

	@FXML
	private TableColumn<?, ?> menuColName;

	@FXML
	private TableColumn<?, ?> menuColPrice;

	@FXML
	private TableColumn<?, ?> menuColIng;


	public void initialize(URL url, ResourceBundle rb) {
		//each cellValueFactory has been set according to the member variables of your entity class
		menuColID.setCellValueFactory(new PropertyValueFactory<Meal, Integer>("id"));
		menuColName.setCellValueFactory(new PropertyValueFactory<Meal, String>("name"));
		menuColPrice.setCellValueFactory(new PropertyValueFactory<Meal, String>("price"));
		menuColIng.setCellValueFactory(new PropertyValueFactory<Meal, String>("ingredients"));
	}

	@FXML
	void requestMenu(ActionEvent event) {
		try {
			SimpleClient.getClient().sendToServer("#requestMenu");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    @FXML
    void sendWarning(ActionEvent event) {
    	try {
			SimpleClient.getClient().sendToServer("#warning");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
