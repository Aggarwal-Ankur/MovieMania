package com.ankuraggarwal.moviemania.fragments;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import com.ankuraggarwal.moviemania.R;
import com.ankuraggarwal.moviemania.data.MovieVideos;
import com.google.gson.Gson;

import java.util.List;


public class TrailerListFragment extends ListFragment implements OnItemClickListener  {

    private static final String TAG = TrailerListFragment.class.getSimpleName();

    private static final String TRAILER_LIST_JSON_KEY = "trailer_list_json";

    private String mTrailerListJson;

    private List<MovieVideos.Results> mMovieVideos;

    public TrailerListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();

        if(arguments != null){
            mTrailerListJson = arguments.getString(TRAILER_LIST_JSON_KEY);

            Gson gson = new Gson();

            mMovieVideos = gson.fromJson(mTrailerListJson, MovieVideos.class).getMovieVideoList();
        }
    }

    public static TrailerListFragment newInstance(String trailerListJson) {
        TrailerListFragment myFragment = new TrailerListFragment();

        Bundle args = new Bundle();
        args.putString(TRAILER_LIST_JSON_KEY, trailerListJson);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trailer_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CharSequence[] videoTitles = new CharSequence[mMovieVideos.size()];

        for(int i =0; i<mMovieVideos.size(); i++){
            videoTitles[i] = mMovieVideos.get(i).getName();
        }

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, videoTitles);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Get the Youtube video key
        String videoKey = mMovieVideos.get(position).getKey();

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoKey));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + videoKey));
            startActivity(intent);
        }
    }

}
