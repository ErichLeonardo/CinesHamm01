package org.Hamm.Test;


import org.Hamm.Model.DAO.UserDAO;
import org.Hamm.Model.Domain.User;
import org.Hamm.Utils.PasswordUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;


public class TestUSER {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/CinesHamm";
        String user = "root";
        String pwd = "";

        try (Connection connection = DriverManager.getConnection(url, user, pwd)) {
            UserDAO userDAO = new UserDAO(connection);

            User userToInsert = new User();
            userToInsert.setEmail("admin");
            userToInsert.setPassword("1234");
            userToInsert.setName("Vicky");
            userToInsert.setSurname("Sell");
            userToInsert.setPhone("606935963");

            User insertedUser = userDAO.insert(userToInsert);

            System.out.println("User inserted:");
            System.out.println(insertedUser);

            User adminUser = userDAO.makeAdmin(insertedUser);
/*

            User foundUser = userDAO.findById(11);

            System.out.println("User found:");
            System.out.println(foundUser);

            //userDAO.delete(insertedUser);

            List<User> userList = userDAO.findAll();
            for (User user1 : userList) {
                System.out.println("ID: " + user1.getId_user());
                System.out.println("Email: " + user1.getEmail());
                System.out.println("Password: " + user1.getPassword());
                System.out.println("Name: " + user1.getName());
                System.out.println("Surname: " + user1.getSurname());
                System.out.println("Phone: " + user1.getPhone());
                System.out.println("Is admin: " + user1.is_admin());
                System.out.println();
            }
            User userToUpdate = userDAO.findById(12);
            if (userToUpdate != null) {
                userToUpdate.setEmail("rafael34@test.com");
                userToUpdate.setHashedPassword(PasswordUtils.hashPassword("trp56"));
                userToUpdate.setName("Rafa");
                userToUpdate.setSurname("Perez");
                userToUpdate.setPhone("987654321");
                User updatedUser = userDAO.update(userToUpdate);
                System.out.println("User updated:");
                System.out.println(updatedUser);
            }

           // User adminUser = userDAO.makeAdmin(insertedUser);

            // Mostrar el usuario después de hacerlo administrador
           // System.out.println("User after making admin:");
            //System.out.println(adminUser);

        */
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }
}
