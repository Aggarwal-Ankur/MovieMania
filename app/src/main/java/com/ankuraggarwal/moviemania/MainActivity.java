package com.ankuraggarwal.moviemania;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ankuraggarwal.moviemania.data.MovieDataItem;
import com.ankuraggarwal.moviemania.data.MovieDetailsItem;
import com.ankuraggarwal.moviemania.data.MovieResults;
import com.ankuraggarwal.moviemania.fragments.DetailsFragment;
import com.ankuraggarwal.moviemania.fragments.MainFragment;
import com.ankuraggarwal.moviemania.fragments.MovieFetchFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.ankuraggarwal.moviemania.BuildConfig.MOVIE_DB_API_KEY;

public class MainActivity extends AppCompatActivity implements MovieFetchFragment.FetchCallbacks, MainFragment.OnMovieSelectedListener{


    private static final String TAG_ASYNC_FRAGMENT = "async_fragment";


    //Constants for Shared Preferences
    private static final int POPULAR_MOVIES_PREF = 1;
    private static final int TOP_RATED_MOVIES_PREF = 2;
    private static final int FAVORITE_MOVIES_PREF = 3;


    /** Because this is a retained fragment and our AsyctTask is inside this, we do not need to implement onSaveInstanceState() in this activity*/
    private MovieFetchFragment mMovieFetchFragment;

    private List<MovieDataItem> mDataItems;



    private static final String MOVIE_LIST_KEY = "movie_list";
    private static final String SELECTED_MOVIE_KEY = "selected_movie";

    private ProgressDialog mDialog;
    private String mSavedListJson;

    private boolean isFavoritesView = false;

    private boolean dualPane = false;

    private DetailsFragment detailsFragment;
    private MainFragment mainFragment;

    private MovieDetailsItem mSelectedMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check if the layout is dual pane or single pane
        detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment);

        if(detailsFragment != null){
            dualPane = true;
        }else{
            dualPane = false;
        }

        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);

        FragmentManager fm = getFragmentManager();
        mMovieFetchFragment = (MovieFetchFragment) fm.findFragmentByTag(TAG_ASYNC_FRAGMENT);

        //Build the dialog
        mDialog = new ProgressDialog(this);
        mDialog.setIndeterminate(true);
        mDialog.setMessage("Fetching Data...");

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        int listType = sharedPref.getInt(getString(R.string.list_type_preference), POPULAR_MOVIES_PREF);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mMovieFetchFragment == null) {
            mMovieFetchFragment = new MovieFetchFragment();
            fm.beginTransaction().add(mMovieFetchFragment, TAG_ASYNC_FRAGMENT).commit();
            fm.executePendingTransactions();

            if(listType == TOP_RATED_MOVIES_PREF){
                fetchTopRatedMovies();
            }else if(listType == POPULAR_MOVIES_PREF){
                fetchPopularMovies();
            }else{
                mMovieFetchFragment.fetchFavoritesList();
            }
        }

        if(listType == TOP_RATED_MOVIES_PREF){
            setTitle(getResources().getString(R.string.top_rated_movies));
            isFavoritesView = false;
        }else if(listType == POPULAR_MOVIES_PREF){
            setTitle(getResources().getString(R.string.popular_movies));
            isFavoritesView = false;
        }else{
            setTitle(getResources().getString(R.string.favorites));
            isFavoritesView = true;
        }



        mDataItems = new ArrayList<>();

        List<MovieDataItem> alreadyFetchedItems = mMovieFetchFragment.getMovieList();

        if(alreadyFetchedItems != null && !alreadyFetchedItems.isEmpty()){
            mDataItems.addAll(alreadyFetchedItems);

            mainFragment.updateMovieList(mDataItems);

            if(savedInstanceState != null){
                //Check for selected Movie

                mSelectedMovie = savedInstanceState.getParcelable(SELECTED_MOVIE_KEY);

                if(mSelectedMovie != null && dualPane){
                    detailsFragment.updateMovieDetails(mSelectedMovie);
                }
            }
        }else{
            //Check if data is available in the saved instance
            if(savedInstanceState != null){
                String listJson = savedInstanceState.getString(MOVIE_LIST_KEY);

                if(listJson != null){
                    mSavedListJson = listJson;
                    Gson gson = new Gson();

                    alreadyFetchedItems = gson.fromJson(listJson, MovieResults.class).getResults();

                    mDataItems.addAll(alreadyFetchedItems);

                    mainFragment.updateMovieList(mDataItems);
                }


                //Check for selected Movie

                mSelectedMovie = savedInstanceState.getParcelable(SELECTED_MOVIE_KEY);

                if(mSelectedMovie != null && dualPane){
                    detailsFragment.updateMovieDetails(mSelectedMovie);
                }
            }
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();


        switch (item.getItemId()){
            case R.id.action_popular:
                fetchPopularMovies();
                editor.putInt(getString(R.string.list_type_preference), POPULAR_MOVIES_PREF);
                editor.commit();
                isFavoritesView = false;
                setTitle(getResources().getString(R.string.popular_movies));
                return true;

            case R.id.action_top_rated:
                fetchTopRatedMovies();
                editor.putInt(getString(R.string.list_type_preference), TOP_RATED_MOVIES_PREF);
                editor.commit();
                setTitle(getResources().getString(R.string.top_rated_movies));
                isFavoritesView = false;
                return true;

            case R.id.action_favorites:
                mMovieFetchFragment.fetchFavoritesList();

                mDialog.show();
                editor.putInt(getString(R.string.list_type_preference), FAVORITE_MOVIES_PREF);
                editor.commit();
                setTitle(getResources().getString(R.string.favorites));
                isFavoritesView = true;
                return true;

            default:

                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * This is used to save the already fetched JSON in the bundle, so that it could be quickly retrieved if activity gets destroyed
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(MOVIE_LIST_KEY, mSavedListJson);
        outState.putParcelable(SELECTED_MOVIE_KEY, mSelectedMovie);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onListFetchCompleted(List<MovieDataItem> movieDataItems) {
        if(mDataItems == null){
            return;
        }
        mDataItems.clear();

        mSavedListJson = mMovieFetchFragment.getListJson();

        //Error handling
        if(movieDataItems != null){
            mDataItems.addAll(movieDataItems);
        }else{
            Snackbar.make(findViewById(android.R.id.content), R.string.error_connectivity, Snackbar.LENGTH_LONG).show();
        }

        mainFragment.updateMovieList(mDataItems);

        mDialog.dismiss();
    }

    @Override
    public void onDetailsFetchCompleted(MovieDetailsItem movieDetails){
        if(movieDetails == null){
            //Some error occurred
            //TODO

        }

        mSelectedMovie = movieDetails;

        if(!dualPane){
            Intent detailsIntent = new Intent(MainActivity.this, DetailsActivity.class);
            detailsIntent.putExtra(DetailsActivity.KEY_MOVIE_DETAILS, movieDetails);
            startActivity(detailsIntent);
        }else{
            detailsFragment.updateMovieDetails(movieDetails);
            detailsFragment.clearBackstack();
        }

        mDialog.dismiss();
    }

    @Override
    public void onMovieSelected(String movieId) {
        mDialog.show();

        if(isFavoritesView){
            mMovieFetchFragment.fetchMovieDetailFromDb(movieId);
        }else{
            Uri.Builder uriBuilder = new Uri.Builder();

            String url = uriBuilder.scheme(IConstants.URL_SCHEME)
                    .authority(IConstants.BASE_URL)
                    .appendPath(IConstants.EXTRA_PATH_1)
                    .appendPath(IConstants.EXTRA_PATH_2)
                    .appendPath(movieId)
                    .appendQueryParameter(IConstants.API_KEY_PARAMETER, MOVIE_DB_API_KEY)
                    .build().toString();

            mMovieFetchFragment.fetchMovieDetailsFromUrl(url);
        }
    }

    /**
     * Utility function to fetch popular movies
     */
    private void fetchPopularMovies(){
        Uri.Builder uriBuilder = new Uri.Builder();

        String url = uriBuilder.scheme(IConstants.URL_SCHEME)
                .authority(IConstants.BASE_URL)
                .appendPath(IConstants.EXTRA_PATH_1)
                .appendPath(IConstants.EXTRA_PATH_2)
                .appendPath(IConstants.POPULAR_PATH)
                .appendQueryParameter(IConstants.API_KEY_PARAMETER, MOVIE_DB_API_KEY)
                .build().toString();

        mMovieFetchFragment.fetchListFromUrl(url);

        mDialog.show();
    }

    /**
     * Utility function to fetch Top rated movies
     */
    private void fetchTopRatedMovies(){
        Uri.Builder uriBuilder = new Uri.Builder();

        String url = uriBuilder.scheme(IConstants.URL_SCHEME)
                .authority(IConstants.BASE_URL)
                .appendPath(IConstants.EXTRA_PATH_1)
                .appendPath(IConstants.EXTRA_PATH_2)
                .appendPath(IConstants.TOP_RATED_PATH)
                .appendQueryParameter(IConstants.API_KEY_PARAMETER, MOVIE_DB_API_KEY)
                .build().toString();

        mMovieFetchFragment.fetchListFromUrl(url);

        mDialog.show();
    }

    @Override
    public void onBackPressed() {

        if(!(dualPane && detailsFragment.handleBackPressed())){
            super.onBackPressed();
        }

    }
}
