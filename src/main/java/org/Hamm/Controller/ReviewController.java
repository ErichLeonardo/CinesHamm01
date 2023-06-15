package org.Hamm.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.Hamm.Model.DAO.CarDAO;
import org.Hamm.Model.DAO.ReviewDAO;
import org.Hamm.Model.Domain.Car;
import org.Hamm.Model.Domain.Review;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ReviewController {

    private Connection connection;
    private ReviewDAO reviewDAO;

    @FXML
    private TextField movieTitleField;
    @FXML
    private TextArea movieReviewField;
    @FXML
    private Label statusLabel;

    public void setConnection(Connection connection) {
        this.connection = connection;
        reviewDAO = new ReviewDAO(connection);
    }

    @FXML
    private void initialize() {
        // Lógica de inicialización, si es necesario
    }

    @FXML
    void handleAddButton() {
        String url = "jdbc:mysql://localhost:3306/CinesHamm";
        String user = "root";
        String pwd = "";
        try (Connection connection = DriverManager.getConnection(url, user, pwd)){
            ReviewDAO reviewDAO = new ReviewDAO(connection);

            String NameOfTheName = movieTitleField.getText();
            String Review = movieReviewField.getText();

            Review newReview = new Review(NameOfTheName, Review);
            reviewDAO.addReview(newReview);

            statusLabel.setText("Review added successfully!");
            movieTitleField.clear();
            movieReviewField.clear();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setMovieTitle(String movieTitle) {
        movieTitleField.setText(movieTitle);
    }
}
