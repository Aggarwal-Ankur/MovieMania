package com.ankuraggarwal.moviemania;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ankuraggarwal.moviemania.data.MovieDataItem;
import com.ankuraggarwal.moviemania.data.MovieDetailsItem;
import com.ankuraggarwal.moviemania.fragments.DetailsFragment;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    public static final String KEY_MOVIE_DETAILS = "movie_details";


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
        DetailsFragment fragobj = new DetailsFragment();
        fragobj.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.details_container, fragobj)
                .commit();
    }

}
