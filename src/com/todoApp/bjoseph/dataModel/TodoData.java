package com.todoApp.bjoseph.dataModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

// this class represents a Singleton Design Pattern
public class TodoData {

    private static TodoData instance = new TodoData();
    private static String filename = "TodoListItems.txt";
    // changing this to use data binding
    // private List<TodoItem> todoItems;
    private ObservableList<TodoItem> todoItems;
    private DateTimeFormatter formatter;

    public static TodoData getInstance() {
        return instance;
    }

    // this is a private constructor (this blocks any class from getting a different instance of the class) hence Singleton
    private TodoData() {
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public ObservableList<TodoItem> getTodoItems() { return todoItems; }
    // setTodoItems is no longer used because the TodoData Singleton loads the records for the application
    //public void setTodoItems(List<TodoItem> todoItems) { this.todoItems = todoItems; }

    // method to load the TodoItems
    public void loadTodoItems() throws IOException {                                                   // @FXML
        // Using a FXCollections.observableArrayList() because this corresponds to the JavaFX Controller (private ListView<TodoItem> todoListView;)
        todoItems = FXCollections.observableArrayList();
        Path path = Paths.get(filename);
        // BufferedReader to read the data
        BufferedReader br = Files.newBufferedReader(path);

        // String represents the total data for each line
        String input;

        try{
            while((input = br.readLine()) != null) {
                // each item in the String[] represents a TodoItem
                String[] itemPieces =  input.split("\t");
                // now each element of the TodoItem needs to be created
                String shortDescription = itemPieces[0];
                String details = itemPieces[1];
                String deadlineString = itemPieces[2];

                //Now convert the String to a Date
                LocalDate date = LocalDate.parse(deadlineString, formatter);

                //Now build the original object TodoItem
                TodoItem todoItem = new TodoItem(shortDescription, details, date);
                // add the new item to the todoItemList
                todoItems.add(todoItem);
            }
        } finally {
            if(br != null) {
                br.close();
            }
        }
    }

    // Method to save the data to a file
    public void storeTodoItems() throws IOException {
        Path path = Paths.get(filename);
        // BufferedWriter to write the data
        BufferedWriter bw = Files.newBufferedWriter(path);
        try {
            // iterate through the TodoItems (the List) and save them to a .txt file
            Iterator<TodoItem> iterator = todoItems.iterator();
            while(iterator.hasNext()) {
                TodoItem item = iterator.next();
                bw.write(String.format("%s\t%s\t%s", item.getShortDescription(), item.getDetails(), item.getDeadline().format(formatter)));
                // the line above represents one full data entry into the .txt file
                bw.newLine();
            }
        } finally {
            if(bw != null) {
                bw.close();
            }
        }
    }

    public void addTodoItem(TodoItem newItem) {
        todoItems.add(newItem);
    }

    public void deleteTodoItem(TodoItem item) {
        todoItems.remove(item);
    }
}
