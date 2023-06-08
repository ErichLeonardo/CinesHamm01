package org.Hamm.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.Hamm.Model.DAO.UserDAO;
import org.Hamm.Model.Domain.User;
import org.Hamm.Test.Test2View;
import org.Hamm.Utils.PasswordUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private Test2View test2View;

    private User authenticatedUser;

    public LoginController() {
        test2View = Test2View.getInstance(); // Obtener la instancia actual de Test2View
    }

    @FXML
    private void login() {
        String url = "jdbc:mysql://localhost:3306/CinesHamm";
        String user = "root";
        String pwd = "";

        try (Connection connection = DriverManager.getConnection(url, user, pwd)) {
            UserDAO userDAO = new UserDAO(connection);

            String email = emailField.getText();
            String password = passwordField.getText();

            // Verificar las credenciales del usuario
            User user2 = userDAO.findByEmail(email);
            if (user2 != null && PasswordUtils.verifyPassword(password, user2.getPassword())) {
                // Credenciales válidas, realizar acciones necesarias

                // Asignar el usuario autenticado
                authenticatedUser = user2;

                // Limpiar campos y mensajes de error
                emailField.clear();
                passwordField.clear();
                errorLabel.setText("");

                // Abrir la siguiente ventana
                if (authenticatedUser.isIs_admin()) {
                    test2View.openAdminWindow(authenticatedUser, connection); // Abrir ventana de administrador

                } else {
                    test2View.openMenuWindow(authenticatedUser, connection); // Abrir ventana de usuario normal

                }
            } else {
                // Credenciales inválidas, mostrar mensaje de error
                errorLabel.setText("Credenciales inválidas");
            }

            Stage currentStage = (Stage) emailField.getScene().getWindow();
            currentStage.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Button to open the registerview
     */
    @FXML
    private void register() {
        Stage currentStage = (Stage) emailField.getScene().getWindow();
        currentStage.close();

        test2View.openRegisterWindow();
    }
}
