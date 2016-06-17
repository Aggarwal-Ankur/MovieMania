package com.ankuraggarwal.moviemania;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ankuraggarwal.moviemania.data.MovieDataItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GridLayoutManager mGridManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
