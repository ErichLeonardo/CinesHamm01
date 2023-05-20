package org.Hamm.Model.DAO;

import org.Hamm.Model.Domain.Car;
import org.Hamm.Utils.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarDAO implements DAO<Car> {

    private Connection connection;

    public CarDAO(Connection connection) {
        this.connection = connection;
    }

    public void setConnection(Connection connection) {
    }

    private final static String FIND_ALL = "SELECT * FROM car";

    private final static String INSERT = "INSERT INTO Car (tuition, brand, model, isRented) VALUES (?, ?, ?, ?)";

    private final static String FIND_BY_ID = "SELECT * FROM Car WHERE id_car = ?";

    private final static String UPDATE = "UPDATE Car SET brand = ?, model = ?, isRented = ? WHERE tuition = ?";

    private final static String DELETE = "DELETE FROM Car WHERE tuition = ?";

    private final static String FIND_BY_TUITION = "SELECT * FROM Car WHERE tuition = ?";

    @Override
    public List<Car> findAll() throws SQLException {
        List<Car> cars = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(FIND_ALL)) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String tuition = result.getString("tuition");
                String brand = result.getString("brand");
                String model = result.getString("model");
                boolean isRented = result.getBoolean("isRented");
                Car car = new Car(tuition, brand, model, isRented);
                cars.add(car);
            }
        }
        return cars;
    }

    @Override
    public Car findById(int id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String tuition = result.getString("tuition");
                String brand = result.getString("brand");
                String model = result.getString("model");
                boolean isRented = result.getBoolean("isRented");
                return new Car(tuition, brand, model, isRented);
            }
            return null;
        }
    }

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


    public Car findByTuition(String tuition) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_TUITION)) {
            statement.setString(1, tuition);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String brand = result.getString("brand");
                String model = result.getString("model");
                boolean isRented = result.getBoolean("isRented");
                return new Car(tuition, brand, model, isRented);
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
