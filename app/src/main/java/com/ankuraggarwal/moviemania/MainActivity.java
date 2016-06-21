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
import com.ankuraggarwal.moviemania.fragments.MovieFetchFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.ankuraggarwal.moviemania.BuildConfig.MOVIE_DB_API_KEY;

public class MainActivity extends AppCompatActivity implements MovieFetchFragment.FetchCallbacks, MainListAdapter.ListItemClickCallback{


    private static final String TAG_ASYNC_FRAGMENT = "async_fragment";
    // These are the parameters to build the URL
    private static final String URL_SCHEME = "https";
    private static final String BASE_URL = "api.themoviedb.org";

    private static final String EXTRA_PATH_1 = "3";
    private static final String EXTRA_PATH_2 = "movie";
    private static final String POPULAR_PATH = "popular";
    private static final String TOP_RATED_PATH = "top_rated";

    private static final String API_KEY_PARAMETER = "api_key";

    //Constants for Shared Preferenced
    private static final int POPULAR_MOVIES_PREF = 1;
    private static final int TOP_RATED_MOVIES_PREF = 2;

    //Constants for Layouts
    private static final int GRID_SPAN = 2;


    /** Because this is a retained fragment and our AsyctTask is inside this, we do not need to implement onSaveInstanceState() in this activity*/
    private MovieFetchFragment mMovieFetchFragment;
    private MainListAdapter mlAdapter;
    private List<MovieDataItem> mDataItems;



    private static final String MOVIE_LIST_KEY = "movie_list";

    private ProgressDialog mDialog;
    private GridLayoutManager mGridManager;
    private String mSavedListJson;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();
        mMovieFetchFragment = (MovieFetchFragment) fm.findFragmentByTag(TAG_ASYNC_FRAGMENT);

        //Build the dialog
        mDialog = new ProgressDialog(this);
        mDialog.setIndeterminate(true);
        mDialog.setMessage("Fetching Data...");

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mMovieFetchFragment == null) {
            mMovieFetchFragment = new MovieFetchFragment();
            fm.beginTransaction().add(mMovieFetchFragment, TAG_ASYNC_FRAGMENT).commit();

            SharedPreferences sharedPref = this.getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);

            int listType = sharedPref.getInt(getString(R.string.list_type_preference), POPULAR_MOVIES_PREF);

            if(listType == TOP_RATED_MOVIES_PREF){
                fetchTopRatedMovies();
            }else {
                fetchPopularMovies();
            }
        }



        mDataItems = new ArrayList<>();

        List<MovieDataItem> alreadyFetchedItems = mMovieFetchFragment.getMovieList();

        if(alreadyFetchedItems != null && !alreadyFetchedItems.isEmpty()){
            mDataItems.addAll(alreadyFetchedItems);
        }else{
            //Check if data is available in the saved instance
            if(savedInstanceState != null){
                String listJson = savedInstanceState.getString(MOVIE_LIST_KEY);

                if(listJson != null){
                    mSavedListJson = listJson;
                    Gson gson = new Gson();

                    alreadyFetchedItems = gson.fromJson(listJson, MovieResults.class).getResults();

                    mDataItems.addAll(alreadyFetchedItems);
                }
            }
        }

        mGridManager = new GridLayoutManager(MainActivity.this, GRID_SPAN);

        RecyclerView rView = (RecyclerView)findViewById(R.id.movie_recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(mGridManager);
        MovieRecyclerItemDecoration itemDecoration = new MovieRecyclerItemDecoration(this, R.dimen.item_offset);
        rView.addItemDecoration(itemDecoration);

        mlAdapter = new MainListAdapter(MainActivity.this, mDataItems, this);
        rView.setAdapter(mlAdapter);
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
                return true;

            case R.id.action_top_rated:
                fetchTopRatedMovies();
                editor.putInt(getString(R.string.list_type_preference), TOP_RATED_MOVIES_PREF);
                editor.commit();
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

        mlAdapter.notifyDataSetChanged();
        mDialog.dismiss();
    }

    @Override
    public void onDetailsFetchCompleted(MovieDetailsItem movieDetails){
        Intent detailsIntent = new Intent(MainActivity.this, DetailsActivity.class);
        detailsIntent.putExtra(DetailsActivity.KEY_MOVIE_DETAILS, movieDetails);
        startActivity(detailsIntent);
        mDialog.dismiss();
    }

    @Override
    public void onListItemClicked(String movieId) {
        mDialog.show();
        Uri.Builder uriBuilder = new Uri.Builder();

        String url = uriBuilder.scheme(URL_SCHEME)
                .authority(BASE_URL)
                .appendPath(EXTRA_PATH_1)
                .appendPath(EXTRA_PATH_2)
                .appendPath(movieId)
                .appendQueryParameter(API_KEY_PARAMETER, MOVIE_DB_API_KEY)
                .build().toString();

        mMovieFetchFragment.fetchMovieDetailsFromUrl(url);
    }

    /**
     * Utility function to fetch popular movies
     */
    private void fetchPopularMovies(){
        Uri.Builder uriBuilder = new Uri.Builder();

        String url = uriBuilder.scheme(URL_SCHEME)
                .authority(BASE_URL)
                .appendPath(EXTRA_PATH_1)
                .appendPath(EXTRA_PATH_2)
                .appendPath(POPULAR_PATH)
                .appendQueryParameter(API_KEY_PARAMETER, MOVIE_DB_API_KEY)
                .build().toString();

        mMovieFetchFragment.fetchListFromUrl(url);

        mDialog.show();
    }

    /**
     * Utility function to fetch Top rated movies
     */
    private void fetchTopRatedMovies(){
        Uri.Builder uriBuilder = new Uri.Builder();

        String url = uriBuilder.scheme(URL_SCHEME)
                .authority(BASE_URL)
                .appendPath(EXTRA_PATH_1)
                .appendPath(EXTRA_PATH_2)
                .appendPath(TOP_RATED_PATH)
                .appendQueryParameter(API_KEY_PARAMETER, MOVIE_DB_API_KEY)
                .build().toString();

        mMovieFetchFragment.fetchListFromUrl(url);

        mDialog.show();
    }
}
