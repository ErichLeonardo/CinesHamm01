package org.Hamm.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import org.Hamm.Model.DAO.FilmDAO;
import org.Hamm.Model.DAO.ReservationDAO;
import org.Hamm.Model.Domain.Film;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EuroController {
    @FXML
    private Label totalLabel;

    @FXML
    private Label totalReservationsLabel;

    @FXML
    private Label reservationsByFilmLabel;

    @FXML
    private ChoiceBox<Film> filmChoiceBox;

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void initialize() {
        try {
            ReservationDAO reservationDAO = new ReservationDAO();
            FilmDAO filmDAO = new FilmDAO();

            // Obtener el número total de reservas
            int total = reservationDAO.countReservations();
            double totalEuros = total * 3;
            totalLabel.setText("Total: " + totalEuros + " euros");
            totalReservationsLabel.setText("Total reservations: " + total);

            // Obtener la lista de películas desde la base de datos
            List<Film> films = filmDAO.findAll();

            // Eliminar duplicados usando un HashSet
            Set<Film> uniqueFilms = new HashSet<>(films);

            // Limpiar el ChoiceBox antes de agregar los elementos
            filmChoiceBox.getItems().clear();

            // Agregar los títulos de las películas al ChoiceBox
            filmChoiceBox.getItems().addAll(uniqueFilms);

            // Personalizar la forma en que se muestra cada elemento en el ChoiceBox
            filmChoiceBox.setConverter(new StringConverter<Film>() {
                @Override
                public String toString(Film film) {
                    return film.getTitle();
                }

                @Override
                public Film fromString(String string) {
                    // No se necesita implementar en este caso
                    return null;
                }
            });

            // Manejar el evento de selección de una película en el ChoiceBox
            filmChoiceBox.setOnAction(event -> {
                Film selectedFilm = filmChoiceBox.getValue();
                if (selectedFilm != null) {
                    int reservationsBySelectedFilm = 0;
                    try {
                        reservationsBySelectedFilm = reservationDAO.countReservationsByFilm(selectedFilm);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    reservationsByFilmLabel.setText(String.valueOf(reservationsBySelectedFilm));
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
