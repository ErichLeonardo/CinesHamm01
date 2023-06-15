package org.Hamm.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.Hamm.Model.DAO.FilmDAO;
import org.Hamm.Model.Domain.Film;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

public class ListFilmControllerAdmin {
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
    private TextField idTextField;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField genreTextField;
    @FXML
    private TextField durationTextField;
    @FXML
    private TextField synopsisTextField;
    @FXML
    private TextField searchField;

    private FilmDAO filmDAO;

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
     * Method for delete a film
     * @throws SQLException
     */
    @FXML
    public void handleDeleteButton() throws SQLException {
        Film selectedFilm = tableView.getSelectionModel().getSelectedItem();
        if (selectedFilm == null) {
            System.out.println("No se ha seleccionado ninguna película.");
            return;
        }

        filmDAO.delete(selectedFilm);
        tableView.getItems().remove(selectedFilm);
        System.out.println("Película eliminada: " + selectedFilm);
    }

    /**
     * Method for add a film
     * @throws SQLException
     */
    @FXML
    public void handleAddButton() throws SQLException {
        int ID = Integer.parseInt(idTextField.getText());
        String title = titleTextField.getText();
        String genre = genreTextField.getText();
        Time duration = Time.valueOf(durationTextField.getText());
        String synopsis = synopsisTextField.getText();

        Film newFilm = new Film(ID, title, genre, duration, synopsis);

        filmDAO.insert(newFilm);

        clearInputFields();
        tableView.getItems().add(newFilm);
    }

    /**
     * Method for update a film
     * @throws SQLException
     */
    @FXML
    public void handleUpdateButton() throws SQLException {
        Film selectedFilm = tableView.getSelectionModel().getSelectedItem();
        if (selectedFilm != null) {
            int ID = selectedFilm.getId_film();
            String title = titleTextField.getText();
            String genre = genreTextField.getText();
            Time duration = Time.valueOf(durationTextField.getText());
            String synopsis = synopsisTextField.getText();

            selectedFilm.setTitle(title);
            selectedFilm.setGenre(genre);
            selectedFilm.setDuration(duration);
            selectedFilm.setSynopsis(synopsis);

            filmDAO.update(selectedFilm);

            clearInputFields();
            tableView.refresh();
        }
    }

    @FXML
    public void handleKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.C) {
            Film selectedFilm = tableView.getSelectionModel().getSelectedItem();
            if (selectedFilm != null) {
                idTextField.setText(String.valueOf(selectedFilm.getId_film()));
                titleTextField.setText(selectedFilm.getTitle());
                genreTextField.setText(selectedFilm.getGenre());
                durationTextField.setText(String.valueOf(selectedFilm.getDuration()));
                synopsisTextField.setText(selectedFilm.getSynopsis());
            }
        }
    }


    /**
     * Method for clean the imput fields
     */
    private void clearInputFields() {
        idTextField.clear();
        titleTextField.clear();
        genreTextField.clear();
        durationTextField.clear();
        synopsisTextField.clear();
    }

    @FXML
    public void showSynopsisDialog() {
        Film selectedFilm = tableView.getSelectionModel().getSelectedItem();
        if (selectedFilm != null) {
            String synopsis = selectedFilm.getSynopsis();
            String title = selectedFilm.getTitle();

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Sinopsis");
            dialog.setHeaderText(null);

            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.setMinHeight(Region.USE_PREF_SIZE);


            String imagePath = "/org/Hamm/Controller/" + title + ".jpg";
            Image cover = new Image(getClass().getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(cover);
            imageView.setFitWidth(150);
            imageView.setPreserveRatio(true);
            dialogPane.setGraphic(imageView);

            dialogPane.setContentText(synopsis);

            ButtonType okButton = new ButtonType("OK");
            dialogPane.getButtonTypes().add(okButton);

            dialog.showAndWait();
        }
    }

    @FXML
    private void showAllReviews() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/Hamm/Controller/ListReviewsAdmin.fxml"));
            Parent reviewListRoot = fxmlLoader.load();

            Stage reviewListStage = new Stage();
            reviewListStage.setTitle("List of Reviews");
            reviewListStage.setScene(new Scene(reviewListRoot, 970, 403));

            ListReviewAdmin reviewsAdmin = fxmlLoader.getController();
            reviewsAdmin.setConnection(connection);
            reviewsAdmin.execute();

            reviewListStage.show();

            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.close();

        } catch (IOException e) {
            e.printStackTrace();
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/Hamm/Controller/ListBookingsAdmin.fxml"));
                Parent root = loader.load();

                ListBookingsControllerAdmin bookingsController = loader.getController();
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
