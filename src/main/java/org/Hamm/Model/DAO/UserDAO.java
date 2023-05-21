package org.Hamm.Model.DAO;

import org.Hamm.Model.Connections.ConnectionMySQL;
import org.Hamm.Model.Domain.User;
import org.Hamm.Utils.PasswordUtils;
import org.Hamm.Utils.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements DAO<User> {

    // Consulta para recuperar todos los usuarios de la tabla "User" con sus respectivos campos
    private static final String FIND_ALL = "SELECT id_user, email, name, surname, phone, isAdmin FROM User";

    // Consulta para recuperar un usuario de la tabla "User" que coincida con el ID proporcionado
    private static final String FIND_BY_ID = "SELECT id_user, email, name, surname, phone, isAdmin FROM User WHERE id_user = ?";

    // Consulta para recuperar un usuario de la tabla "User" que coincida con el correo electrónico proporcionado
    private static final String FIND_BY_EMAIL = "SELECT id_user, email, password, name, surname, phone, isAdmin FROM User WHERE email = ?";

    // Consulta para insertar un nuevo usuario en la tabla "User" con los valores proporcionados
    private static final String INSERT = "INSERT INTO User (email, password, name, surname, phone, isAdmin) VALUES (?, ?, ?, ?, ?, ?)";

    // Consulta para actualizar los campos de un usuario existente en la tabla "User"
    private static final String UPDATE = "UPDATE User SET email = ?, password=?, name = ?, surname = ?, phone = ? WHERE id_user = ?";

    // Consulta para eliminar un usuario de la tabla "User" según el ID proporcionado
    private static final String DELETE = "DELETE FROM User WHERE id_user = ?";

    // Consulta para actualizar el estado de administrador de un usuario en la tabla "User"
    private static final String MAKE_ADMIN = "UPDATE User SET isAdmin = ? WHERE id_user = ?";


    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public UserDAO() {
        this.connection = ConnectionMySQL.getConnect();
    }


    /**
     * Busca un usuario por su correo electrónico en la base de datos.
     *
     * @param email el correo electrónico del usuario a buscar
     * @return el usuario encontrado o null si no se encontró ningún usuario con el correo electrónico dado
     * @throws SQLException si ocurre algún error en la consulta SQL
     */
    public User findByEmail(String email) throws SQLException {
        User user = null;
        try (PreparedStatement ps = connection.prepareStatement(FIND_BY_EMAIL)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId_user(rs.getInt("id_user"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setName(rs.getString("name"));
                    user.setSurname(rs.getString("surname"));
                    user.setPhone(rs.getString("phone"));
                    user.setIs_admin(rs.getBoolean("isAdmin"));
                }
            }
        }
        return user;
    }

    /**
     * Obtiene una lista de todos los usuarios almacenados en la base de datos.
     *
     * @return la lista de usuarios
     * @throws SQLException si ocurre algún error en la consulta SQL
     */
    @Override
    public List<User> findAll() throws SQLException {
        List<User> userList = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId_user(rs.getInt("id_user"));
                user.setEmail(rs.getString("email"));
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));
                user.setPhone(rs.getString("phone"));
                user.setIs_admin(rs.getBoolean("isAdmin"));
                userList.add(user);
            }
        }
        return userList;
    }

    /**
     * Busca un usuario por su ID en la base de datos.
     *
     * @param id el ID del usuario a buscar
     * @return el usuario encontrado o null si no se encontró ningún usuario con el ID dado
     * @throws SQLException si ocurre algún error en la consulta SQL
     */
    @Override
    public User findById(int id) throws SQLException {
        User user = null;
        try (PreparedStatement ps = connection.prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId_user(rs.getInt("id_user"));
                    user.setEmail(rs.getString("email"));
                    user.setName(rs.getString("name"));
                    user.setSurname(rs.getString("surname"));
                    user.setPhone(rs.getString("phone"));
                    user.setIs_admin(rs.getBoolean("isAdmin"));
                }
            }
        }
        return user;
    }

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * @param entity el usuario a insertar
     * @return el usuario insertado con su ID generado, o null si no se pudo insertar el usuario
     * @throws SQLException si ocurre algún error en la consulta SQL
     */
    @Override
    public User insert(User entity) throws SQLException {
        User user = null;
        String SELECT_BY_EMAIL = "SELECT * FROM User WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_EMAIL)) {
            ps.setString(1, entity.getEmail());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    throw new SQLException("Ya existe un usuario con el correo electrónico proporcionado.");
                }

                if (!Validator.validateEmail(entity.getEmail())) {
                    throw new IllegalArgumentException("Invalid email format");
                }

                if (!Validator.validatePhone(entity.getPhone())) {
                    throw new IllegalArgumentException("Invalid phone format");
                }

            }
        }
        try (PreparedStatement ps = connection.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getEmail());
            ps.setString(2, PasswordUtils.hashPassword(entity.getPassword()));
            ps.setString(3, entity.getName());
            ps.setString(4, entity.getSurname());
            ps.setString(5, entity.getPhone());
            ps.setBoolean(6, false);
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user = new User();
                    user.setId_user(generatedKeys.getInt(1));
                    user.setEmail(entity.getEmail());
                    user.setPassword(entity.getPassword());
                    user.setName(entity.getName());
                    user.setSurname(entity.getSurname());
                    user.setPhone(entity.getPhone());
                    user.setIs_admin(false);
                }
            }
        }
        return user;
    }

    /**
     * Elimina un usuario de la base de datos.
     *
     * @param entity el usuario a eliminar
     */
    @Override
    public void delete(User entity) {
        if (entity.isIs_admin()) {
            System.out.println("El usuario es un administrador y no puede ser borrado.");
            return;
        }
        try (PreparedStatement ps = connection.prepareStatement(DELETE)) {
            ps.setInt(1, entity.getId_user());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Actualiza los datos de un usuario en la base de datos.
     *
     * @param entity el usuario con los nuevos datos
     * @return el usuario actualizado o null si no se pudo actualizar el usuario
     * @throws SQLException si ocurre algún error en la consulta SQL
     */
    @Override
    public User update(User entity) throws SQLException {
        User user = null;
        if (entity.isIs_admin()) {
            throw new IllegalArgumentException("No se puede modificar el estado de administrador");
        }
        if (!Validator.validateEmail(entity.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (!Validator.validatePhone(entity.getPhone())) {
            throw new IllegalArgumentException("Invalid phone format");
        }

        try (PreparedStatement ps = connection.prepareStatement(UPDATE)) {
            ps.setString(1, entity.getEmail());
            ps.setString(2, PasswordUtils.hashPassword(entity.getPassword()));
            ps.setString(3, entity.getName());
            ps.setString(4, entity.getSurname());
            ps.setString(5, entity.getPhone());
            ps.setInt(6, entity.getId_user());
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                user = findById(entity.getId_user());
            }
        }
        return user;
    }

    /**
     * Asigna el estado de administrador a un usuario en la base de datos.
     *
     * @param entity el usuario al que se desea asignar el estado de administrador
     * @return el usuario con el estado de administrador actualizado
     * @throws SQLException si ocurre algún error en la consulta SQL
     */
    public User makeAdmin(User entity) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(MAKE_ADMIN)) {
            ps.setBoolean(1, true);
            ps.setInt(2, entity.getId_user());
            ps.executeUpdate();
        }
        return entity;
    }

    /**
     * Verifica si una contraseña sin cifrar coincide con una contraseña cifrada.
     *
     * @param plainPassword   la contraseña sin cifrar
     * @param hashedPassword  la contraseña cifrada
     * @return true si las contraseñas coinciden, false en caso contrario
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return PasswordUtils.verifyPassword(plainPassword, hashedPassword);
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

}
