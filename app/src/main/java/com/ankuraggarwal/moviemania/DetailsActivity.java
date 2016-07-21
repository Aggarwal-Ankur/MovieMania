package com.ankuraggarwal.moviemania;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ankuraggarwal.moviemania.data.MovieDetailsItem;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    public static final String KEY_MOVIE_DETAILS = "movie_details";

    private ImageView mPosterImage;

    private TextView mSynopsisTv, mRatingTextView, mReleaseDateTextView;

    private static final String IMAGE_FETCH_BASE_URL = "http://image.tmdb.org/t/p/w500";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mPosterImage = (ImageView) findViewById(R.id.imageView);
        mSynopsisTv = (TextView) findViewById(R.id.synopsis);
        mRatingTextView = (TextView) findViewById(R.id.rating);
        mReleaseDateTextView = (TextView) findViewById(R.id.release_date);


        MovieDetailsItem movieDetails = getIntent().getParcelableExtra(KEY_MOVIE_DETAILS);


    }

}
