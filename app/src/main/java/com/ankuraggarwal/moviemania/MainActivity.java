package com.ankuraggarwal.moviemania;

import android.app.FragmentManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ankuraggarwal.moviemania.data.MovieDataItem;
import com.ankuraggarwal.moviemania.fragments.MovieFetchFragment;

import java.util.ArrayList;
import java.util.List;

import static com.ankuraggarwal.moviemania.BuildConfig.MOVIE_DB_API_KEY;

public class MainActivity extends AppCompatActivity {

    private GridLayoutManager mGridManager;
    private static final String TAG_ASYNC_FRAGMENT = "async_fragment";

    private MovieFetchFragment mMovieFetchFragment;



    // These are the parameters to build the URL
    private static final String URL_SCHEME = "https";
    private static final String BASE_URL = "api.themoviedb.org";

    private static final String EXTRA_PATH_1 = "3";
    private static final String EXTRA_PATH_2 = "movie";
    private static final String POPULAR_PATH = "popular";
    private static final String TOP_RATED_PATH = "top_rated";

    private static final String API_KEY_PARAMETER = "api_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();
        mMovieFetchFragment = (MovieFetchFragment) fm.findFragmentByTag(TAG_ASYNC_FRAGMENT);

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



            mMovieFetchFragment.fetchDataFromUrl(url);
        }


        List<MovieDataItem> rowListItem = getDummyData();
        mGridManager = new GridLayoutManager(MainActivity.this, 4);

        RecyclerView rView = (RecyclerView)findViewById(R.id.movie_recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(mGridManager);
        MovieRecyclerItemDecoration itemDecoration = new MovieRecyclerItemDecoration(this, R.dimen.item_offset);
        rView.addItemDecoration(itemDecoration);

        MainListAdapter mlAdapter = new MainListAdapter(MainActivity.this, rowListItem);
        rView.setAdapter(mlAdapter);
    }


    private List<MovieDataItem> getDummyData(){
        List<MovieDataItem> allItems = new ArrayList<MovieDataItem>();
        allItems.add(new MovieDataItem("United States", R.mipmap.ic_launcher));
        allItems.add(new MovieDataItem("Canada", R.mipmap.ic_launcher));
        allItems.add(new MovieDataItem("United Kingdom", R.mipmap.ic_launcher));
        allItems.add(new MovieDataItem("Germany", R.mipmap.ic_launcher));
        allItems.add(new MovieDataItem("United States", R.mipmap.ic_launcher));
        allItems.add(new MovieDataItem("Canada", R.mipmap.ic_launcher));
        allItems.add(new MovieDataItem("United Kingdom", R.mipmap.ic_launcher));
        allItems.add(new MovieDataItem("Germany", R.mipmap.ic_launcher));

        return allItems;

    }
}
