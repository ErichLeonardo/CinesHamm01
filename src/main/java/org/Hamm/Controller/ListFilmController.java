package org.Hamm.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.Hamm.Model.DAO.FilmDAO;
import org.Hamm.Model.Domain.Film;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

public class ListFilmController {

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @FXML
    private TableView<Film> tableView;
    @FXML
    private TableColumn<Film, Integer> idColumn;
    @FXML
    private TableColumn<Film, String> titleColumn;
    @FXML
    private TableColumn<Film, String> genreColumn;
    @FXML
    private TableColumn<Film, Time> durationColumn;
    @FXML
    private TableColumn<Film, String> synopsisColumn;
    @FXML
    private TextField searchField;

    private FilmDAO filmDAO;


    /**
     * See the film list
     */
    public void execute() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id_film"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        synopsisColumn.setCellValueFactory(new PropertyValueFactory<>("synopsis"));

        filmDAO = new FilmDAO(); // Pasar la conexión al constructor de FilmDAO

        try {
            List<Film> films = filmDAO.findAll();
            tableView.getItems().addAll(films);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                List<Film> films = filmDAO.search(newValue);
                tableView.getItems().clear(); // Limpiar los elementos existentes en la tabla
                tableView.getItems().addAll(films); // Agregar las películas encontradas a la tabla
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
     * Method for view better the synopsis
     */
    @FXML
    public void showSynopsisDialog() {
        Film selectedFilm = tableView.getSelectionModel().getSelectedItem();
        if (selectedFilm != null) {
            String synopsis = selectedFilm.getSynopsis();

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Sinopsis");
            dialog.setHeaderText(null);

            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.setContentText(synopsis);
            dialogPane.setMinHeight(Region.USE_PREF_SIZE);

            ButtonType okButton = new ButtonType("OK");
            dialogPane.getButtonTypes().add(okButton);

            dialog.showAndWait();
        }
    }

    @FXML
    public void handleSearchButton() {
        String searchTerm = searchField.getText();

        try {
            List<Film> films = filmDAO.search(searchTerm);
            tableView.getItems().clear(); // Limpiar los elementos existentes en la tabla
            tableView.getItems().addAll(films); // Agregar las películas encontradas a la tabla
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleUserDoubleClick() {
        Film selectedFilm = tableView.getSelectionModel().getSelectedItem();
        if (selectedFilm != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/Hamm/Controller/ListBookings.fxml"));
                Parent root = loader.load();

                ListBookingsController bookingsController = loader.getController();
                bookingsController.setConnection(connection);
                bookingsController.setFilmId(selectedFilm.getId_film());
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

