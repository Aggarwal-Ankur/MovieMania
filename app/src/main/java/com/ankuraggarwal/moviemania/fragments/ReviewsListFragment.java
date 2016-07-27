package com.ankuraggarwal.moviemania.fragments;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import com.ankuraggarwal.moviemania.R;
import com.ankuraggarwal.moviemania.data.MovieReviews;
import com.ankuraggarwal.moviemania.data.MovieVideos;
import com.google.gson.Gson;

import java.util.List;


public class ReviewsListFragment extends ListFragment implements OnItemClickListener  {

    private static final String TAG = ReviewsListFragment.class.getSimpleName();

    private static final String REVIEW_LIST_JSON_KEY = "trailer_list_json";

    private String mReviewListJson;

    private List<MovieReviews.Results> mMovieReviews;

    public ReviewsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();

        if(arguments != null){
            mReviewListJson = arguments.getString(REVIEW_LIST_JSON_KEY);

            Gson gson = new Gson();

            mMovieReviews = gson.fromJson(mReviewListJson, MovieReviews.class).getMovieReviewList();
        }
    }

    public static ReviewsListFragment newInstance(String trailerListJson) {
        ReviewsListFragment myFragment = new ReviewsListFragment();

        Bundle args = new Bundle();
        args.putString(REVIEW_LIST_JSON_KEY, trailerListJson);
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

        CharSequence[] reviewAuthors = new CharSequence[mMovieReviews.size()];

        for(int i =0; i<mMovieReviews.size(); i++){
            reviewAuthors[i] = "Review by" + mMovieReviews.get(i).getAuthor();
        }

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, reviewAuthors);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Get the Youtube video key
        /*String videoKey = mMovieReviews.get(position).getKey();

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoKey));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + videoKey));
            startActivity(intent);
        }*/
    }

}
