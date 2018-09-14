package com.todoApp.bjoseph;

import com.todoApp.bjoseph.dataModel.TodoData;
import com.todoApp.bjoseph.dataModel.TodoItem;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller {

    private List<TodoItem> todoItems;
    // because the filtered list will be accessed through an event handler, the list needs to be an instance variable
    private FilteredList<TodoItem> filteredList;
    // to reduce the number of redundant Predicates
    private Predicate<TodoItem> wantAllTodoItems;
    private Predicate<TodoItem> wantTodaysTodoItems;

    @FXML
    private ListView<TodoItem> todoListView;
    @FXML
    private TextArea todoItemDetails;
    @FXML
    private Label deadlineLabel;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ContextMenu todoListContextMenu;
    @FXML
    private ToggleButton filterToggleButton;
/********** this section of code is for testing only *********/
    @FXML
    private Label testLabel;
    @FXML
    private Button testButton;
    @FXML
    private WebView webView;

    public void initialize() {
/********** this code is only test code **********/
        testButton.setEffect(new DropShadow());
/********** this code is only test code **********/

//        TodoItem item1 = new TodoItem("Start Java Lessons",
//                "Sit down at personal computer and watch/code Udemy Java lessons",
//                LocalDate.of(2018, Month.SEPTEMBER,5));
//        TodoItem item2 = new TodoItem("Apply To Back-End Jobs",
//                "Look into Denver Tech Center software development jobs",
//                LocalDate.of(2018,Month.OCTOBER,1));
//        TodoItem item3 = new TodoItem("Conduct Phone Interview",
//                "After finding a place, practice and conduct a phone interview for a software engineer position",
//                LocalDate.of(2018,Month.NOVEMBER,1));
//        TodoItem item4 = new TodoItem("Interview In Person",
//                "Once the phone interview is aced, meet the office in person",
//                LocalDate.of(2018,Month.NOVEMBER,5));
//        TodoItem item5 = new TodoItem("Review Offer Letter",
//                "After acing the phone interview and the in-person interview, review offer letter and make decision",
//                LocalDate.of(2018,Month.NOVEMBER,10));

//        todoItems =  new ArrayList<>();
//        todoItems.add(item1);
//        todoItems.add(item2);
//        todoItems.add(item3);
//        todoItems.add(item4);
//        todoItems.add(item5);

        // temp line here that READS from the .txt file what the last saved set of TodoItems were and sets them in the List
//        TodoData.getInstance().setTodoItems(todoItems);

        // programmatically selecting the first item
        todoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoItem>() {
            @Override
            public void changed(ObservableValue<? extends TodoItem> observable, TodoItem oldValue, TodoItem newValue) {
                if(newValue != null) {
                    TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                    todoItemDetails.setText(item.getDetails());
                    // DateTime Formatter
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                    // with the formatter remove .toString
                    // (old code) deadlineLabel.setText(item.getDeadline().toString());
                    // needs a Date object not a string to work
                    deadlineLabel.setText(df.format(item.getDeadline()));
                }
            }
        });
        // initialize the predicates from private member instances
        wantAllTodoItems = new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem todoItem) {
                return true;
            }
        };

        wantTodaysTodoItems =  new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem todoItem) {
                return (todoItem.getDeadline().equals(LocalDate.now()));
            }
        };


        // creating a filtered list above the sorted list. Filter the list first, then allow the SortedList handle the remaining items
        // UPDATE because the predicate is initialized above, can remove the @Override method here
        filteredList =  new FilteredList<TodoItem>(TodoData.getInstance().getTodoItems(), wantAllTodoItems);

        // creating a sorted list to wrap the TodoItems that make up the todoListView
        // the SortedList requires two parameters, the items being compared and a Comparator<TodoItems>
        // UPDATE changed the first param to filteredList instead of ALL of the TodoData items
        SortedList<TodoItem> sortedList = new SortedList<>(filteredList, new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem o1, TodoItem o2) {
                // return 0 if the two items are considered to be equal
                // returns -1 if the o1 is less than than o2
                // returns +1 if o1 is greater than o2
                return o1.getDeadline().compareTo(o2.getDeadline());
            }
        });

        // UPDATE this to use data binding
        // todoListView.getItems().setAll(TodoData.getInstance().getTodoItems());
        // UPDATE instead of just using data binding to populate the list, we can now use the sortedList
        // todoListView.setItems(TodoData.getInstance().getTodoItems());
        todoListView.setItems(sortedList);
        // this has something to do with only being able to click on one item in the todoList at a time
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        // to default select the first record in the list
        todoListView.getSelectionModel().selectFirst();

        // using a cell factory callback in the initialize method to determine background color of due date
        todoListView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> param) {
                ListCell<TodoItem> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(TodoItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty) {
                            setText(null);
                        } else {
                            setText(item.getShortDescription());
                            // anything earlier than tomorrow?
                            if(item.getDeadline().isBefore(LocalDate.now().plusDays(1))) {
                                setTextFill(Color.RED);
                                // anything one day out from now
                            } else if(item.getDeadline().equals(LocalDate.now().plusDays(1))) {
                                setTextFill(Color.PURPLE);
                            }
                        }
                    }
                };
                // need to associate the context menu with the todoListView (this view is the list of all Todos)
                // this needs to be tied to a cell BECAUSE the user will open the context menu by right clicking
                // after the cell is created (cannot right click an empty cell) call the contextMenu into action
                cell.emptyProperty().addListener( (obs, wasEmpty, isNowEmpty) -> {
                            if(isNowEmpty) {
                                // if the cell is now empty, set the contextMenu to null (because you can't r click an empty cell)
                                cell.setContextMenu(null);
                            } else {
                                // set the cell's context menu to one just created to delete something
                                cell.setContextMenu(todoListContextMenu);
                            }
                        });
                return cell;
            }
        });

        // creating a context menu
        todoListContextMenu =  new ContextMenu();
        // adding delete to the context menu
        MenuItem deleteMenuItem = new MenuItem("Delete");
        // this line actually adds the Delete to the context menu
        todoListContextMenu.getItems().addAll(deleteMenuItem);
        // now add the event handler to the delete menu item
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // to delete the todoItem, first select the item to delete
                TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                // then call
                deleteItem(item);
            }
        });
    }

    @FXML
    public void showNewItemDialog() {
        // to show the new dialog create an instance of the dialog first
        Dialog<ButtonType> dialog = new Dialog<>();
        // it is good practice to set an owner of the new dialog
        // to be the owner of the dialog, the type has to be of type Window
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        // set a dialog title
        dialog.setTitle("Add a New Todo Item");
        // alternative to headText in fxml
        // dialog.setHeaderText("Use the dialog to create a new todoItem");

        // getting the fxml resource first
        FXMLLoader fxmlLoader =  new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));


        try {
            // loading the dialog fxml here
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            // if the load fails, display message and catch IOException
            System.out.println("Could not load the dialog");
            e.printStackTrace();
        }
        // JavaFX has pre-defined button types for use (as well as custom buttons)
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        // now the controller gets the feedback of what button the user pressed
        Optional<ButtonType> buttonPressed = dialog.showAndWait();
        if(buttonPressed.isPresent() && buttonPressed.get() == ButtonType.OK) {
            // to call the new processResults method from the dialog controller
            DialogController dController = fxmlLoader.getController();
            TodoItem newItem = dController.processResults();

            // temp fix to update the TodoItem List appearing on the left hand side of the application
            // updated code to use data binding, therefore this fix is not needed anymore
            // todoListView.getItems().setAll(TodoData.getInstance().getTodoItems());

            // selecting the newest todoItem added to the list
            todoListView.getSelectionModel().select(newItem);

            // this was used for testing purposes
//            System.out.println("OK button pressed");
//        } else {
//            System.out.println("Cancel button pressed");

        }
    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        // first get the item the user has selected from the todoListView
        TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
        // perform a null check on the selectedItem and if the DELETE key is pressed
        if(selectedItem != null) {
            if(keyEvent.getCode().equals(KeyCode.DELETE)) {
                deleteItem(selectedItem);
            }
        }
    }

    @FXML
    public void handleClickListView() {
        // get the selected item from the todoList
        TodoItem item = todoListView.getSelectionModel().getSelectedItem();

//        StringBuilder sb = new StringBuilder(item.getDetails());
//        sb.append("\n\n\n\n");
//        sb.append("Due: ");
//        sb.append(item.getDeadline().toString());
//        todoItemDetails.setText(sb.toString());

        // will display the details property from the TodoItem class in the textArea
        todoItemDetails.setText(item.getDetails());
        // display the deadline data (which is a date)
        deadlineLabel.setText(item.getDeadline().toString());
    }

    public void deleteItem(TodoItem item) {
        // a nice confirmation dialog before allowing a delete to happen
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        // set the properties of the confirmation dialog
        alert.setTitle("Delete Todo Item");
        alert.setHeaderText("Delete Todo Item: " + item.getShortDescription());
        alert.setContentText("Please press OK to confirm, Cancel to quit");
        // show the dialog now
        Optional<ButtonType> result = alert.showAndWait();
        // now depending on the button selected react accordingly
        if(result.isPresent() && result.get() == ButtonType.OK) {
            TodoData.getInstance().deleteTodoItem(item);
        }
    }

    // this is known as an event handler even if the EventHandler() call is not present
    @FXML
    public void handleFilterButton() {
        // toggling back and forth the user wants to see if the selected item (while filtered) is present (while unfiltered)
        TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();

        // first determine if the toggle button is selected on not
        if(filterToggleButton.isSelected()) {
            // if pressed HERE is the location to determine the filtering criteria
            // UPDATE because the predicate is initialized above, can remove @Override here
            filteredList.setPredicate(wantTodaysTodoItems);
            // test first if filtered list is empty
            if(filteredList.isEmpty()) {
                // if the list is empty (clear the center display)
                todoItemDetails.clear();
                // and deadline label
                deadlineLabel.setText("");
                // if the selectedItem is in the filter list
            } else if(filteredList.contains(selectedItem)) {
                todoListView.getSelectionModel().select(selectedItem);
            } else {
                // the list is NOT empty, BUT the selectedItem is not present, just select first available in list
                todoListView.getSelectionModel().selectFirst();
            }
        } else {
            // if not pressed (by setting true here, ALL items are returned ((no filtering is performed))
            // UPDATE because the predicate is initialized above, can remove @Override here
            filteredList.setPredicate(wantAllTodoItems);
            // after setting the predicate
            todoListView.getSelectionModel().select(selectedItem);
        }
    }

    // handle the exit from the application (EventHandler)
    @FXML
    public void handleExit() {
        Platform.exit();
    }

/************ this section of code is ONLY for testing *************/
    @FXML
    // grow the scale of the control on mouse enter
    public void handleMouseEnter() {
        testLabel.setScaleX(2.0);
        testLabel.setScaleY(2.0);
    }
    @FXML
    // return the scale of the control to normal on mouse exit
    public void handleMouseExit() {
        testLabel.setScaleX(1.0);
        testLabel.setScaleY(1.0);
    }

    @FXML
    public void handleOpenClick() {
        // the process of saving a file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Application File");
        // any number of extensions can be added (txt and pdf are added here)
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text", "*.txt"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                // the *.* is the catch-all for all file types
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                // can have multiple extensions in one line
                new FileChooser.ExtensionFilter("Image Formats", "*.jpg", "*.png", "*.gif")
        );
        // similar to the other dialog created, the parent window needs to be passed to showOpenDialog in order to make the fileChooser modal
        // in this setup the user has to select a file in order to gain control of the original modal
        // and only one file chooser at a time
        // fileChooser only allows the user to select a file, DirectoryChooser however
        // fileChooser.showOpenDialog(mainBorderPane.getScene().getWindow());

        // this is an example of a directory chooser
        // DirectoryChooser directoryChooser =  new DirectoryChooser();

        File file = fileChooser.showSaveDialog(mainBorderPane.getScene().getWindow());
        // to be able to open multiple files at once
        // List<File> file = fileChooser.showOpenMultipleDialog(mainBorderPane.getScene().getWindow());
        if(file != null) {
            // for multiple files
//            for(int i = 0; i < file.size(); i++) {
//                System.out.println(file.get(i));
//            }
            System.out.println(file.getPath());
        } else {
            System.out.println("Chooser was cancelled");
        }
    }

    @FXML
    public void handleLinkClick() {
        // this is the eventHandler for the HyperLink...However again, it could open local dialog or go to web page
        // first is to open the system's default browser for page
//        try {
//            Desktop.getDesktop().browse(new URI("http://www.javafx.com"));
//        } catch (IOException | URISyntaxException e) {
//            e.printStackTrace();
//        }
        // second is to open a WebView control within the application
        WebEngine webEngine = webView.getEngine();
        webEngine.load("http://www.javafx.com");
    }
/************ this section of code is ONLY for testing *************/
}
