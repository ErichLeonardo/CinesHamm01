package org.Hamm.Model.Domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void isIs_admin() {
        // Arrange
        User user = new User();
        user.setIs_admin(true);

        // Act
        boolean isAdmin = user.isIs_admin();

        // Assert
        assertTrue(isAdmin);
    }

    @Test
    void setIs_admin() {
        // Arrange
        User user = new User();

        // Act
        user.setIs_admin(true);

        // Assert
        assertTrue(user.isIs_admin());
    }

    @Test
    void getId_user() {
        // Arrange
        User user = new User();
        user.setId_user(1);

        // Act
        int idUser = user.getId_user();

        // Assert
        assertEquals(1, idUser);
    }

    @Test
    void getEmail() {
        // Arrange
        User user = new User();
        user.setEmail("example@example.com");

        // Act
        String email = user.getEmail();

        // Assert
        assertEquals("example@example.com", email);
    }

    @Test
    void setEmail() {
        // Arrange
        User user = new User();

        // Act
        user.setEmail("example@example.com");

        // Assert
        assertEquals("example@example.com", user.getEmail());
    }

    @Test
    void getPassword() {
        // Arrange
        User user = new User();
        user.setPassword("password");

        // Act
        String password = user.getPassword();

        // Assert
        assertEquals("password", password);
    }

    @Test
    void setPassword() {
        // Arrange
        User user = new User();

        // Act
        user.setPassword("password");

        // Assert
        assertEquals("password", user.getPassword());
    }

    @Test
    void getName() {
        // Arrange
        User user = new User();
        user.setName("John");

        // Act
        String name = user.getName();

        // Assert
        assertEquals("John", name);
    }

    @Test
    void setName() {
        // Arrange
        User user = new User();

        // Act
        user.setName("John");

        // Assert
        assertEquals("John", user.getName());
    }

    @Test
    void getSurname() {
        // Arrange
        User user = new User();
        user.setSurname("Doe");

        // Act
        String surname = user.getSurname();

        // Assert
        assertEquals("Doe", surname);
    }

    @Test
    void setSurname() {
        // Arrange
        User user = new User();

        // Act
        user.setSurname("Doe");

        // Assert
        assertEquals("Doe", user.getSurname());
    }

    @Test
    void getPhone() {
        // Arrange
        User user = new User();
        user.setPhone("123456789");

        // Act
        String phone = user.getPhone();

        // Assert
        assertEquals("123456789", phone);
    }

    @Test
    void setPhone() {
        // Arrange
        User user = new User();

        // Act
        user.setPhone("123456789");

        // Assert
        assertEquals("123456789", user.getPhone());
    }


}
