package org.Hamm.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.Hamm.Model.Connections.ConnectionMySQL;
import org.Hamm.Model.DAO.ReviewDAO;
import org.Hamm.Model.Domain.Film;
import org.Hamm.Model.Domain.Review;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListReviewAdmin {

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @FXML
    private TableView<Review> tableView;

    @FXML
    private TableColumn<Review, Integer> Id_review;
    @FXML
    private TableColumn<Review, String> NameOfTheNameColumn;
    @FXML
    private TableColumn<Review, String> ReviewColumn;

    @FXML
    private TextField searchField;

    private ReviewDAO reviewDAO;

    public void execute() {
        Id_review.setCellValueFactory(new PropertyValueFactory<>("Id_review"));
        NameOfTheNameColumn.setCellValueFactory(new PropertyValueFactory<>("Review"));
        ReviewColumn.setCellValueFactory(new PropertyValueFactory<>("NameOfTheName"));


        reviewDAO = new ReviewDAO(connection);

        try {
            Connection connection = ConnectionMySQL.getConnect();
            reviewDAO = new ReviewDAO(connection);
            List<Review> reviews = reviewDAO.findAll();
            tableView.getItems().clear();
            tableView.getItems().addAll(reviews);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                List<Review> reviews = reviewDAO.search(newValue);
                tableView.getItems().clear();
                tableView.getItems().addAll(reviews);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void handleSearchButton() {
        String searchTerm = searchField.getText();

        try {
            List<Review> reviews = reviewDAO.search(searchTerm);
            tableView.getItems().clear();
            tableView.getItems().addAll(reviews);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleDeleteButton() throws SQLException {
        Review selectedReview = tableView.getSelectionModel().getSelectedItem();
        if (selectedReview == null) {
            System.out.println("No se ha seleccionado ninguna review.");
            return;
        }

        reviewDAO.deleteReview(selectedReview);
        tableView.getItems().remove(selectedReview);
        System.out.println("Review eliminada: " + selectedReview);
    }


}
