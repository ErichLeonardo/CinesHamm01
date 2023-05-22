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

            Car car1 = new Car("0006CIN", "", "", true);
            carDAO.insert(car1);
            /*Car car2 = new Car("0001CIN", null, null, true);
            Car car5 = new Car("0005CIN", null, null, true);
            Car car3 = new Car("0003CIN", null, null, true);
            Car car4 = new Car("0004CIN", null, null, true);
            carDAO.insert(car2);
            carDAO.insert(car3);
            carDAO.insert(car4);
            carDAO.insert(car5);*/
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

