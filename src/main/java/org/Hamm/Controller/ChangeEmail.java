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

    /**
     * Method for change the user email.
     */
    @FXML
    private void changeEmail() {
        String newEmail = newEmailField.getText();
        String password = passwordField.getText();

        UserDAO userDAO = new UserDAO();
        try {
            User user = userDAO.findByEmail(emailField.getText());
            if (user == null) {
                statusLabel.setText("No user found with the provided email.");
                return;
            }

            if (!PasswordUtils.verifyPassword(password, user.getPassword())) {
                statusLabel.setText("Password does not match.");
                return;
            }

            User existingUser = userDAO.findByEmail(newEmail);
            if (existingUser != null) {
                statusLabel.setText("The new email is already in use.");
                return;
            }

            User updatedUser = userDAO.updateEmail(user, newEmail);

            if (updatedUser != null) {
                System.out.println("Email updated successfully.");
                statusLabel.setText("Email change successful!");
            } else {
                System.out.println("Failed to update the email.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        emailField.clear();
        newEmailField.clear();
        passwordField.clear();
    }
}
