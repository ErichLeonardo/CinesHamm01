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

    /**
     * Retrieves all reviews from the database.
     *
     * @return a list of Review objects representing the reviews
     * @throws SQLException if any SQL error occurs
     */
    public List<Review> findAll() throws SQLException {
        List<Review> reviews = new ArrayList<>();

        String sql = "SELECT id_review, name_of_the_movie, review FROM review";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int idReview = resultSet.getInt("id_review");
                String movieName = resultSet.getString("name_of_the_movie");
                String reviewText = resultSet.getString("review");

                Review review = new Review(idReview, movieName, reviewText);
                reviews.add(review);
            }
        }

        return reviews;
    }

    /**
     * Adds a new review to the database.
     *
     * @param review the Review object to be added
     * @throws SQLException if any SQL error occurs
     */
    public void addReview(Review review) throws SQLException {
        String sql = "INSERT INTO review (id_review, name_of_the_movie, review) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, review.getId_review());
            statement.setString(2, review.getNameOfTheFilm());
            statement.setString(3, review.getReview());

            statement.executeUpdate();
        }
    }

    /**
     * Searches for reviews matching the provided movie name.
     *
     * @param newValue the movie name to search for
     * @return a list of Review objects matching the search criteria
     * @throws SQLException if any SQL error occurs
     */
    public List<Review> search(String newValue) throws SQLException {
        List<Review> matchingReviews = new ArrayList<>();

        String sql = "SELECT id_review, name_of_the_movie, review FROM review WHERE name_of_the_movie LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + newValue + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int idReview = resultSet.getInt("id_review");
                    String movieName = resultSet.getString("name_of_the_movie");
                    String reviewText = resultSet.getString("review");

                    Review review = new Review(idReview, movieName, reviewText);
                    matchingReviews.add(review);
                }
            }
        }
        return matchingReviews;
    }

    /**
     * Deletes a review from the database.
     *
     * @param review the Review object to be deleted
     * @throws SQLException if any SQL error occurs
     */
    public void deleteReview(Review review) throws SQLException {
        String sql = "DELETE FROM review WHERE id_review = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, review.getId_review());

            statement.executeUpdate();
        }
    }
}
