package org.Hamm.Test;



import org.Hamm.Model.DAO.ReservationDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestRESERVATION {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/CinesHamm";
        String user = "root";
        String pwd = "";

        try (Connection connection = DriverManager.getConnection(url, user, pwd)) {
            ReservationDAO reservationDAO = new ReservationDAO(connection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


