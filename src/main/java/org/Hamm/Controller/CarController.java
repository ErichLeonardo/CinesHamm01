package org.Hamm.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.Hamm.Model.DAO.CarDAO;
import org.Hamm.Model.Domain.Car;
import org.Hamm.Utils.Validator;

public class CarController {
    private Connection connection;
    private CarDAO carDAO;

    @FXML
    private TextField tuitionField;
    @FXML
    private TextField brandField;
    @FXML
    private TextField modelField;
    @FXML
    private Label statusLabel;

    public void setConnection(Connection connection) {
        this.connection = connection;
        carDAO = new CarDAO(connection);
    }

    /**
     * Button for add a new car
     */
    @FXML
    private void handleAddCar() {
        String url = "jdbc:mysql://localhost:3306/CinesHamm";
        String user = "root";
        String pwd = "";

        try (Connection connection = DriverManager.getConnection(url, user, pwd)) {
            CarDAO carDAO = new CarDAO(connection);

            String tuition = tuitionField.getText();
            String brand = brandField.getText();
            String model = modelField.getText();
            boolean isRented = false;

            if (Validator.validateTuition(tuition)) {
                Car newCar = new Car(tuition, brand, model, isRented);
                carDAO.insert(newCar);

                statusLabel.setText("Car added successfully!");
                tuitionField.clear();
                brandField.clear();
                modelField.clear();
            } else {
                statusLabel.setText("Invalid tuition!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
