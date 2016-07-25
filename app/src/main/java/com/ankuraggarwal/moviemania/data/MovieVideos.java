package com.ankuraggarwal.moviemania.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankur on 25-Jul-16.
 */

public class MovieVideos {
    private String id;

    @SerializedName("results")
    private List<Results> movieVideoList;

    public List<Results> getMovieVideoList() {
        return movieVideoList;
    }

    public String getId() {
        return id;
    }

    public class Results{
        private String name;
        private String key;

        public String getName() {
            return name;
        }

        public String getKey() {
            return key;
        }
    }
}
