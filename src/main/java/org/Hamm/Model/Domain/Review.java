package org.Hamm.Model.Domain;

import java.util.Objects;

public class Review {
    private String NameOfTheName;
    private String Review;

    public Review(String nameOfTheName, String review) {
        NameOfTheName = nameOfTheName;
        Review = review;
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
        return Objects.equals(Review, review.Review);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Review);
    }

    @Override
    public String toString() {
        return "Review{" +
                "NameOfTheName='" + NameOfTheName + '\'' +
                ", Review='" + Review + '\'' +
                '}';
    }
}
