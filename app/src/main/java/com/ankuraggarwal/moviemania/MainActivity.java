package com.ankuraggarwal.moviemania;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ankuraggarwal.moviemania.data.MovieDataItem;
import com.ankuraggarwal.moviemania.fragments.MovieFetchFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GridLayoutManager mGridManager;
    private static final String TAG_ASYNC_FRAGMENT = "async_fragment";

    private MovieFetchFragment mMovieFetchFragment;

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
        }


        List<MovieDataItem> rowListItem = getDummyData();
        mGridManager = new GridLayoutManager(MainActivity.this, 4);

        RecyclerView rView = (RecyclerView)findViewById(R.id.movie_recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(mGridManager);
        MovieRecyclerItemDecoration itemDecoration = new MovieRecyclerItemDecoration(this, R.dimen.item_offset);
        rView.addItemDecoration(itemDecoration);

        MainListAdapter rcAdapter = new MainListAdapter(MainActivity.this, rowListItem);
        rView.setAdapter(rcAdapter);
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
