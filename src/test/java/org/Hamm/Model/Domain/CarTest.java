package org.Hamm.Model.Domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {

    @Test
    void getId_car() {
        // Arrange
        Car car = new Car(1, "4560GPR", "Toyota", "Corolla", false);

        // Act
        int idCar = car.getId_car();

        // Assert
        assertEquals(1, idCar);
    }

    @Test
    void getTuition() {
        // Arrange
        Car car = new Car(1, "4560GPR", "Toyota", "Corolla", false);

        // Act
        String tuition = car.getTuition();

        // Assert
        assertEquals("4560GPR", tuition);
    }

    @Test
    void setTuition() {
        // Arrange
        Car car = new Car(1, "4560GPR", "Toyota", "Corolla", false);

        // Act
        car.setTuition("3569TRR");

        // Assert
        assertEquals("3569TRR", car.getTuition());
    }

    @Test
    void getBrand() {
        // Arrange
        Car car = new Car(1, "4560GPR", "Toyota", "Corolla", false);

        // Act
        String brand = car.getBrand();

        // Assert
        assertEquals("Toyota", brand);
    }

    @Test
    void setBrand() {
        // Arrange
        Car car = new Car(1, "4560GPR", "Toyota", "Corolla", false);

        // Act
        car.setBrand("Honda");

        // Assert
        assertEquals("Honda", car.getBrand());
    }

    @Test
    void getModel() {
        // Arrange
        Car car = new Car(1, "4560GPR", "Toyota", "Corolla", false);

        // Act
        String model = car.getModel();

        // Assert
        assertEquals("Corolla", model);
    }

    @Test
    void setModel() {
        // Arrange
        Car car = new Car(1, "4560GPR", "Toyota", "Corolla", false);

        // Act
        car.setModel("Civic");

        // Assert
        assertEquals("Civic", car.getModel());
    }

    @Test
    void isRented() {
        // Arrange
        Car car = new Car(1, "4560GPR", "Toyota", "Corolla", false);

        // Act
        boolean isRented = car.isRented();

        // Assert
        assertFalse(isRented);
    }

    @Test
    void setRented() {
        // Arrange
        Car car = new Car(1, "4560GPR", "Toyota", "Corolla", false);

        // Act
        car.setRented(true);

        // Assert
        assertTrue(car.isRented());
    }

    @Test
    void getReservations() {
        // Arrange
        Car car = new Car(1, "4560GPR", "Toyota", "Corolla", false);
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation());
        car.setReservations(reservations);

        // Act
        List<Reservation> carReservations = car.getReservations();

        // Assert
        assertEquals(reservations, carReservations);
    }

    @Test
    void setReservations() {
        // Arrange
        Car car = new Car(1, "4560GPR", "Toyota", "Corolla", false);
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation());

        // Act
        car.setReservations(reservations);

        // Assert
        assertEquals(reservations, car.getReservations());
    }

    @Test
    void testToString() {
        // Arrange
        Car car = new Car(1, "4560GPR", "Toyota", "Corolla", false);

        // Act
        String carString = car.toString();

        // Assert
        assertEquals("Car{id_car=1, tuition='4560GPR', brand='Toyota', model='Corolla', rented=false, reservations=null, users=null}", carString);
    }

    @Test
    void testEquals() {
        // Arrange
        Car car1 = new Car(1, "4560GPR", "Toyota", "Corolla", false);
        Car car2 = new Car(1, "4560GPR", "Toyota", "Corolla", false);
        Car car3 = new Car(2, "3569TRR", "Honda", "Civic", true);

        // Assert
        assertEquals(car1, car2);
        assertNotEquals(car1, car3);
    }

    @Test
    void testHashCode() {
        // Arrange
        Car car1 = new Car(1, "4560GPR", "Toyota", "Corolla", false);
        Car car2 = new Car(1, "4560GPR", "Toyota", "Corolla", false);

        // Assert
        assertEquals(car1.hashCode(), car2.hashCode());
    }
}
