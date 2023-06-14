package org.Hamm.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.Hamm.Test.Test2View;



import java.io.IOException;
import java.sql.Connection;

public class AdminController {

    @FXML
    private Label userIdLabel;

    private int userId;

    public void initialize() {
        userIdLabel.setText("ID admin: " + userId);
    }

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setUserId(int id_user) {
        this.userId = id_user;
        initialize(); // Actualizar la etiqueta con el nuevo valor de userId
    }

    /**
     * View the filmview for admin, u can add, update and delete films
     */
    @FXML
    private void handleMoviesButtonAdmin() {
        Test2View.getInstance().handleMoviesButtonAdmin();
    }

    /**
     * view the userview for admin, u can add, update and delete users
     */
    @FXML
    private void handleUserButtonAdmin() {
        Test2View.getInstance().handleUserButtonAdmin();
    }

    /**
     * view the reservation view where u can delete a reservation or add one new reservation
     */
    @FXML
    public void handleBookingsButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/Hamm/Controller/ListBookingsAdmin.fxml"));
            Parent bookingsRoot = fxmlLoader.load();

            Stage bookingsStage = new Stage();
            bookingsStage.setTitle("Bookings List");
            bookingsStage.setScene(new Scene(bookingsRoot, 880, 380));

            ListBookingsControllerAdmin bookingsController = fxmlLoader.getController();
            bookingsController.setConnection(connection); // Pasar la conexión al controlador de la lista de reservas
            bookingsController.execute();

            bookingsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleEuroButtonAdmin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/Hamm/Controller/EuroView.fxml"));
            Parent euroRoot = fxmlLoader.load();

            Stage euroStage = new Stage();
            euroStage.setTitle("Euro View");
            euroStage.setScene(new Scene(euroRoot, 600, 400));

            EuroController euroController = fxmlLoader.getController();
            euroController.setConnection(connection); // Pasar la conexión al controlador EuroController

            euroController.initialize(); // Inicializar el controlador EuroController

            euroStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    public void handleSignOffAdminButton() {
        try {
            Stage currentStage = (Stage) userIdLabel.getScene().getWindow();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/Hamm/Controller/Login.fxml"));
            Parent loginRoot = fxmlLoader.load();

            Scene loginScene = new Scene(loginRoot, 600, 400);

            currentStage.setScene(loginScene);

            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
