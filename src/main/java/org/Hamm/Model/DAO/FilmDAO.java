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
// Consulta SQL para recuperar todos los registros de películas de la base de datos

    private static final String FIND_BY_ID = "SELECT * FROM Film WHERE id_film = ?";
// Consulta SQL para buscar una película por su ID en la base de datos

    private static final String FIND_BY_TITLE = "SELECT * FROM Film WHERE title = ?";
// Consulta SQL para buscar una película por su título en la base de datos

    private static final String INSERT = "INSERT INTO Film (id_film, title, genre, duration, synopsis) VALUES (?, ?, ?, ?, ?)";
// Consulta SQL para insertar un nuevo registro de película en la base de datos

    private static final String DELETE = "DELETE FROM Film WHERE id_film = ?";
// Consulta SQL para eliminar un registro de película de la base de datos

    private static final String UPDATE = "UPDATE Film SET title = ?, genre = ?, duration = ?, synopsis = ? WHERE id_film = ?";
// Consulta SQL para actualizar un registro de película en la base de datos

    private Connection connection;

    public FilmDAO(Connection connection) {
        this.connection = connection;
    }

    public FilmDAO() {
        this.connection = ConnectionMySQL.getConnect();
    }

    /**
     * Recupera todos los registros de películas de la base de datos.
     *
     * @return una lista de objetos Film que representa todas las películas
     * @throws SQLException si ocurre algún error en la consulta SQL
     */
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

    /**
     * Busca una película por su ID en la base de datos.
     *
     * @param id el ID de la película a buscar
     * @return el objeto Film correspondiente al ID proporcionado, o null si no se encuentra
     * @throws SQLException si ocurre algún error en la consulta SQL
     */
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

    /**
     * Inserta un nuevo registro de película en la base de datos.
     *
     * @param entity el objeto Film a insertar
     * @return el objeto Film insertado
     * @throws SQLException si ocurre algún error en la consulta SQL
     */
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

    /**
     * Elimina un registro de película de la base de datos.
     *
     * @param entity el objeto Film a eliminar
     * @throws SQLException si ocurre algún error en la consulta SQL
     */
    @Override
    public void delete(Film entity) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE)) {
            stmt.setInt(1, entity.getId_film());
            stmt.executeUpdate();
        }
    }

    /**
     * Actualiza un registro de película en la base de datos.
     *
     * @param entity el objeto Film con los datos actualizados
     * @return el objeto Film actualizado
     * @throws SQLException si ocurre algún error en la consulta SQL
     */
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

    /**
     * Busca una película por su título en la base de datos.
     *
     * @param title el título de la película a buscar
     * @return el objeto Film correspondiente al título proporcionado, o null si no se encuentra
     * @throws SQLException si ocurre algún error en la consulta SQL
     */
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

    /**
     * Performs a search for Film records in the database based on a search text.
     *
     * @param searchText the search text
     * @return a list of Film objects that match the search text
     * @throws SQLException if any SQL error occurs
     */
    public List<Film> search(String searchText) throws SQLException {
        List<Film> filmList = new ArrayList<>();
        String SEARCH_QUERY = "SELECT * FROM Film WHERE id_film = ? OR title LIKE ? OR genre LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(SEARCH_QUERY)) {
            int searchId;
            try {
                searchId = Integer.parseInt(searchText);
            } catch (NumberFormatException e) {
                searchId = -1; // Valor inválido para el ID
            }
            ps.setInt(1, searchId);
            String searchPattern = "%" + searchText + "%";
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern); // Establecer el valor para el tercer parámetro
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Film film = new Film(
                            rs.getInt("id_film"),
                            rs.getString("title"),
                            rs.getString("genre"),
                            rs.getTime("duration"),
                            rs.getString("synopsis")
                    );
                    filmList.add(film);
                }
            }
        }
        return filmList;
    }



    /**
     * Cierra la conexión a la base de datos.
     *
     * @throws Exception si ocurre algún error al cerrar la conexión
     */
    @Override
    public void close() throws Exception {
       // if (connection != null) {
        //    connection.close();
       // }
    }
}
