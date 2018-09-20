package com.contactApp.bjoseph;

import com.contactApp.bjoseph.dataModel.Contact;
import com.contactApp.bjoseph.dataModel.ContactData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Optional;

public class Controller {

    private ContactData data;

    @FXML
    private BorderPane mainPanel;
    @FXML
    private TableView<Contact> contactsTable;

    public void initialize() {
        // create an instance of the contactData class
        data = new ContactData();
        data.loadContacts();
        contactsTable.setItems(data.getContacts());
        // a way to stretch the columns to the width of the TableView pane
        contactsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    public void showNewContactDialog() {
        // to show the new dialog create an instance of the dialog first
        Dialog<ButtonType> contactDialog = new Dialog<>();
        // it is good practice to set an owner of the new dialog
        // to be the owner of the dialog, the type has to be of type Window
        contactDialog.initOwner(mainPanel.getScene().getWindow());
        // set a dialog title
        contactDialog.setTitle("Add a New Contact");
        // getting the fxml resource first
        FXMLLoader fxmlLoader =  new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("contactDialog.fxml"));

        // because the dialog might sometimes fail to load need a try/catch block
        try {
            // loading the dialog fxml here
            contactDialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            // if the load fails, display message and catch IOException
            System.out.println("Could not load the dialog");
            e.printStackTrace();
        }
        // JavaFX has pre-defined button types for use (as well as custom buttons)
        contactDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        contactDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        // now the controller gets the feedback of what button the user pressed
        Optional<ButtonType> buttonPressed = contactDialog.showAndWait();
        if(buttonPressed.isPresent() && buttonPressed.get() == ButtonType.OK) {
            // to call the new processResults method from the dialog controller
            ContactController dController = fxmlLoader.getController();
            Contact newContact = dController.processResults();
            // add the new contact
            data.addContact(newContact);
            // save the new contact to the xml file by saving all of the contacts
            data.saveContacts();
        }
    }

    @FXML
    public void showEditContactDialog() {
        // first need to know which contact the user has selected to edit
        Contact selectedContact = contactsTable.getSelectionModel().getSelectedItem();
        // determine if the contact is valid
        if(selectedContact == null) {
            Alert nullAlert = new Alert(Alert.AlertType.WARNING);
            nullAlert.setTitle("No Contact Selected");
            nullAlert.setHeaderText(null);
            nullAlert.setContentText("Please Select the Contact You Want to Edit");
            nullAlert.showAndWait();
            // need return or a NullPointerException occurs
            return;
        }

        // now user has made a selection
        Dialog<ButtonType> editDialog =  new Dialog<>();
        editDialog.initOwner(mainPanel.getScene().getWindow());
        editDialog.setTitle("Edit Contact");
        // getting the fxml resource first
        FXMLLoader fxmlLoader =  new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("contactDialog.fxml"));
        // because the dialog might sometimes fail to load need a try/catch block
        try {
            // loading the dialog fxml here
            editDialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            // if the load fails, display message and catch IOException
            System.out.println("Could not load the dialog");
            e.printStackTrace();
        }
        // JavaFX has pre-defined button types for use (as well as custom buttons)
        editDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        editDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        // This is a change from add -- the controller needs to populate the edit dialog before a button is pressed
        // so the controller method is called first before reading what button is pressed
        ContactController dController = fxmlLoader.getController();
        dController.editContact(selectedContact);

        // NOW get the feedback of what button the user pressed
        Optional<ButtonType> buttonPressed = editDialog.showAndWait();
        if(buttonPressed.isPresent() && buttonPressed.get() == ButtonType.OK) {
            // if OK is pressed actually update the data record
            dController.updateContact(selectedContact);
            // save the new contact to the xml file by saving all of the contacts
            data.saveContacts();
        }
    }

    @FXML
    public void confirmDeleteContact() {
        // first need to know which contact the user has selected to edit
        Contact selectedContact = contactsTable.getSelectionModel().getSelectedItem();
        // determine if the contact is valid
        if(selectedContact == null) {
            Alert nullAlert = new Alert(Alert.AlertType.WARNING);
            nullAlert.setTitle("No Contact Selected");
            nullAlert.setHeaderText(null);
            nullAlert.setContentText("Please Select the Contact You Want to Delete");
            nullAlert.showAndWait();
            // need return or a NullPointerException occurs
            return;
        }

        // a matching contact has been found, now add confirmation alert to delete
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Contact");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Please Confirm Deletion for: " + selectedContact.getFirstName() + " " + selectedContact.getLastName());
        // Handle which button is pressed from Alert
        Optional<ButtonType> buttonPressed = confirmationAlert.showAndWait();
        if(buttonPressed.isPresent() && buttonPressed.get() == ButtonType.OK) {
            // if OK is pressed actually update the data record
            data.deleteContact(selectedContact);
            // save the new contact to the xml file by saving all of the contacts
            data.saveContacts();
        }
    }
}
