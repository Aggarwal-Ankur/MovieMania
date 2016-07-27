package com.ankuraggarwal.moviemania.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ankuraggarwal.moviemania.R;
import com.ankuraggarwal.moviemania.data.MovieReviews;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieReviewFragment extends Fragment {

    private static final String TAG = MovieReviewFragment.class.getSimpleName();

    private static final String MOVIE_REVIEW_TITLE_KEY = "movie_review_title";
    private static final String MOVIE_REVIEW_CONTENT_KEY = "movie_review_content";

    private String mMovieReviewTitle, mMovieReviewContent;


    public MovieReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();

        if(arguments != null){
            mMovieReviewTitle = arguments.getString(MOVIE_REVIEW_TITLE_KEY);
            mMovieReviewContent = arguments.getString(MOVIE_REVIEW_CONTENT_KEY);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_review, container, false);

        TextView movieReviewTitleTV= (TextView) v.findViewById(R.id.review_title);
        TextView movieReviewContentTV= (TextView) v.findViewById(R.id.review_content);


        if(mMovieReviewTitle != null){
            movieReviewTitleTV.setText(mMovieReviewTitle);
        }

        if(mMovieReviewContent != null){
            movieReviewContentTV.setText(mMovieReviewContent);
        }

        return v;
    }

    public static MovieReviewFragment newInstance(String movieReviewTitle, String movieReviewContent) {
        MovieReviewFragment myFragment = new MovieReviewFragment();

        Bundle args = new Bundle();
        args.putString(MOVIE_REVIEW_TITLE_KEY, movieReviewTitle);
        args.putString(MOVIE_REVIEW_CONTENT_KEY, movieReviewContent);
        myFragment.setArguments(args);

        return myFragment;
    }

}
