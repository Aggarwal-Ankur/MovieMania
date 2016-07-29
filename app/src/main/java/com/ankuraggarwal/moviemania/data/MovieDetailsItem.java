package com.ankuraggarwal.moviemania.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankur on 6/20/2016.
 */

public class MovieDetailsItem implements Parcelable{
    private String id;

    private String title;

    @SerializedName("release_date")
    private String releaseDate;

    private String overview;

    @SerializedName("vote_average")
    private String voteAverage;

    @SerializedName("poster_path")
    private String posterPath;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(overview);
        dest.writeString(voteAverage);
        dest.writeString(posterPath);
    }

    public MovieDetailsItem(){
        //Required empty constructor
    }

    public MovieDetailsItem(Parcel inParcel){
        id = inParcel.readString();
        title = inParcel.readString();
        releaseDate = inParcel.readString();
        overview = inParcel.readString();
        voteAverage = inParcel.readString();
        posterPath = inParcel.readString();
    }

    public static final Parcelable.Creator<MovieDetailsItem> CREATOR = new Parcelable.Creator<MovieDetailsItem>(){
        @Override
        public MovieDetailsItem createFromParcel(Parcel source) {
            return new MovieDetailsItem(source);
        }

        @Override
        public MovieDetailsItem[] newArray(int size) {
            return new MovieDetailsItem[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
