package org.Hamm.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.Hamm.Model.Connections.ConnectionMySQL;
import org.Hamm.Model.DAO.CarDAO;
import org.Hamm.Model.DAO.ReservationDAO;
import org.Hamm.Model.Domain.Car;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListCarControllerAdmin {
    private Connection connection;
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @FXML
    private TableView<Car> tableView;
    @FXML
    private TableColumn<Car, Integer> idColumn;
    @FXML
    private TableColumn<Car, String> tuitionColumn;
    @FXML
    private TableColumn<Car, String> brandColumn;
    @FXML
    private TableColumn<Car, String> modelColumn;
    @FXML
    private TableColumn<Car, Boolean> rentedColumn;
    @FXML
    private TextField tuitionTextField;
    @FXML
    private TextField brandTextField;
    @FXML
    private TextField modelTextField;
    @FXML
    private CheckBox isRentedCheckBox;
    @FXML
    private TextField searchField;

    private CarDAO carDAO;

    public void execute() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id_car"));
        tuitionColumn.setCellValueFactory(new PropertyValueFactory<>("tuition"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        rentedColumn.setCellValueFactory(new PropertyValueFactory<>("rented"));

        carDAO = new CarDAO();

        try {
            Connection connection = ConnectionMySQL.getConnect();
            carDAO = new CarDAO(connection);
            List<Car> cars = carDAO.findAll();
            tableView.getItems().clear();
            tableView.getItems().addAll(cars);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                List<Car> cars = carDAO.search(newValue);
                tableView.getItems().clear();
                tableView.getItems().addAll(cars);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleCarDoubleClick();
            }
        });
    }

    @FXML
    public void handleDeleteButton() {
        Car selectedCar = tableView.getSelectionModel().getSelectedItem();
        if (selectedCar != null) {
            try {
                carDAO.delete(selectedCar);
                tableView.getItems().remove(selectedCar);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleAddButton() {
        String tuition = tuitionTextField.getText();
        String brand = brandTextField.getText();
        String model = modelTextField.getText();
        boolean isRented = isRentedCheckBox.isSelected();

        Car newCar = new Car(tuition, brand, model, isRented);

        try {
            carDAO.insert(newCar);
            clearInputFields();
            tableView.getItems().add(newCar);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleUpdateButton() {
        Car selectedCar = tableView.getSelectionModel().getSelectedItem();
        if (selectedCar != null) {
            String tuition = tuitionTextField.getText();
            String brand = brandTextField.getText();
            String model = modelTextField.getText();
            boolean isRented = isRentedCheckBox.isSelected();

            selectedCar.setTuition(tuition);
            selectedCar.setBrand(brand);
            selectedCar.setModel(model);
            selectedCar.setRented(isRented);

            try {
                carDAO.update(selectedCar);
                clearInputFields();
                tableView.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.C) {
            Car selectedCar = tableView.getSelectionModel().getSelectedItem();
            if (selectedCar != null) {
                tuitionTextField.setText(selectedCar.getTuition());
                brandTextField.setText(selectedCar.getBrand());
                modelTextField.setText(selectedCar.getModel());
                isRentedCheckBox.setSelected(selectedCar.isRented());
            }
        }
    }

    /**
     * Method for clear all the fields.
     */
    private void clearInputFields() {
        tuitionTextField.clear();
        brandTextField.clear();
        modelTextField.clear();
        isRentedCheckBox.setSelected(false);
    }

    /**
     * Method for search in the table.
     */
    @FXML
    public void handleSearchButton() {
        String searchTerm = searchField.getText();

        try {
            List<Car> cars = carDAO.search(searchTerm);
            tableView.getItems().clear();
            tableView.getItems().addAll(cars);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for bring a car to the booking view.
     */
    public void handleCarDoubleClick() {
        Car selectedCar = tableView.getSelectionModel().getSelectedItem();
        if (selectedCar != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/Hamm/Controller/ListBookingsAdmin.fxml"));
                Parent root = loader.load();

                ListBookingsControllerAdmin bookingsController = loader.getController();
                bookingsController.setConnection(connection);
                bookingsController.setCarTuiton(selectedCar.getTuition());
                bookingsController.execute();

                Stage stage = (Stage) tableView.getScene().getWindow();
                stage.close();

                Stage bookingsStage = new Stage();
                bookingsStage.setTitle("Bookings List");
                bookingsStage.setScene(new Scene(root, 880, 380));
                bookingsStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
