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

public class MenuPrincipalController {
    @FXML
    private Label userIdLabel;

    private int userId;
    private Connection connection;

    public void initialize() {
        userIdLabel.setText("ID user: " + userId);
    }

    /**
     * U can see your id of user.
     * @param id_user
     */
    public void setUserId(int id_user) {
        this.userId = id_user;
        initialize(); // Actualizar la etiqueta con el nuevo valor de userId
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * View the filmview where u can see all the films
     */
    @FXML
    private void handleMoviesButton() {
        Test2View.getInstance().handleMoviesButton();
    }

    /**
     * See the booking of a user, and add a new reservation
     */
    @FXML
    public void handleBookingsButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/Hamm/Controller/ListBookings.fxml"));
            Parent bookingsRoot = fxmlLoader.load();

            Stage bookingsStage = new Stage();
            bookingsStage.setTitle("Booking List");
            bookingsStage.setScene(new Scene(bookingsRoot, 880, 380));

            ListBookingsController bookingsController = fxmlLoader.getController();
            bookingsController.setConnection(connection); // Pasar la conexión al controlador de la lista de reservas
            bookingsController.setUserId(userId); // Pasar el ID del usuario al controlador de la lista de reservas
            bookingsController.execute();
            bookingsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * View the carview where u can add a car.
     */
    @FXML
    public void handleCarButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/Hamm/Controller/AddCar.fxml"));
            Parent carRoot = fxmlLoader.load();

            Stage carStage = new Stage();
            carStage.setTitle("Car");
            carStage.setScene(new Scene(carRoot, 600, 500));

            CarController carController = fxmlLoader.getController();
            carController.setConnection(connection); // Pasar la conexión al controlador de coches

            carStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *Back to login view
     */
    @FXML
    public void handleSignOffButton() {
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
