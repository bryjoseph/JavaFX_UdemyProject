package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class Controller {

    // the memberVariable name must be the same as the fx:id in the todo.fxml
    @FXML
    private TextField nameField;
    @FXML
    private Button helloButton;
    @FXML
    private Button byeButton;
    @FXML
    private CheckBox checkBox;
    @FXML
    private Label ourLabel;

    // this method is setting disabled on initialization <--- part of the UI lifecycle
    @FXML
    public void initialize() {
        helloButton.setDisable(true);
        byeButton.setDisable(true);
    }

    // Helpful to other programmers to annotate the eventListener/eventHandler method as well
    @FXML
    public void onButtonClicked(ActionEvent e) {
        // because of the parameter e, can getSource() and .equals() on what type of button was used to also have a different outcome
        if(e.getSource().equals(helloButton)) {
            System.out.println("Hello, " + nameField.getText());
        } else if(e.getSource().equals(byeButton)) {
            System.out.println("Goodbye, " + nameField.getText());
        }

        /*Advanced Multi-Threading Happening here*/
        Runnable task = new Runnable() {
            @Override
            public void run() {
                // this will not run on the UI thread it will run in the background
                try{
                    String s = Platform.isFxApplicationThread() ? "UI Thread" : "Background Thread";
                    System.out.println("I'm going to sleep on the " + s);
                    Thread.sleep(10000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            String s = Platform.isFxApplicationThread() ? "UI Thread" : "Background Thread";
                            System.out.println("I'm updating the label on the " + s);
                            ourLabel.setText("We did something!");
                        }
                    });
                } catch(InterruptedException event) {
                    // dont care here, just simulating putting a thread to sleep
                }
            }
        };
        new Thread(task).start();
        /*Advanced Multi-Threading Happening here*/

        if(checkBox.isSelected()) {
            //.clear just clears the text field
            nameField.clear();
            // after the clear (no values in text field) disable buttons again
            helloButton.setDisable(true);
            byeButton.setDisable(true);
        }
//        System.out.println("Hello, " + nameField.getText());
//        System.out.println("The follow button was pressed: " + e.getSource());
    }

    // this method is checking the text field after initialization
    @FXML
    public void handleKeyReleased() {
        String text = nameField.getText();
        //.trim() is to ignore any white space in the text field
        boolean disableButtons = text.isEmpty() || text.trim().isEmpty();
        // connect to the buttons
        helloButton.setDisable(disableButtons);
        byeButton.setDisable(disableButtons);
    }

    // eventHandler for checkBox
    @FXML
    public void handleChange() {
        System.out.println("The check box is " + (checkBox.isSelected() ? "checked" : "unchecked"));
//        if(checkBox.isSelected()) {
//            //.clear just clears the text field
//            nameField.clear();
//            // after the clear (no values in text field) disable buttons again
//            helloButton.setDisable(true);
//            byeButton.setDisable(true);
//        }
    }
}
