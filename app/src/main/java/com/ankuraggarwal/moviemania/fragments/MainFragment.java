package com.ankuraggarwal.moviemania.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ankuraggarwal.moviemania.MainActivity;
import com.ankuraggarwal.moviemania.MainListAdapter;
import com.ankuraggarwal.moviemania.MovieRecyclerItemDecoration;
import com.ankuraggarwal.moviemania.R;
import com.ankuraggarwal.moviemania.data.MovieDataItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnMovieSelectedListener} interface
 * to handle interaction events.
 */
public class MainFragment extends Fragment implements MainListAdapter.ListItemClickCallback{


    private OnMovieSelectedListener mListener;

    private MainListAdapter mlAdapter;
    private List<MovieDataItem> mDataItems;

    public MainFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataItems = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);

        RecyclerView rView = (RecyclerView)rootView.findViewById(R.id.movie_recycler_view);
        rView.setHasFixedSize(true);
        MovieRecyclerItemDecoration itemDecoration = new MovieRecyclerItemDecoration(getActivity(), R.dimen.item_offset);
        rView.addItemDecoration(itemDecoration);

        mlAdapter = new MainListAdapter(getActivity(), mDataItems, this);
        rView.setAdapter(mlAdapter);

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMovieSelectedListener) {
            mListener = (OnMovieSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMovieSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClicked(String movieId) {
        if(mListener != null){
            mListener.onMovieSelected(movieId);
        }
    }

    public void updateMovieList(List<MovieDataItem> movieList){
        if(mDataItems != null && movieList != null){
            mDataItems.clear();

            mDataItems.addAll(movieList);

            mlAdapter.notifyDataSetChanged();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMovieSelectedListener {
        void onMovieSelected(String movieId);
    }
}
