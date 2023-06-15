package org.Hamm.Model.DAO;

import org.Hamm.Model.Domain.Review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {
    private Connection connection;

    public ReviewDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Review> findAll() throws SQLException {
        List<Review> reviews = new ArrayList<>();

        String sql = "SELECT review, name_of_the_movie FROM review";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String reviewText = resultSet.getString("review");
                String movieName = resultSet.getString("name_of_the_movie");

                Review review = new Review(reviewText, movieName);
                reviews.add(review);
            }
        }

        return reviews;
    }

    public void addReview(Review review) throws SQLException {
        String sql = "INSERT INTO review (review, name_of_the_movie) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, review.getReview());
            statement.setString(2, review.getNameOfTheName());

            statement.executeUpdate();
        }
    }
}
