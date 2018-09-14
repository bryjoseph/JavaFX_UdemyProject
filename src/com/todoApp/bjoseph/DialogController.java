package com.todoApp.bjoseph;

import com.todoApp.bjoseph.dataModel.TodoData;
import com.todoApp.bjoseph.dataModel.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.time.LocalDate;

public class DialogController {

    // match the fx:id's from the controls in the dialog
    @FXML
    private TextField shortDescriptionField;

    @FXML
    private TextArea itemDetailsText;

    @FXML
    private DatePicker itemDeadlineDate;

    // method to process the results from each control
    // instead of being void return the new TodoItem to be selected by the MainController
    public TodoItem processResults() {
        String shortDescription;
        String itemDetails;
        LocalDate itemDeadline;

        // trim to remove any extra white spaces
        if(shortDescriptionField.getText().isEmpty()) {
            shortDescription = "Nothing Entered By User";
        } else {
            shortDescription = shortDescriptionField.getText().trim();
        }

        if(itemDetailsText.getText().isEmpty()) {
            itemDetails = "Nothing Entered By User";
        } else {
            itemDetails = itemDetailsText.getText().trim();
        }

        if(itemDeadlineDate.getValue() == null) {
            itemDeadline = LocalDate.now();
        } else {
            itemDeadline = itemDeadlineDate.getValue();
        }

        // create the new item
        TodoItem newTodoItem = new TodoItem(shortDescription, itemDetails, itemDeadline);

        // add the new item
        TodoData.getInstance().addTodoItem(newTodoItem);
        // return the new item
        return newTodoItem;
    }
}
