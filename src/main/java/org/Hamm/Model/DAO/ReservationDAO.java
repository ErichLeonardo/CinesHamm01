package org.Hamm.Model.DAO;

import org.Hamm.Model.Connections.ConnectionMySQL;
import org.Hamm.Model.Domain.Reservation;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO implements DAO<Reservation> {

    private Connection connection;

    private UserDAO userDAO;
    private CarDAO carDAO;
    private FilmDAO filmDAO;

    public ReservationDAO(Connection connection) {
        this.connection = connection;
        userDAO = new UserDAO(connection);
        carDAO = new CarDAO(connection);
        filmDAO = new FilmDAO(connection);
    }

    public ReservationDAO() {
        this.connection = ConnectionMySQL.getConnect();
    }

    private static final String FIND_ALL = "SELECT * FROM Reservation";
    private static final String FIND_BY_ID = "SELECT * FROM Reservation WHERE id_reservation=?";
    private static final String FIND_BY_ID_FILM = "SELECT * FROM Reservation JOIN Film on Reservation.id_film=Film.id.film WHERE ID_FILM=?";
    private final static String INSERT_RESERVATION = "INSERT INTO Reservation(id_user, tuition_car, id_film, day, hour, location) VALUES(?,?,?,?,?,?)";
    private final static String FIND_DUPLICATE_RESERVATIONS = "SELECT * FROM Reservation WHERE id_user=? AND day=? AND hour=?";
    private final static String FIND_CONFLICTING_RESERVATIONS = "SELECT * FROM Reservation WHERE day=? AND hour=? AND location=?";
    private final static String DELETE_RESERVATION = "DELETE FROM Reservation WHERE id_reservation=?";
    private final static String CHECK_RESERVATION_EXISTENCE = "SELECT COUNT(*) FROM Reservation WHERE id_reservation = ?";
    private final static String UPDATE_RESERVATION = "UPDATE Reservation SET id_user = ?, id_car = ?, id_film = ?, day = ?, hour = ?, location = ? WHERE id_reservation = ?";
    private final static String COUNT_RESERVATIONS_BY_DATE_TIME_AND_FILM = "SELECT COUNT(*) FROM reservation WHERE id_film = ? AND day = ? AND hour = ?";


    @Override
    public List<Reservation> findAll() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(FIND_ALL)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId_reservation(rs.getInt("id_reservation"));
                reservation.setUser(userDAO.findById(rs.getInt("id_user")));
                reservation.setCar(carDAO.findById(rs.getInt("id_car")));
                reservation.setFilm(filmDAO.findById(rs.getInt("id_film")));
                reservation.setDate(rs.getDate("day").toLocalDate());
                reservation.setTime(rs.getTime("hour").toLocalTime());
                reservation.setLocation(rs.getString("location"));
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    @Override
    public Reservation findById(int id) throws SQLException {
        Reservation reservation = null;
        try (PreparedStatement ps = connection.prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                reservation = new Reservation();
                reservation.setId_reservation(rs.getInt("id_reservation"));
                reservation.setUser(userDAO.findById(rs.getInt("id_user")));
                reservation.setCar(carDAO.findById(rs.getInt("id_car")));
                reservation.setFilm(filmDAO.findById(rs.getInt("id_film")));
                reservation.setDate(rs.getDate("day").toLocalDate());
                reservation.setTime(rs.getTime("hour").toLocalTime());
                reservation.setLocation(rs.getString("location"));
            }
        }
        return reservation;
    }

    @Override
    public Reservation insert(Reservation entity) throws SQLException {
        try (PreparedStatement psCheck = connection.prepareStatement(FIND_DUPLICATE_RESERVATIONS)) {
            psCheck.setInt(1, entity.getUser().getId_user());
            psCheck.setDate(2, Date.valueOf(entity.getDate()));
            psCheck.setTime(3, Time.valueOf(entity.getTime()));
            ResultSet rsCheck = psCheck.executeQuery();
            if (rsCheck.next()) {
                int count = rsCheck.getInt(1);
                if (count > 0) {
                    throw new SQLException("Ya existe una reserva con el mismo id, la misma hora y el mismo día hecha por el mismo usuario.");
                }
            }
        }
        try (PreparedStatement psLocation = connection.prepareStatement(FIND_CONFLICTING_RESERVATIONS)) {
            psLocation.setDate(1, Date.valueOf(entity.getDate()));
            psLocation.setTime(2, Time.valueOf(entity.getTime()));
            psLocation.setString(3, entity.getLocation());
            ResultSet rsLocation = psLocation.executeQuery();
            if (rsLocation.next()) {
                int count = rsLocation.getInt(1);
                if (count > 0) {
                    throw new SQLException("Ya existe una reserva para la misma hora y día en esta ubicación.");
                }
            }
        }
        try (PreparedStatement psInsert = connection.prepareStatement(INSERT_RESERVATION)) {
            psInsert.setInt(1, entity.getUser().getId_user());
            psInsert.setString(2, entity.getCar().getTuition());
            psInsert.setInt(3, entity.getFilm().getId_film());
            psInsert.setDate(4, Date.valueOf(entity.getDate()));
            psInsert.setTime(5, Time.valueOf(entity.getTime()));
            psInsert.setString(6, entity.getLocation());
            psInsert.executeUpdate();
        }

        return entity;
    }



    @Override
    public void delete(Reservation entity) throws SQLException {
        try (Connection connection = ConnectionMySQL.getConnect();
             PreparedStatement ps = connection.prepareStatement(DELETE_RESERVATION)) {
            ps.setInt(1, entity.getId_reservation());
            ps.executeUpdate();
        }
    }


    @Override
    public Reservation update(Reservation entity) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(CHECK_RESERVATION_EXISTENCE)) {
            ps.setDate(1, Date.valueOf(entity.getDate()));
            ps.setTime(2, Time.valueOf(entity.getTime()));
            ps.setString(3, entity.getLocation());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                throw new SQLException("La localización ya está ocupada para esta hora y día");
            }
        }
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_RESERVATION)) {
            ps.setInt(1, entity.getUser().getId_user());
            ps.setInt(2, entity.getCar().getId_car());
            ps.setInt(3, entity.getFilm().getId_film());
            ps.setDate(4, Date.valueOf(entity.getDate()));
            ps.setTime(5, Time.valueOf(entity.getTime()));
            ps.setString(6, entity.getLocation());
            ps.setInt(7, entity.getId_reservation());
            ps.executeUpdate();
        }

        return entity;
    }

    public void checkMaxReservations(LocalDateTime dateTime, int filmId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(COUNT_RESERVATIONS_BY_DATE_TIME_AND_FILM)) {
            ps.setTimestamp(1, Timestamp.valueOf(dateTime));
            ps.setInt(2, filmId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                if (count >= 20) {
                    throw new SQLException("No se pueden hacer más de 20 reservas para esta película en la misma hora y día");
                }
            }
        }
    }

    public List<Reservation> findByFilmId(int filmId) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(FIND_BY_ID_FILM)) {
            ps.setInt(1, filmId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId_reservation(rs.getInt("id_reservation"));
                reservation.setUser(userDAO.findById(rs.getInt("id_user")));
                reservation.setCar(carDAO.findById(rs.getInt("id_car")));
                reservation.setFilm(filmDAO.findById(rs.getInt("id_film")));
                reservation.setDate(rs.getDate("day").toLocalDate());
                reservation.setTime(rs.getTime("hour").toLocalTime());
                reservation.setLocation(rs.getString("location"));
                reservations.add(reservation);
            }
        }
        return reservations;
    }


    @Override
    public void close() throws Exception {

    }
}
