package com.ankuraggarwal.moviemania.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankur on 18-Jun-16.
 */

public class MovieResults {
    @SerializedName("results")
    private List<MovieDataItem> results;

    public List<MovieDataItem> getResults() {
        return results;
    }

    public void setResults(List<MovieDataItem> results) {
        this.results = results;
    }
}
