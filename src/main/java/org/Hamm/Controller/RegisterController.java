package org.Hamm.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.Hamm.Model.DAO.UserDAO;
import org.Hamm.Model.Domain.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RegisterController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField phoneField;

    private Connection connection;
    private Stage primaryStage;

    @FXML
    private void initialize() {
        // Establecer la conexión con la base de datos (ajusta los valores según tu configuración)
        String url = "jdbc:mysql://localhost:3306/CinesHamm";
        String user = "root";
        String pwd = "";

        try {
            connection = DriverManager.getConnection(url, user, pwd);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void register() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String name = nameField.getText();
        String surname = surnameField.getText();
        String phone = phoneField.getText();

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);
        user.setPhone(phone);

        try {
            UserDAO userDAO = new UserDAO(connection);
            User insertedUser = userDAO.insert(user);

            if (insertedUser != null) {
                // Registro exitoso, puedes realizar alguna acción adicional si deseas
                System.out.println("Usuario registrado exitosamente");

                // Volver a la vista de inicio de sesión
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/Hamm/Controller/Login.fxml"));
                Parent loginRoot = fxmlLoader.load();

                Scene scene = new Scene(loginRoot, 600, 400);
                primaryStage.setScene(scene);
                primaryStage.show();
            } else {
                // Error al registrar el usuario, puedes mostrar un mensaje de error
                System.out.println("Error al registrar el usuario");
            }
        } catch (SQLException | IOException e) {
            // Manejo de excepciones de SQL y de carga de FXML
            e.printStackTrace();
        }
    }

    @FXML
    private void BacktoLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/Hamm/Controller/Login.fxml"));
            Parent loginRoot = fxmlLoader.load();
            Scene scene = new Scene(loginRoot, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

