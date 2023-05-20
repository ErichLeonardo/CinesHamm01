package org.Hamm.Model.Domain;

import org.Hamm.Utils.Validator;

import java.util.List;
import java.util.Objects;

public class Car {
    private int id_car;
    private String tuition;
    private String brand;
    private String model;
    private boolean rented;
    List<Reservation> reservations;
    List<User> users;

    public Car(String tuition, String brand, String model, boolean rented) {
        this.tuition = tuition;
        this.brand = brand;
        this.model = model;
        this.rented = rented;
    }


    public int getId_car() {
        return id_car;
    }

    public void setId_car() {
        this.id_car = id_car;
    }

    public String getTuition() {
        return tuition;
    }

    public void setTuition(String tuition) {
            this.tuition = tuition;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id_car=" + id_car +
                ", tuition='" + tuition + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", rented=" + rented +
                ", reservations=" + reservations +
                ", users=" + users +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id_car == car.id_car && Objects.equals(tuition, car.tuition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_car, tuition);
    }
}
