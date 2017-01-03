package com.ankuraggarwal.moviemania;

import android.app.Application;

import com.ankuraggarwal.moviemania.modules.ContextModule;
import com.squareup.picasso.Picasso;

/**
 * Created by Ankur on 12/29/2016.
 */


/**
 * MainFragment -> MainListAdapter -> Picasso -> context
 *
 * MainFragment -> MovieFetchFragment
 *
 * MovieFetchFragment -> Gson, OKHttpClient
 *
 * MovieReviewFragment -> Gson
 *
 *TrailerListFragment -> Gson
 *
 * ContextModule
 *
 * NetworkModule
 *
 * PicassoModule
 *
 */
public class MovieManiaApplication extends Application {
    private MovieManiaApplicationComponent component;

    private Picasso picasso;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerMovieManiaApplicationComponent.builder()
                .contextModule(new ContextModule(this.getApplicationContext()))
                .build();

        picasso = component.providePicasso();
    }

    public MovieManiaApplicationComponent getComponent() {
        return component;
    }
}
