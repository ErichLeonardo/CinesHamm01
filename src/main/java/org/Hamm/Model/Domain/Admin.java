package org.Hamm.Model.Domain;

import java.util.List;
import java.util.Objects;

public class Admin extends Person {
    private int id_admin;
    private String email;
    private String password;
    List<User> userList;
    List<Car> carList;
    List<Film> filmList;
    List<Reservation> reservationList;

    public Admin(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<Car> getCarList() {
        return carList;
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }

    public List<Film> getFilmList() {
        return filmList;
    }

    public void setFilmList(List<Film> filmList) {
        this.filmList = filmList;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Admin admin = (Admin) o;
        return id_admin == admin.id_admin && Objects.equals(email, admin.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id_admin, email);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id_admin=" + id_admin +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userList=" + userList +
                ", carList=" + carList +
                ", filmList=" + filmList +
                ", reservationList=" + reservationList +
                '}';
    }
}


