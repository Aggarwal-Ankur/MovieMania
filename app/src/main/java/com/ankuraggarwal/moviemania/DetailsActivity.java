package com.ankuraggarwal.moviemania;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ankuraggarwal.moviemania.data.MovieDataItem;
import com.ankuraggarwal.moviemania.data.MovieDetailsItem;
import com.ankuraggarwal.moviemania.fragments.DetailsFragment;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getSimpleName();

    public static final String KEY_MOVIE_DETAILS = "movie_details";

    private DetailsFragment detailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MovieDetailsItem movieDetails = getIntent().getParcelableExtra(KEY_MOVIE_DETAILS);

        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_MOVIE_DETAILS, movieDetails);
        detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.details_container, detailsFragment)
                .commit();


        Configuration config = getResources().getConfiguration();
        Log.d(TAG, "Smallest Width =" + config.smallestScreenWidthDp);
    }

    @Override
    public void onBackPressed() {

        if(!(detailsFragment.handleBackPressed())){
            super.onBackPressed();
        }

    }

}
