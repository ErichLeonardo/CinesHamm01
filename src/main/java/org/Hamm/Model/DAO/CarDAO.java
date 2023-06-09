package org.Hamm.Model.DAO;

import org.Hamm.Model.Connections.ConnectionMySQL;
import org.Hamm.Model.Domain.Car;
import org.Hamm.Utils.Validator;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.Hamm.Logger.LoggerSingleton;


public class CarDAO implements DAO<Car> {

    private Connection connection;

    public CarDAO(Connection connection) {
        this.connection = connection;
    }

    public CarDAO() {

    }

    public void setConnection(Connection connection) {
    }

    // Consulta SQL para obtener todos los registros de car
    private final static String FIND_ALL = "SELECT * FROM car";

    // Consulta SQL para insertar un nuevo registro en la tabla Car
    private final static String INSERT = "INSERT INTO Car (tuition, brand, model, isRented) VALUES (?, ?, ?, ?)";

    // Consulta SQL para buscar un registro de Car por su id
    private final static String FIND_BY_ID = "SELECT * FROM Car WHERE id_car = ?";

    // Consulta SQL para actualizar un registro de Car
    private final static String UPDATE = "UPDATE Car SET brand = ?, model = ?, isRented = ? WHERE tuition = ?";

    // Consulta SQL para eliminar un registro de Car por su tuition
    private final static String DELETE = "DELETE FROM Car WHERE tuition = ?";

    // Consulta SQL para buscar un registro de Car por su tuition
    private final static String FIND_BY_TUITION = "SELECT * FROM Car WHERE tuition = ?";

    /**
     * Obtiene todos los registros de Car de la base de datos.
     *
     * @return una lista de objetos Car
     * @throws SQLException si ocurre algún error en la consulta SQL
     */
    @Override
    public List<Car> findAll() throws SQLException {
        List<Car> cars = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(FIND_ALL)) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int id_car = result.getInt("id_car");
                String tuition = result.getString("tuition");
                String brand = result.getString("brand");
                String model = result.getString("model");
                boolean isRented = result.getBoolean("isRented");
                Car car = new Car(id_car, tuition, brand, model, isRented);
                cars.add(car);
            }
        }
        return cars;
    }

    /**
     * Busca un registro de Car por su id.
     *
     * @param id el id del registro a buscar
     * @return un objeto Car si se encuentra, o null si no existe
     * @throws SQLException si ocurre algún error en la consulta SQL
     */
    @Override
    public Car findById(int id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                int id_car = result.getInt("id_car");
                String tuition = result.getString("tuition");
                String brand = result.getString("brand");
                String model = result.getString("model");
                boolean isRented = result.getBoolean("isRented");
                return new Car(id_car, tuition, brand, model, isRented);
            }
            LoggerSingleton logger = LoggerSingleton.getInstance();
            logger.logger("No se encontró ningún registro de Car con ID: " + id);
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Inserta un nuevo registro de Car en la base de datos.
     *
     * @param entity el objeto Car a insertar
     * @return el objeto Car insertado
     * @throws SQLException si ocurre algún error en la consulta SQL
     */
    @Override
    public Car insert(Car entity) throws SQLException {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }

        if (!Validator.validateTuition(entity.getTuition()) && !Validator.validateOldTuition(entity.getTuition())) {
            throw new IllegalArgumentException("Invalid tuition format");
        }

        try {
            // Check if the entity already exists in the database
            Car existingCar = findByTuition(entity.getTuition());
            if (existingCar != null) {
                // Update the existing entity in the database
                try (PreparedStatement stmt = connection.prepareStatement(UPDATE)) {
                    stmt.setString(2, entity.getTuition());
                    stmt.setString(3, entity.getBrand());
                    stmt.setString(4, entity.getModel());
                    stmt.setBoolean(5, entity.isRented());

                    stmt.executeUpdate();
                }
            } else {
                // Insert a new entity into the database
                try (PreparedStatement stmt = connection.prepareStatement(INSERT)) {
                    stmt.setString(1, entity.getTuition());
                    stmt.setString(2, entity.getBrand());
                    stmt.setString(3, entity.getModel());
                    stmt.setBoolean(4, entity.isRented());
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    /**
     * Elimina un registro de Car de la base de datos.
     *
     * @param entity el objeto Car a eliminar
     * @throws SQLException si ocurre algún error en la consulta SQL
     */
    @Override
    public void delete(Car entity) throws SQLException {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }

        try (PreparedStatement stmt = connection.prepareStatement(DELETE)) {
            stmt.setString(1, entity.getTuition());
            stmt.executeUpdate();
        }
    }

    /**
     * Actualiza un registro de Car en la base de datos.
     *
     * @param entity el objeto Car a actualizar
     * @return el objeto Car actualizado
     * @throws SQLException si ocurre algún error en la consulta SQL
     */
    @Override
    public Car update(Car entity) throws SQLException {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }

        // Check if the entity already exists in the database
        Car existingCar = findByTuition(entity.getTuition());
        if (existingCar == null) {
            throw new SQLException("Cannot update a non-existent entity");
        }

        // Check if the tuition has been changed
        if (!existingCar.getTuition().equals(entity.getTuition())) {
            // Check if there is another entity with the same tuition in the database
            Car carWithSameTuition = findByTuition(entity.getTuition());
            if (carWithSameTuition != null) {
                throw new SQLException("Another entity with the same tuition already exists");
            }
        }

        if (!Validator.validateTuition(entity.getTuition()) && !Validator.validateOldTuition(entity.getTuition())) {
            throw new IllegalArgumentException("Invalid tuition format");
        }

        try (PreparedStatement stmt = connection.prepareStatement(UPDATE)) {
            if (entity.isRented()) {
                stmt.setString(1, "cine");
                stmt.setString(2, "cine");
            } else {
                stmt.setString(1, entity.getBrand());
                stmt.setString(2, entity.getModel());
            }
            stmt.setBoolean(3, entity.isRented());
            stmt.setString(4, entity.getTuition());
            stmt.executeUpdate();
        }

        return entity;
    }

    /**
     * Busca un registro de Car por su tuition.
     *
     * @param tuition el tuition del registro a buscar
     * @return un objeto Car si se encuentra, o null si no existe
     * @throws SQLException si ocurre algún error en la consulta SQL
     */
    public Car findByTuition(String tuition) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_TUITION)) {
            statement.setString(1, tuition);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                int id_car = result.getInt("id_car");
                String brand = result.getString("brand");
                String model = result.getString("model");
                boolean isRented = result.getBoolean("isRented");
                return new Car(id_car,tuition, brand, model, isRented);
            }
            return null;
        }
    }

    public List<Car> search(String searchText) throws SQLException {
        List<Car> carList = new ArrayList<>();
        String SEARCH_QUERY = "SELECT id_car, tuition, brand, model, isRented FROM Car " +
                "WHERE id_car = ? OR tuition LIKE ? OR brand LIKE ? OR model LIKE ?";
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
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id_car = rs.getInt("id_car");
                    String tuition = rs.getString("tuition");
                    String brand = rs.getString("brand");
                    String model = rs.getString("model");
                    boolean isRented = rs.getBoolean("isRented");
                    Car car = new Car(id_car, tuition, brand, model, isRented);
                    carList.add(car);
                }
            }
        }
        return carList;
    }


    /**
     * Cierra la conexión a la base de datos.
     *
     * @throws Exception si ocurre algún error al cerrar la conexión
     */
    @Override
    public void close() throws Exception {
       // if (connection != null) {
       //     connection.close();
       // }
    }



}
