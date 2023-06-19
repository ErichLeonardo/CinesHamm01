package org.Hamm.Model.Domain;

import java.util.Objects;

public class Review {

    private int Id_review;
    private String NameOfTheName;
    private String Review;



    public Review(int id_review, String nameOfTheName, String review) {
        Id_review = id_review;
        NameOfTheName = nameOfTheName;
        Review = review;
    }

    public int getId_review() {
        return Id_review;
    }

    public void setId_review(int id_review) {
        Id_review = id_review;
    }

    public String getNameOfTheName() {
        return NameOfTheName;
    }

    public void setNameOfTheName(String nameOfTheName) {
        NameOfTheName = nameOfTheName;
    }

    public String getReview() {
        return Review;
    }

    public void setReview(String review) {
        Review = review;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Id_review == review.Id_review && Objects.equals(Review, review.Review);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id_review, Review);
    }

    @Override
    public String toString() {
        return "Review{" +
                "Id_review=" + Id_review +
                ", NameOfTheName='" + NameOfTheName + '\'' +
                ", Review='" + Review + '\'' +
                '}';
    }

}
