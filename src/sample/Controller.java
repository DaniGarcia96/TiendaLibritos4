package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private Label lable1;
    @FXML private Label lable2;
    @FXML private Label lable3;
    @FXML private Label lable4;
    @FXML private Label lable5;
    private int itemsProcessed = 0;

    @FXML private TextField item_order_textfiled;
    @FXML private TextField book_id_textfield;
    @FXML private TextField quanity_textfield;
    @FXML private TextField item_info_textfield;
    @FXML private TextField subtotal_textfield;
    @FXML private Button process_button;
    @FXML private Button confirm_button;
    @FXML private Button view_order_button;
    @FXML private Button finish_order_button;
    @FXML private Button new_order_button;
    @FXML private Button exit_button;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSateStetUp();
    }



    private void initSateStetUp(){

        if(process_button.isDisable()){
            process_button.setDisable(false);
        }
        item_info_textfield.setDisable(true);
        subtotal_textfield.setDisable(true);
        confirm_button.setDisable(true);
        view_order_button.setDisable(true);
        finish_order_button.setDisable(true);

        item_info_textfield.setEditable(false);
        subtotal_textfield.setEditable(false);
    }


    @FXML private void processItem1(){
        process_button.setDisable(true);
        confirm_button.setDisable(false);

        System.out.println(item_order_textfiled.getText());
        System.out.println(book_id_textfield.getText());
        System.out.println(quanity_textfield.getText());
    }

    @FXML private void confirmItem(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you Sure?");
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeYes,buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == buttonTypeYes){
            System.out.println("yes");
            itemsProcessed++;
            process_button.setText("Process Item #"+(itemsProcessed+1));
            lable2.setText("Enter Book ID for Item #" + (itemsProcessed + 1));
            lable3.setText("Enter quantity for Item #"+(itemsProcessed+1));
            processItem1();
        }else if(result.get() == buttonTypeNo){
            System.out.println("no");
        }

    }
    @FXML private void newOrder(){
        initSateStetUp();
    }

    @FXML private void quitApplication(){
        Platform.exit();
    }
}
