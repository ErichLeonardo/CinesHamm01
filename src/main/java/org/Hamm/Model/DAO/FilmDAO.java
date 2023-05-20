package org.Hamm.Model.DAO;

import org.Hamm.Model.Connections.ConnectionMySQL;
import org.Hamm.Model.Domain.Film;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FilmDAO implements DAO<Film> {

    private static final String FIND_ALL = "SELECT * FROM Film";
    private static final String FIND_BY_ID = "SELECT * FROM Film WHERE id_film = ?";
    private static final String FIND_BY_TITLE = "SELECT * FROM Film WHERE title = ?";
    private static final String INSERT = "INSERT INTO Film (id_film, title, genre, duration, synopsis) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE = "DELETE FROM Film WHERE id_film = ?";
    private static final String UPDATE = "UPDATE Film SET title = ?, genre = ?, duration = ?, synopsis = ? WHERE id_film = ?";

    private Connection connection;

    public FilmDAO(Connection connection) {
        this.connection = connection;
    }

    public FilmDAO() {
        this.connection = ConnectionMySQL.getConnect();
    }


    @Override
    public List<Film> findAll() throws SQLException {
        List<Film> films = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(FIND_ALL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Film film = new Film(
                        rs.getInt("id_film"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getTime("duration"),
                        rs.getString("synopsis")
                );
                films.add(film);
            }
        }
        return films;
    }

    @Override
    public Film findById(int id) throws SQLException {
        Film film = null;
        try (PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                film = new Film(
                        rs.getInt("id_film"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getTime("duration"),
                        rs.getString("synopsis")
                );
            }
        }
        return film;
    }

    @Override
    public Film insert(Film entity) throws SQLException {
        Film existingFilm = findById(entity.getId_film());
        if (existingFilm != null) {
            throw new SQLException("A film with the same ID already exists");
        }
        existingFilm = findByTitle(entity.getTitle());
        if (existingFilm != null) {
            throw new SQLException("A film with the same title already exists");
        }
        try (PreparedStatement stmt = connection.prepareStatement(INSERT)) {
            stmt.setInt(1, entity.getId_film());
            stmt.setString(2, entity.getTitle());
            stmt.setString(3, entity.getGenre());
            stmt.setTime(4, entity.getDuration());
            stmt.setString(5, entity.getSynopsis());
            stmt.executeUpdate();
        }
        return entity;
    }

    @Override
    public void delete(Film entity) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE)) {
            stmt.setInt(1, entity.getId_film());
            stmt.executeUpdate();
        }
    }

    @Override
    public Film update(Film entity) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE)) {
            stmt.setString(1, entity.getTitle());
            stmt.setString(2, entity.getGenre());
            stmt.setTime(3, entity.getDuration());
            stmt.setString(4, entity.getSynopsis());
            stmt.setInt(5, entity.getId_film());
            stmt.executeUpdate();
        }
        return entity;
    }

    private Film findByTitle(String title) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(FIND_BY_TITLE)) {
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Film(
                        rs.getInt("id_film"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getTime("duration"),
                        rs.getString("synopsis")
                );
            }
            return null;
        }
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}
