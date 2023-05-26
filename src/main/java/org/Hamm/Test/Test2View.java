package org.Hamm.Test;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.Hamm.Controller.*;
import org.Hamm.Model.Domain.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Test2View extends Application {

    private Connection connection;

    private static Test2View instance; // Nueva variable estática para guardar la instancia actual de Test2View

    @Override
    public void start(Stage primaryStage) throws IOException {
        instance = this; // Guardar la instancia actual de Test2View

        Parent loginRoot = FXMLLoader.load(getClass().getResource("/org/Hamm/Controller/Login.fxml"));

        primaryStage.setTitle("Test View");
        primaryStage.setScene(new Scene(loginRoot, 600, 400));
        primaryStage.show();
    }

    // Método para abrir la ventana del menú principal
    public void openMenuWindow(User user, Connection connection) throws IOException {
        this.connection = connection;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/Hamm/Controller/MenuPrincipal.fxml"));
        Parent menuPrincipalRoot = fxmlLoader.load();

        Stage menuPrincipalStage = new Stage();
        menuPrincipalStage.setTitle("MenuPrincipal");
        menuPrincipalStage.setScene(new Scene(menuPrincipalRoot, 600, 500));

        MenuPrincipalController menuPrincipalController = fxmlLoader.getController();
        menuPrincipalController.setConnection(connection);
        menuPrincipalController.setUserId(user.getId_user());

        menuPrincipalStage.show();
    }

    // Método para abrir la ventana del menú administrador
    public void openAdminWindow(User user, Connection connection) throws IOException {
        this.connection = connection;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/Hamm/Controller/Adminview.fxml"));
        Parent adminRoot = fxmlLoader.load();

        Stage adminStage = new Stage();
        adminStage.setTitle("Admin");
        adminStage.setScene(new Scene(adminRoot, 600, 400));

        AdminController adminController = fxmlLoader.getController();
        adminController.setConnection(connection);
        adminController.setUserId(user.getId_user());

        adminStage.show();
    }

    // Método para abrir la ventana de registro
    public void openRegisterWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/Hamm/Controller/Register.fxml"));
            Parent registerRoot = fxmlLoader.load();

            Stage registerStage = new Stage();
            registerStage.setTitle("Register");
            registerStage.setScene(new Scene(registerRoot, 600, 500));

            RegisterController registerController = fxmlLoader.getController();
            registerController.setPrimaryStage(registerStage);

            registerStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Manejador de evento para el botón de películas (usuario normal)
    @FXML
    public void handleMoviesButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/Hamm/Controller/ListFilm.fxml"));
            Parent filmListRoot = fxmlLoader.load();

            Stage filmListStage = new Stage();
            filmListStage.setTitle("Lista de películas");
            filmListStage.setScene(new Scene(filmListRoot, 785, 380));

            ListFilmController filmListController = fxmlLoader.getController();

            filmListController.setConnection(connection); // Pasar la conexión al controlador de la lista de películas
            filmListController.execute();
            filmListStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Manejador de evento para el botón de películas (admin)
    @FXML
    public void handleMoviesButtonAdmin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/Hamm/Controller/ListFilmAdmin.fxml"));
            Parent filmListRoot = fxmlLoader.load();

            Stage filmListStage = new Stage();
            filmListStage.setTitle("Film List");
            filmListStage.setScene(new Scene(filmListRoot, 970, 403));

            ListFilmControllerAdmin filmListController = fxmlLoader.getController();

            filmListController.setConnection(connection);
            filmListController.execute();
            filmListStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //ventana lista user para admin
    public void handleUserButtonAdmin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/Hamm/Controller/ListUserAdmin.fxml"));
            Parent userListRoot = fxmlLoader.load();

            Stage userListStage = new Stage();
            userListStage.setTitle("User List");
            userListStage.setScene(new Scene(userListRoot, 970, 443));

            ListUserControllerAdmin userListController = fxmlLoader.getController();

            userListController.setConnection(connection);
            userListController.execute();
            userListStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Test2View getInstance() {
        return instance; // Devolver la instancia actual de Test2View
    }

    public static void main(String[] args) {
        launch(args);
    }
}
