package main;

import com.sun.deploy.util.StringUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable {

    @FXML private Label lable1;
    @FXML private Label lable2;
    @FXML private Label lable3;
    @FXML private Label lable4;
    @FXML private Label lable5;
    private int itemsProcessed = 0;
    private FileWriter fw;
    private PrintWriter pw;

    private FileReader fr;
    private BufferedReader br;
    private Scanner sc;
    private HashMap<String, String> map;


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
        File f = new File("src/main/inventory.txt");
        File fileTowrite = new File("src/main/transactions.txt");

        map = new HashMap<>();
        try {fileTowrite.createNewFile();}
        catch (IOException e) {e.printStackTrace();}


        try {
            fw = new FileWriter(fileTowrite);
            pw = new PrintWriter(fw);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}


        try {
            fr = new FileReader(f);
            br = new BufferedReader(fr);
            fillTable();
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}

        System.out.println("hi");
        initSateStetUp();
    }



    private void initSateStetUp(){
        if(process_button.isDisable()){process_button.setDisable(false);}

        item_info_textfield.setDisable(true);
        subtotal_textfield.setDisable(true);
        confirm_button.setDisable(true);
        view_order_button.setDisable(true);
        finish_order_button.setDisable(true);
        item_info_textfield.setEditable(false);
        subtotal_textfield.setEditable(false);
    }


    @FXML private void processItem(){

        if(lookUpID(book_id_textfield.getText())){
            process_button.setDisable(true);
            confirm_button.setDisable(false);
            item_info_textfield.setText(map.get(book_id_textfield.getText()));
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Can't find ID?");
            ButtonType buttonTypeOk = new ButtonType("OK");

            alert.getButtonTypes().setAll(buttonTypeOk);

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == buttonTypeOk){
               initSateStetUp();
            }
        }
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
            processItem();
        }else if(result.get() == buttonTypeNo){
            System.out.println("no");
        }

    }
    @FXML private void newOrder(){
        initSateStetUp();
    }

    @FXML private void quitApplication(){
        try {fr.close(); br.close();}
        catch (IOException e) {e.printStackTrace();}
        Platform.exit();
    }


    private void fillTable() throws IOException {
        String line = null;
        while ((line = br.readLine()) != null){
            String delimiter = ",";
            String[] tokens = line.split(delimiter);
            map.put(tokens[0],line);
        }
    }
    private boolean lookUpID(String id){
        String val = map.get(id);
        if(val == null){return false;}
        else {return true;}
    }
}
