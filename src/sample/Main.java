package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // if wanting to execute the code below, comment out FXMLLoader.load()
        Parent root = FXMLLoader.load(getClass().getResource("todo.fxml"));

        // the following code is replicating the fxml code
//        GridPane root = new GridPane();
//        root.setAlignment(Pos.CENTER);
//        root.setHgap(10);
//        root.setVgap(10);
        // now window appears the same as before

        primaryStage.setTitle("Broj World Title");
        primaryStage.setScene(new Scene(root, 1000, 500));

        // add text within the scene (not Title)
//      Label greeting = new Label("Welcome to JavaFX");
//      greeting.setTextFill(Color.GREEN);
//      // font names are case sensitive
//      greeting.setFont(Font.font("Times New Roman", FontWeight.BOLD, 70));
        // add the greeting to the child of GridPane
//      root.getChildren().add(greeting);

        // method call to show all of the content above
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
