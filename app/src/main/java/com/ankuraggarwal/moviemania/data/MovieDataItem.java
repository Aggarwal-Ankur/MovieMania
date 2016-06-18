package com.ankuraggarwal.moviemania.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ankur on 6/16/2016.
 */
public class MovieDataItem implements Serializable{
    @SerializedName("original_title")
    private String mMovieTitle;

    @SerializedName("poster_path")
    private String posterPath;

    private String id;

    public String getMovieTitle() {
        return mMovieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.mMovieTitle = movieTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
