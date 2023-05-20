package org.Hamm.Test;


import org.Hamm.Model.DAO.CarDAO;
import org.Hamm.Model.Domain.Car;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestCAR {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/CinesHamm";
        String user = "root";
        String pwd = "";

        try (Connection connection = DriverManager.getConnection(url, user, pwd)) {
            CarDAO carDAO = new CarDAO(connection);

            Car car1 = new Car("TYU-123", "Toyota", "Corolla", false);
            Car car2 = new Car("DEF-456", null, null, true);
            carDAO.insert(car1);
            System.out.println("Car " + car1.getTuition() + " inserted successfully");

            // Insert car2
            carDAO.insert(car2);
            System.out.println("Car " + car2.getTuition() + " inserted successfully");
            /*
            Car car = carDAO.findByTuition("ABC-123");
            System.out.println("Before update: " + car);
            car.setBrand("Nissan");
            car.setModel("Sentra");
            carDAO.update(car);
            car = carDAO.findByTuition("ABC-123");
            System.out.println("After update: " + car);*/

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

