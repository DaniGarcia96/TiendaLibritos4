/* Name: James Vinson
 Course: CNT 4714 – Spring 2016
 Assignment title: Program 1 – Event-driven Programming
 Date: Sunday January 24, 2016
*/

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

    private int itemsProcessed;
    private float discount;
    private int percent;
    private float subTotal;
    private float total;
    private int itemsInOrder;
    private ArrayList<String> viewOrder;
    private float taxRate = 0.06f;
    private Date date;

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
        date = new Date();
        viewOrder = new ArrayList<>();
        itemsProcessed = 1;
        discount = 0.0f;
        percent = 0;
        subTotal = 0.0f;
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

        process_button.setText("Process Item #"+(itemsProcessed));
        confirm_button.setText("Confirm Item #"+(itemsProcessed));
        lable2.setText("Enter Book ID for Item #" + (itemsProcessed));
        lable3.setText("Enter quantity for Item #"+(itemsProcessed));

        book_id_textfield.setEditable(true);
        quanity_textfield.setEditable(true);
    }
    @FXML private void processItem(){
        if(!isItemOrder_Int(item_order_textfiled.getText())){
            basicError("Please enter a number > 0 order!",true);
        }
        else if(!isQuanity_Int(quanity_textfield.getText())){
            basicError("Please enter a number > 0 for quanity!",false);
        }
        else if(lookUpID(book_id_textfield.getText())){
            process_button.setDisable(true);
            confirm_button.setDisable(false);
            item_order_textfiled.setDisable(true);
            checkDiscount(quanity_textfield.getText());
            String s = map.get(book_id_textfield.getText());
            String [] tokens = s.split(",");
            total = Float.parseFloat(tokens[2]) * Float.parseFloat(quanity_textfield.getText());
            total = total -(total*discount);
            subTotal += total;
            item_info_textfield.setText(tokens[0] + " " +tokens[1] + " $" + tokens[2] +" " + quanity_textfield.getText()+ " " + (int) (discount * 100) + "% $" + String.format("%.2f", total));
            lable4.setText("Item #"+(itemsProcessed)+ "info: #");
            lable5.setText("Order subtotal for " + itemsProcessed + " item(s)");
            subtotal_textfield.setText("$" +String.format("%.2f", subTotal));
            book_id_textfield.setEditable(false);
            quanity_textfield.setEditable(false);
            finish_order_button.setDisable(true);
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

    private boolean isQuanity_Int(String text){
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
            if(Integer.parseInt(item_order_textfiled.getText()) > (itemsProcessed)){
                process_button.setText("Process Item #"+(itemsProcessed+1));
                confirm_button.setText("Confirm Item #"+(itemsProcessed+1));
                lable2.setText("Enter Book ID for Item #" + (itemsProcessed + 1));
                lable3.setText("Enter quantity for Item #"+(itemsProcessed+1));
                writeToTransaction();
                itemsProcessed++;
                viewOrder.add(item_info_textfield.getText());
                process_button.setDisable(false);
                confirm_button.setDisable(true);
                book_id_textfield.setEditable(true);
                quanity_textfield.setEditable(true);
                if(finish_order_button.isDisable()){
                    finish_order_button.setDisable(false);
                }
            }else{
                viewOrder.add(item_info_textfield.getText());
                writeToTransaction();
                process_button.setDisable(true);
                confirm_button.setDisable(true);
                lable2.setText("");
                lable3.setText("");
                if(finish_order_button.isDisable()){
                    finish_order_button.setDisable(false);
                }
            }
        }else if(result.get() == buttonTypeNo){
            subTotal -= total;
            confirm_button.setDisable(true);
            process_button.setDisable(false);
            book_id_textfield.setEditable(true);
            quanity_textfield.setEditable(true);
            if(finish_order_button.isDisable()){
                finish_order_button.setDisable(false);
            }
        }
        if(view_order_button.isDisable()){view_order_button.setDisable(false);}
}

    @FXML private void viewOrder(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Your Order");

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        int i =1;
        for(String s: viewOrder){
            textArea.appendText(i +". "+s + "\n");
            i++;
        }

        alert.getDialogPane().setContent(textArea);

        ButtonType buttonTypeOk = new ButtonType("OK");

        alert.getButtonTypes().setAll(buttonTypeOk);

        Optional<ButtonType> result = alert.showAndWait();

    }

    @FXML private void finishOrder(){
        process_button.setDisable(true);
        confirm_button.setDisable(true);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Date date = new Date();
        DateFormat dateFormat2 = new SimpleDateFormat("MM/dd/yy k:m:s a z");
        alert.setHeaderText(dateFormat2.format(date));


        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.appendText("Number of line items: "+item_order_textfiled.getText()+"\n\n");
        textArea.appendText("Items#/ID/Title/Qty/Disc%/Subtotal:"+"\n\n");

        int i =1;
        for(String s: viewOrder){
            textArea.appendText(i +". "+s + "\n\n");
            i++;
        }

        textArea.appendText("$" +String.format("%.2f", subTotal)+ "\n\n");
        textArea.appendText("Tax rate:  6% \n\n");
        float taxAmount = (subTotal*taxRate);
        textArea.appendText("Tax amount: "+String.format("%.2f",taxAmount) +"\n\n");
        textArea.appendText("Order total: "+String.format("%.2f",(subTotal+taxAmount))+"\n\n");
        textArea.appendText("Thank you for shopping with us!");

        alert.getDialogPane().setContent(textArea);

        ButtonType buttonTypeOk = new ButtonType("OK");

        alert.getButtonTypes().setAll(buttonTypeOk);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == buttonTypeOk){
            finish_order_button.setDisable(true);
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
    private void writeToTransaction(){
        DateFormat dateFormat1 = new SimpleDateFormat("yyMMddhhmmss");
        DateFormat dateFormat2 = new SimpleDateFormat("MM/dd/yy k:m:s a z");
        try {
            String dateToString = dateFormat1.format(date);
            String dateToString2 = dateFormat2.format(date);
            String s = map.get(book_id_textfield.getText());
            String [] tokens = s.split(",");
            fw.write(dateToString+ ", " + tokens[0]+ ", " +tokens[1]+ ", " +tokens[2]+ ", " + quanity_textfield.getText()+", "+ String.format(".2f",discount)+", "+ String.format("%.2f", total)+", "+ dateToString2+"\n");
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


    private void checkDiscount(String s){
        try {
            int val = Integer.parseInt(s);
            if(val < 4){discount = 0.0f;}
            else if(val < 9){discount = 0.1f;}
            else if(val < 15){discount = 0.15f;}
            else {discount = 0.2f;}
        }
        catch (NumberFormatException e){
            e.printStackTrace();
        }
    }
}
