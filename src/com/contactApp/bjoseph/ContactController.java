package com.contactApp.bjoseph;

import com.contactApp.bjoseph.dataModel.Contact;
import com.contactApp.bjoseph.dataModel.ContactData;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ContactController {

    // match the fx:id's from the fxml file
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField notesTextField;

    // method to process the results from each control
    // instead of being void return the new TodoItem to be selected by the MainController
    public Contact processResults() {
        String firstName;
        String lastName;
        String phoneNumber;
        String notes;

        // trim to remove any extra white spaces
        if(firstNameField.getText().isEmpty()) {
            firstName = "First Name Not Entered by User";
        } else {
            firstName = firstNameField.getText().trim();
        }

        if(lastNameField.getText().isEmpty()) {
            lastName = "Last Name Not Entered By User";
        } else {
            lastName = lastNameField.getText().trim();
        }

        if(phoneNumberField.getText().isEmpty()) {
            phoneNumber = "Phone Number Not Entered By User";
        } else {
            phoneNumber = phoneNumberField.getText().trim();
        }

        if(notesTextField.getText().isEmpty()) {
            notes = "Notes Not Entered By User";
        } else {
            notes = notesTextField.getText().trim();
        }

        // return the new item
        return new Contact(firstName, lastName, phoneNumber, notes);
    }

    public void editContact(Contact selectedContact) {
        // simply populate the fields for the Contact class (this info is coming from the selected contact from TableView)
        firstNameField.setText(selectedContact.getFirstName());
        lastNameField.setText(selectedContact.getLastName());
        phoneNumberField.setText(selectedContact.getPhoneNumber());
        notesTextField.setText(selectedContact.getNotes());
    }

    public void updateContact(Contact updateContact) {
        // simply populate the fields for the Contact class (this info is coming from the dialog)
        updateContact.setFirstName(firstNameField.getText());
        updateContact.setLastName(lastNameField.getText());
        updateContact.setPhoneNumber(phoneNumberField.getText());
        updateContact.setNotes(notesTextField.getText());
    }
}
