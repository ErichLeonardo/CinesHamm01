package org.Hamm.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.Hamm.Model.DAO.FilmDAO;
import org.Hamm.Model.Domain.Film;

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
    }

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


    private void clearInputFields() {
        idTextField.clear();
        titleTextField.clear();
        genreTextField.clear();
        durationTextField.clear();
        synopsisTextField.clear();
    }


}
