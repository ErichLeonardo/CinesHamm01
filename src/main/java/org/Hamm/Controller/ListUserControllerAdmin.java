package org.Hamm.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.stage.Stage;
import org.Hamm.Model.DAO.UserDAO;
import org.Hamm.Model.Domain.Car;
import org.Hamm.Model.Domain.User;
import org.Hamm.Utils.PasswordUtils;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListUserControllerAdmin {
    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @FXML
    private TableView<User> tableView;
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> passwordColumn;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> surnameColumn;
    @FXML
    private TableColumn<User, String> phoneColumn;
    @FXML
    private TableColumn<User, Boolean> isAdminColumn;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField searchField;
    @FXML
    private CheckBox isAdminCheckBox;


    private UserDAO userDAO;

    /**
     * List of my users data base
     */
    public void execute() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        isAdminColumn.setCellValueFactory(new PropertyValueFactory<>("is_admin"));

        userDAO = new UserDAO(); // Pasar la conexión al constructor de UserDAO

        try {
            List<User> users = userDAO.findAll();
            tableView.getItems().addAll(users);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Configurar evento de escucha para búsqueda en tiempo real
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                List<User> users = userDAO.search(newValue);
                tableView.getItems().clear(); // Limpiar los elementos existentes en la tabla
                tableView.getItems().addAll(users); // Agregar los usuarios encontrados a la tabla
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleUserDoubleClick();
            }
        });


    }

    /**
     * Method to delete a user
     */
    @FXML
    public void handleDeleteButton() {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            System.out.println("No se ha seleccionado ningún usuario.");
            return;
        }

        if (selectedUser.isIs_admin()) {
            System.out.println("El usuario es un administrador y no puede ser borrado.");
            return;
        }

        userDAO.delete(selectedUser);
        tableView.getItems().remove(selectedUser);
        System.out.println("Usuario eliminado: " + selectedUser);
    }

    /**
     * Method to update a user
     * @throws SQLException
     */
    @FXML
    public void handleUpdateButton() throws SQLException {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            System.out.println("No se ha seleccionado ningún usuario.");
            return;
        }

        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        String name = nameTextField.getText();
        String surname = surnameTextField.getText();
        String phone = phoneTextField.getText();
        boolean isAdmin = isAdminCheckBox.isSelected();

        if (password != null && !password.isEmpty()) {
            String hashedPassword = PasswordUtils.hashPassword(password);
            selectedUser.setPassword(hashedPassword);
        }

        selectedUser.setEmail(email);
        selectedUser.setName(name);
        selectedUser.setSurname(surname);
        selectedUser.setPhone(phone);
        selectedUser.setIs_admin(isAdmin);

        userDAO.update(selectedUser);

        tableView.refresh();
        clearInputFields();
    }

    /**
     * Method for copy one selected user and bring the atributtes to the fields.
     * @param event
     */
    @FXML
    public void handleKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.C) {
            User selectedUser = tableView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                emailTextField.setText(selectedUser.getEmail());
                passwordTextField.setText(selectedUser.getPassword());
                nameTextField.setText(selectedUser.getName());
                surnameTextField.setText(selectedUser.getSurname());
                phoneTextField.setText(selectedUser.getPhone());
            }
        }
    }

    /**
     * Method for clear the fields.
     */
    private void clearInputFields() {
        emailTextField.clear();
        passwordTextField.clear();
        nameTextField.clear();
        surnameTextField.clear();
        phoneTextField.clear();
        isAdminCheckBox.setSelected(false);
    }

    /**
     * Method to make admin a user
     */

    @FXML
    public void handleMakeAdminButton() throws SQLException {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            System.out.println("No se ha seleccionado ningún usuario.");
            return;
        }

        selectedUser.setIs_admin(true);
        userDAO.makeAdmin(selectedUser);

        tableView.refresh();
    }

    /**
     * Method to add a user
     * @throws SQLException
     */
    @FXML
    public void handleAddButton() throws SQLException {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        String hashedPassword = PasswordUtils.hashPassword(password);
        String name = nameTextField.getText();
        String surname = surnameTextField.getText();
        String phone = phoneTextField.getText();
        boolean isAdmin = isAdminCheckBox.isSelected();

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(hashedPassword);
        newUser.setName(name);
        newUser.setSurname(surname);
        newUser.setPhone(phone);
        newUser.setIs_admin(isAdmin);

        userDAO.insert(newUser);

        clearInputFields();
        tableView.getItems().add(newUser);
    }

    /**
     * Method for search one user with the button
     */
    @FXML
    public void handleSearchButton() {
        String searchText = searchField.getText(); // Obtener el texto de búsqueda desde un campo de texto

        // Realizar la búsqueda en la base de datos utilizando el UserDAO
        try {
            List<User> users = userDAO.search(searchText);
            tableView.getItems().clear(); // Limpiar los elementos existentes en la tabla
            tableView.getItems().addAll(users); // Agregar los usuarios encontrados a la tabla
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for bring a film to the booking view.
     */
    public void handleUserDoubleClick() {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/Hamm/Controller/ListBookingsAdmin.fxml"));
                Parent root = loader.load();

                ListBookingsControllerAdmin bookingsController = loader.getController();
                bookingsController.setConnection(connection);
                bookingsController.setUserId(selectedUser.getId_user());
                bookingsController.execute(); // Cargar los datos en la vista

                // Cerrar la ventana actual (opcional)
                Stage stage = (Stage) tableView.getScene().getWindow();
                stage.close();

                // Mostrar la nueva ventana
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

