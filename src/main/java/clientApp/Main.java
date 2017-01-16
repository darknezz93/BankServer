package clientApp;/**
 * Created by adam on 15.01.17.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL url = new File("src/main/java/clientApp/sample.fxml").toURL();
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("Klient konta bankowego");
        primaryStage.setScene(new Scene(root, 690, 519));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}