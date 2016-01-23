/* Name: James Vinson
 Course: CNT 4714 – Spring 2016
 Assignment title: Program 1 – Event-driven Programming
 Date: Sunday January 24, 2016
*/
package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application  {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("bookStoreGUI.fxml"));
        primaryStage.setTitle("Ye Olde Book Shoppe");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
