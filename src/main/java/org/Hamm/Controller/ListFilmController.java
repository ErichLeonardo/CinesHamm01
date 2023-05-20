package org.Hamm.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Region;
import org.Hamm.Model.DAO.FilmDAO;
import org.Hamm.Model.Domain.Film;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ButtonType;

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

    private FilmDAO filmDAO;


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
}

