package com.todoApp.bjoseph;

import com.todoApp.bjoseph.dataModel.TodoData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TodoMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("todoMainWindow.fxml"));
        // to set a completely different theme for the FX Application
        // the param of setUserStylesheet can accept a URL to a custom theme
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        primaryStage.setTitle("Todo List Application");
        primaryStage.setScene(new Scene(root, 1000, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // stop is the method called when the user shuts down the application
    @Override
    public void stop() throws Exception {
        try {
            // here in the try block this WRITES whatever changes/new records from the TodoApplication
            TodoData.getInstance().storeTodoItems();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void init() throws Exception {
        try {
            // on init load the records from the .txt file
            TodoData.getInstance().loadTodoItems();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
