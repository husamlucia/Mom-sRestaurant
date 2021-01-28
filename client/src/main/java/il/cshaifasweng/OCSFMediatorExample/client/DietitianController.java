package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class DietitianController {


        @FXML
        private TableView<?> branchTable;

        @FXML
        private TableColumn<?, ?> brIdCol;

        @FXML
        private TableColumn<?, ?> brOpenCol;

        @FXML
        private TableColumn<?, ?> brCloseCol;

        @FXML
        private Button showMenuBtn;

        @FXML
        private TableView<?> menuTable;

        @FXML
        private TableColumn<?, ?> mealIdCol;

        @FXML
        private TableColumn<?, ?> mealImageCol;

        @FXML
        private TableColumn<?, ?> mealNameCol;

        @FXML
        private TableColumn<?, ?> mealIngCol;

        @FXML
        private TableColumn<?, ?> networkMealMenuCol;

        @FXML
        private TableColumn<?, ?> mealPriceCol;

        @FXML
        private Button saveBtn;

        @FXML
        private TextField nameTF;

        @FXML
        private TextField ingredientsTF;

        @FXML
        private TextField priceTF;

        @FXML
        private TextField branchIdTF;

        @FXML
        private Button editMealBtn;

        @FXML
        private Button addMealBtn;

        @FXML
        private Button removeMealBtn;

        @FXML
        private Button chooseImageButton;

        @FXML
        private Button mapInsideBtn;

        @FXML
        private ComboBox<?> hourComboBox;

        @FXML
        private DatePicker datePicker;

        @FXML
        private Button mapOutsideBtn;

        @FXML
        private TableView<?> retaurantMapTable;

        @FXML
        private TableColumn<?, ?> tableNumberMapCol;

        @FXML
        private TableColumn<?, ?> numOfSeatsMapCol;

        @FXML
        private TableColumn<?, ?> reservationsMapCol;

        @FXML
        void addMeal(ActionEvent event) {

        }

        @FXML
        void chooseImage(ActionEvent event) {

        }

        @FXML
        void editMeal(ActionEvent event) {

        }

        @FXML
        void removeMeal(ActionEvent event) {

        }

        @FXML
        void requestMenu(ActionEvent event) {

        }

        @FXML
        void requestRestaurantMap(ActionEvent event) {

        }

        @FXML
        void saveMeal(ActionEvent event) {

        }

    }


