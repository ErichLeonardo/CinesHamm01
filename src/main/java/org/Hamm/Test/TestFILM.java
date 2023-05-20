package org.Hamm.Test;

import org.Hamm.Model.DAO.FilmDAO;
import org.Hamm.Model.Domain.Film;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

public class TestFILM {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/CinesHamm";
        String user = "root";
        String pwd = "";

        try (Connection connection = DriverManager.getConnection(url, user, pwd)) {
            FilmDAO FilmDAO = new FilmDAO(connection);

            /*Film newFilm = new Film(1, "The Matrix", "Action", Time.valueOf("02:16:00"), "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.");
            FilmDAO.insert(newFilm);
            List<Film> films = FilmDAO.findAll();
            System.out.println("Todas las películas:");
            for (Film film : films) {
                System.out.println(film);
            }
            Film film3 = new Film(3, "The Godfather", "Crime, Drama", Time.valueOf("02:55:00"), "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.");
            Film film4 = new Film(4, "The Shawshank Redemption", "Drama", Time.valueOf("02:22:00"), "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.");
            Film film5 = new Film(5, "The Dark Knight", "Action, Crime, Drama", Time.valueOf("02:32:00"), "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.");
            Film film6 = new Film(6, "The Lord of the Rings: The Fellowship of the Ring", "Action, Adventure, Drama", Time.valueOf("02:58:00"), "A meek Hobbit from the Shire and eight companions set out on a journey to destroy the powerful One Ring and save Middle-earth from the Dark Lord Sauron.");
            FilmDAO.insert(film3);
            FilmDAO.insert(film4);
            FilmDAO.insert(film5);
            FilmDAO.insert(film6);*/



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
