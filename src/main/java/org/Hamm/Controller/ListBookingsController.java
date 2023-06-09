package org.Hamm.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.Hamm.Model.Connections.ConnectionMySQL;
import org.Hamm.Model.DAO.CarDAO;
import org.Hamm.Model.DAO.FilmDAO;
import org.Hamm.Model.DAO.ReservationDAO;
import org.Hamm.Model.DAO.UserDAO;
import org.Hamm.Model.Domain.Car;
import org.Hamm.Model.Domain.Film;
import org.Hamm.Model.Domain.Reservation;
import org.Hamm.Model.Domain.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ListBookingsController {
    private Connection connection;
    private UserDAO userDAO;
    private CarDAO carDAO;
    private FilmDAO filmDAO;
    private ReservationDAO reservationDAO;

    public void setConnection(Connection connection) {
        this.connection = connection;
        this.userDAO = new UserDAO(connection);
        this.carDAO = new CarDAO(connection);
        this.filmDAO = new FilmDAO(connection);
        this.reservationDAO = new ReservationDAO(connection);
    }

    @FXML
    private TableView<Reservation> tableView;
    @FXML
    private TableColumn<Reservation, Integer> idColumn;
    @FXML
    private TableColumn<Reservation, String> userColumn;
    @FXML
    private TableColumn<Reservation, String> carColumn;
    @FXML
    private TableColumn<Reservation, String> filmColumn;
    @FXML
    private TableColumn<Reservation, String> dateColumn;
    @FXML
    private TableColumn<Reservation, String> timeColumn;
    @FXML
    private TableColumn<Reservation, String> locationColumn;
    @FXML
    private Label labelError;
    @FXML
    private TextField idUserField;
    @FXML
    public TextField tuitionCarField;
    @FXML
    private TextField idFilmField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> timeComboBox;
    @FXML
    private ComboBox<String> locationComboBox;

    private void showError(String message) {
        labelError.setText(message);
    }

    private int userId;

    public void setUserId(int userId) {
        this.userId = userId;
        idUserField.setText(String.valueOf(userId));
    }

    public void setFilmId(int filmId){
        idFilmField.setText(String.valueOf(filmId));
    }

    /**
     * List of reservations
     */
    public void execute() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id_reservation"));
        userColumn.setCellValueFactory(data -> {
            Reservation reservation = data.getValue();
            int idUser = reservation.getUser().getId_user();
            return new SimpleStringProperty(String.valueOf(idUser));
        });

        carColumn.setCellValueFactory(data -> {
            Reservation reservation = data.getValue();
            String tuition = reservation.getCar().getTuition();
            return new SimpleStringProperty(tuition);
        });

        filmColumn.setCellValueFactory(data -> {
            Reservation reservation = data.getValue();
            int idFilm = reservation.getFilm().getId_film();
            return new SimpleStringProperty(String.valueOf(idFilm));
        });
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        try {
            Connection connection = ConnectionMySQL.getConnect();
            reservationDAO = new ReservationDAO(connection); // Inicializar reservationDAO aquí
            List<Reservation> reservations = reservationDAO.findAll();
            tableView.getItems().addAll(reservations);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a new reservation
     */
    @FXML
    public void handleAddBookingButton() {
        int idUser;
        String tuition;
        int idFilm;
        LocalDate date;
        String time;
        String location;

        idUser = Integer.parseInt(idUserField.getText());
        tuition = tuitionCarField.getText();
        idFilm = Integer.parseInt(idFilmField.getText());
        date = datePicker.getValue();
        time = timeComboBox.getValue();
        location = locationComboBox.getValue();

        User user;
        Car car;
        Film film;

        try {
            // Obtener instancias de User, Car y Film
            Connection connection = ConnectionMySQL.getConnect();
            this.userDAO = new UserDAO(connection);
            this.carDAO = new CarDAO(connection);
            this.filmDAO = new FilmDAO(connection);
            this.reservationDAO = new ReservationDAO(connection);
            user = userDAO.findById(idUser);
            car = carDAO.findByTuition(tuition);
            film = filmDAO.findById(idFilm);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setCar(car);
        reservation.setFilm(film);
        reservation.setDate(date);
        reservation.setTime(LocalTime.parse(time));
        reservation.setLocation(location);

        try (Connection connection = ConnectionMySQL.getConnect()) {
            ReservationDAO reservationDAO = new ReservationDAO(connection);
            reservationDAO.insert(reservation);
            tableView.getItems().add(reservation);

            idUserField.clear();
            tuitionCarField.clear();
            idFilmField.clear();

            labelError.setText("Reservation inserted successfully.");
        } catch (SQLException e) {
            String errorMessage = e.getMessage();
            showError(errorMessage);
        }
    }


    /**
     * Button for see the film list
     */
    @FXML
    public void handleMoviesButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/Hamm/Controller/ListFilm.fxml"));
            Parent filmListRoot = fxmlLoader.load();

            Stage filmListStage = new Stage();
            filmListStage.setTitle("Lista de películas");
            filmListStage.setScene(new Scene(filmListRoot, 765, 380));

            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.close();
            filmListStage.show();

            ListFilmController filmListController = fxmlLoader.getController();
            filmListController.setConnection(connection); // Pasar la conexión al controlador de la lista de películas
            filmListController.setUserId(userId);
            filmListController.execute(); // Ejecutar el método para cargar los datos en la tabla
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

