package main;

import com.sun.deploy.util.StringUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Controller implements Initializable {

    @FXML private Label lable1;
    @FXML private Label lable2;
    @FXML private Label lable3;
    @FXML private Label lable4;
    @FXML private Label lable5;

    private int itemsProcessed = 0;
    private float discount = 0.0f;
    private int percent = 0;
    private float total = 0.0f;
    private int itemsInOrder = 0;

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
        File fileToWrite = new File("src/main/transactions.txt");

        map = new HashMap<>();
        try {fileToWrite.createNewFile();}
        catch (IOException e) {e.printStackTrace();}

        try {
            fw = new FileWriter(fileToWrite,true);
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

        initSateStetUp();
    }
    private void initSateStetUp(){
        itemsProcessed = 0;
        discount = 0.0f;
        percent = 0;
        total = 0.0f;
        itemsInOrder = 0;
        if(process_button.isDisable()){process_button.setDisable(false);}

        item_order_textfiled.setDisable(false);
        item_info_textfield.setDisable(true);
        subtotal_textfield.setDisable(true);
        confirm_button.setDisable(true);
        view_order_button.setDisable(true);
        finish_order_button.setDisable(true);
        item_info_textfield.setEditable(false);
        subtotal_textfield.setEditable(false);
    }
    @FXML private void processItem(){
        if(!isItemOrder_Int(item_order_textfiled.getText())){
            basicError("Please enter a number > 0",true);
        }
        else if(lookUpID(book_id_textfield.getText())){
            process_button.setDisable(true);
            confirm_button.setDisable(false);
            item_order_textfiled.setDisable(true);
            String s = map.get(book_id_textfield.getText());
            String [] tokens = s.split(",");
            total += Float.parseFloat(tokens[2]) * Float.parseFloat(quanity_textfield.getText());
            item_info_textfield.setText(tokens[0] + " " +tokens[1] + " $" + tokens[2] +" " + quanity_textfield.getText()+ " %" + (int) (discount * 100) + " $" + total);
        }
        else {basicError("Can't find book Id",false);}
    }

    private void updateItemInfo(){
         if(lookUpID(book_id_textfield.getText())){
            String s = map.get(book_id_textfield.getText());
            String [] tokens = s.split(",");
            total += Float.parseFloat(tokens[2]) * Float.parseFloat(quanity_textfield.getText());
            item_info_textfield.setText(tokens[0] + " " +tokens[1] + " $" + tokens[2] +" " + quanity_textfield.getText()+ " %" + (int) (discount * 100) + " $" + total);
        }
        else {basicError("Can't find book Id",false);}

    }
    private boolean isItemOrder_Int(String text) {
       try {
           int val = Integer.parseInt(text);
           if(val > 0){return true;}
           else {return false;}
       }
       catch (NumberFormatException e){
           e.printStackTrace();
           return false;
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
            System.out.println(item_order_textfiled.getText() + " " + (itemsProcessed +1));
            if(Integer.parseInt(item_order_textfiled.getText()) > (itemsProcessed +1)){
                process_button.setText("Process Item #"+(itemsProcessed+1));
                lable2.setText("Enter Book ID for Item #" + (itemsProcessed + 1));
                lable3.setText("Enter quantity for Item #"+(itemsProcessed+1));
                writeToTransaction();
                itemsProcessed++;
                processItem();
            }else{
                System.out.println("hi");
                updateItemInfo();
                writeToTransaction();
                process_button.setDisable(true);
                confirm_button.setDisable(true);
            }


        }else if(result.get() == buttonTypeNo){}

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
    private void writeToTransaction(){
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyMMddhhmmss");
        try {
            String dateToString = dateFormat.format(date);
            percent = (int) (discount * 100);
            fw.write(dateToString+ ", " + item_info_textfield.getText()+ ", " + quanity_textfield.getText()+", "+percent+"%, "+ String.format("%.2g%n", total));
            fw.flush();
        }
        catch (IOException e) {e.printStackTrace();}
    }
    private void basicError(String text, boolean shouldInitSateStetUp){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(text);
        ButtonType buttonTypeOk = new ButtonType("OK");

        alert.getButtonTypes().setAll(buttonTypeOk);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == buttonTypeOk && shouldInitSateStetUp){
            initSateStetUp();
        }
    }


}
