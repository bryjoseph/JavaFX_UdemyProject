package com.contactApp.bjoseph;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ContactMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("contactApp.fxml"));
        // to set a completely different theme for the FX Application
        // the param of setUserStylesheet can accept a URL to a custom theme
        // setUserAgentStylesheet(STYLESHEET_CASPIAN);
        primaryStage.setTitle("My Contacts");
        primaryStage.setScene(new Scene(root, 1000, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
