package todoApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TodoMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("todo.fxml"));
        primaryStage.setTitle("Broj World Title");
        primaryStage.setScene(new Scene(root, 1000, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
