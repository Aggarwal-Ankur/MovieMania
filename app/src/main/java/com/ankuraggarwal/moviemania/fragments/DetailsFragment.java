package com.ankuraggarwal.moviemania.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ankuraggarwal.moviemania.DetailsActivity;
import com.ankuraggarwal.moviemania.R;
import com.ankuraggarwal.moviemania.data.MovieDetailsItem;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFavoritesSelectedListener} interface
 * to handle interaction events.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    private ImageView mPosterImage;

    private TextView mSynopsisTv, mRatingTextView, mReleaseDateTextView;

    private static final String IMAGE_FETCH_BASE_URL = "http://image.tmdb.org/t/p/w500";

    private MovieDetailsItem mMovieDetails;

    private OnFavoritesSelectedListener mListener;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            mMovieDetails = getArguments().getParcelable(DetailsActivity.KEY_MOVIE_DETAILS);
        }
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.content_details, container, false);
        mPosterImage = (ImageView) rootView.findViewById(R.id.imageView);
        mSynopsisTv = (TextView) rootView.findViewById(R.id.synopsis);
        mRatingTextView = (TextView) rootView.findViewById(R.id.rating);
        mReleaseDateTextView = (TextView) rootView.findViewById(R.id.release_date);

        refreshUI();

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int movieId, boolean selected) {
        if (mListener != null) {
            mListener.onFavoriteSelected(movieId, selected);
        }
    }

    public void updateMovieDetails(MovieDetailsItem movieDetails){
        this.mMovieDetails = movieDetails;
        refreshUI();
    }

    private void refreshUI(){
        if(mMovieDetails != null){
            //setTitle(movieDetails.getTitle());

            mSynopsisTv.setText(mMovieDetails.getOverview());
            mRatingTextView.setText(getResources().getString(R.string.details_user_rating)+ " : "+ mMovieDetails.getVoteAverage());
            mReleaseDateTextView.setText(getResources().getString(R.string.release_date)+ " : "+ mMovieDetails.getReleaseDate());

            Picasso.with(getActivity()).load(IMAGE_FETCH_BASE_URL+ mMovieDetails.getPosterPath()).into(mPosterImage);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFavoritesSelectedListener) {
            mListener = (OnFavoritesSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFavoritesSelectedListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFavoritesSelectedListener {
        // TODO: Update argument type and name
        void onFavoriteSelected(int movieId, boolean selected);
    }
}
