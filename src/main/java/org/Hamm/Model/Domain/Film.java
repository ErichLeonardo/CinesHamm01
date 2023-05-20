package org.Hamm.Model.Domain;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Film {
    private int id_film;
    private String title;
    private String genre;
    private Time duration;
    private String synopsis;
    private List<Reservation> reservationList;

    public Film(int id_film, String title, String genre, Time duration, String synopsis) {
        this.id_film = id_film;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.synopsis = synopsis;
        this.reservationList = new ArrayList<>();
    }

    public int getId_film() {
        return id_film;
    }

    public void setId_film(int id_film) {
        this.id_film = id_film;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public void addReservation(Reservation reservation) {
        reservationList.add(reservation);
    }

    @Override
    public String toString() {
        return "Film{" +
                "id_film=" + id_film +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", duration=" + duration +
                ", synopsis='" + synopsis + '\'' +
                ", reservationList=" + reservationList +
                '}';
    }
}
