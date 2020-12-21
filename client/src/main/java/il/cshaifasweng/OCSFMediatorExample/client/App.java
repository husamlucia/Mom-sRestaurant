package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Meal;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private SimpleClient client;

    @Override
    public void start(Stage stage) throws IOException {
    	EventBus.getDefault().register(this);
    	client = SimpleClient.getClient();
    	client.openConnection();
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    

    @Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
    	EventBus.getDefault().unregister(this);
		super.stop();
	}
    
    @Subscribe
    public void onWarningEvent(WarningEvent event) {
    	Platform.runLater(() -> {
    		Alert alert = new Alert(AlertType.WARNING,
        			String.format("Message: %s\nTimestamp: %s\n",
        					event.getWarning().getMessage(),
        					event.getWarning().getTime().toString())
        	);
        	alert.show();
    	});
    	
    }
    
    @Subscribe
    public void onMenuEvent(MenuEvent event){
        Platform.runLater(()->{
            //It has MenuEvent which has "Menu" as attribute. Now gotta transform it to "TableView"..
            ObservableList<Meal> MealList = FXCollections.observableArrayList();
            MealList.addAll(event.getMenu().getMeals());
            TableView tbl = (TableView) scene.lookup("#menuTable");
            tbl.setItems(MealList);
        });
    }

    @Subscribe
    public void onAllMealsEvent(AllMealsEvent event){
        Platform.runLater(()->{
          ObservableList<Meal> MealList = FXCollections.observableArrayList();
          MealList.addAll(event.getMenu().getMeals());
          TableView tbl = (TableView) scene.lookup("#menuTable1");
          tbl.setItems(MealList);
          System.out.println(MealList.size());
        });
    }

	public static void main(String[] args) {
        launch();
    }

}