package org.Hamm.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.Hamm.Model.DAO.UserDAO;
import org.Hamm.Model.Domain.User;
import org.Hamm.Utils.PasswordUtils;

import java.sql.SQLException;

public class ChangeEmail {
    @FXML
    private TextField emailField;
    @FXML
    private TextField newEmailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button changeButton;
    @FXML
    private Label statusLabel;

    public void setEmailField(String email) {
        emailField.setText(email);
    }

    private LoginController loginController;

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }



    @FXML
    private void changeEmail() {
        String newEmail = newEmailField.getText();
        String password = passwordField.getText();

        // Get the current user by their email
        UserDAO userDAO = new UserDAO();
        try {
            User user = userDAO.findByEmail(emailField.getText());
            if (user == null) {
                // Show an error message or take appropriate action
                System.out.println("No user found with the provided email.");
                return;
            }

            // Verify if the current password matches
            if (!PasswordUtils.verifyPassword(password, user.getPassword())) {
                // Password does not match, show an error message or take appropriate action
                statusLabel.setText("Password does not match.");
                return;
            }

            // Check if the new email is already in use
            User existingUser = userDAO.findByEmail(newEmail);
            if (existingUser != null) {
                // The new email is already in use, show an error message or take appropriate action
                statusLabel.setText("The new email is already in use.");
                return;
            }

            // Update the user's email with the new value
            user.setEmail(newEmail);
            User updatedUser = userDAO.update(user);

            if (updatedUser != null) {
                // Email was successfully updated
                System.out.println("Email updated successfully.");

                // Actualizar el valor del campo emailField en el controlador de inicio de sesión
                loginController.setEmailField(newEmail);
            } else {
                // Failed to update the email
                System.out.println("Failed to update the email.");
            }
        } catch (SQLException e) {
            // Handle the exception
            e.printStackTrace();
        }

        // Reset the fields after performing the action
        statusLabel.setText("Email change successful!");
        emailField.clear();
        newEmailField.clear();
        passwordField.clear();
    }


}