package com.ankuraggarwal.moviemania.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankur on 25-Jul-16.
 */

public class MovieReviews {
    @SerializedName("results")
    private List<Results> movieReviewList;

    public List<Results> getMovieReviewList() {
        return movieReviewList;
    }


    public class Results{
        private String author;
        private String content;

        public String getContent() {
            return content;
        }

        public String getAuthor() {
            return author;
        }
    }
}
