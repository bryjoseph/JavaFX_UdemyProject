package firstSceneBuilderProject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneBuilderMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("mainScene.fxml"));
        primaryStage.setTitle("First Scene Builder Project");
        primaryStage.setScene(new Scene(root, 1000, 500));

        // method call to show all of the content above
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}