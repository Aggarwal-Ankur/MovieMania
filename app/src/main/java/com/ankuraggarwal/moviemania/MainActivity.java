package com.ankuraggarwal.moviemania;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ankuraggarwal.moviemania.data.MovieDataItem;
import com.ankuraggarwal.moviemania.data.MovieDetailsItem;
import com.ankuraggarwal.moviemania.fragments.MovieFetchFragment;

import java.util.ArrayList;
import java.util.List;

import static com.ankuraggarwal.moviemania.BuildConfig.MOVIE_DB_API_KEY;

public class MainActivity extends AppCompatActivity implements MovieFetchFragment.FetchCallbacks, MainListAdapter.ListItemClickCallback{

    private GridLayoutManager mGridManager;
    private static final String TAG_ASYNC_FRAGMENT = "async_fragment";


    /** Because this is a retained fragment and our AsyctTask is inside this, we do not need to implement onSaveInstanceState() in this activity*/
    private MovieFetchFragment mMovieFetchFragment;
    private MainListAdapter mlAdapter;
    private List<MovieDataItem> mDataItems;



    // These are the parameters to build the URL
    private static final String URL_SCHEME = "https";
    private static final String BASE_URL = "api.themoviedb.org";

    private static final String EXTRA_PATH_1 = "3";
    private static final String EXTRA_PATH_2 = "movie";
    private static final String POPULAR_PATH = "popular";
    private static final String TOP_RATED_PATH = "top_rated";

    private static final String API_KEY_PARAMETER = "api_key";

    private ProgressDialog mDialog;

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



        mDataItems = new ArrayList<>();

        List<MovieDataItem> alreadyFetchedItems = mMovieFetchFragment.getMovieList();

        if(alreadyFetchedItems != null && !alreadyFetchedItems.isEmpty()){
            mDataItems.addAll(alreadyFetchedItems);
        }

        mGridManager = new GridLayoutManager(MainActivity.this, 2);

        RecyclerView rView = (RecyclerView)findViewById(R.id.movie_recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(mGridManager);
        MovieRecyclerItemDecoration itemDecoration = new MovieRecyclerItemDecoration(this, R.dimen.item_offset);
        rView.addItemDecoration(itemDecoration);

        mlAdapter = new MainListAdapter(MainActivity.this, mDataItems, this);
        rView.setAdapter(mlAdapter);
    }

    @Override
    public void onListFetchCompleted() {
        if(mDataItems == null){
            return;
        }
        mDataItems.clear();

        mDataItems.addAll(mMovieFetchFragment.getMovieList());
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
}
