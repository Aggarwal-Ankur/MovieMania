package com.ankuraggarwal.moviemania.data;

import java.io.Serializable;

/**
 * Created by Ankur on 6/16/2016.
 */
public class MovieDataItem implements Serializable{
    private String mMovieTitle;
    private int mImgResource;

    public MovieDataItem(String title, int imgResource){
        this.mMovieTitle = title;
        this.mImgResource = imgResource;
    }

    public String getMovieTitle() {
        return mMovieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.mMovieTitle = movieTitle;
    }

    public int getImgResource() {
        return mImgResource;
    }

    public void setImgResource(int imgResource) {
        this.mImgResource = imgResource;
    }
}
