package com.ankuraggarwal.moviemania.fragments;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ankuraggarwal.moviemania.DetailsActivity;
import com.ankuraggarwal.moviemania.IConstants;
import com.ankuraggarwal.moviemania.R;
import com.ankuraggarwal.moviemania.data.MovieDataItem;
import com.ankuraggarwal.moviemania.data.MovieDetailsItem;
import com.ankuraggarwal.moviemania.data.MovieResults;
import com.ankuraggarwal.moviemania.data.MovieReviews;
import com.ankuraggarwal.moviemania.data.MovieVideos;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.ankuraggarwal.moviemania.BuildConfig.MOVIE_DB_API_KEY;

public class DetailsFragment extends Fragment {

    private static String TAG = DetailsFragment.class.getSimpleName();

    private ImageView mPosterImage;

    private TextView mSynopsisTv, mRatingTextView, mReleaseDateTextView;

    private static final String IMAGE_FETCH_BASE_URL = "http://image.tmdb.org/t/p/w500";

    private MovieDetailsItem mMovieDetails;

    private OnFavoritesSelectedListener mListener;

    private ProgressDialog mDialog;

    private VideoListFetchTask mVideoListFetchTask;
    private ReviewListFetchTask mReviewListFetchTask;

    private LinearLayout mButtonsLayout;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Build the dialog
        mDialog = new ProgressDialog(getActivity());
        mDialog.setIndeterminate(true);
        mDialog.setMessage("Fetching Data...");
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


        mButtonsLayout = (LinearLayout) rootView.findViewById(R.id.details_button_layout);

        Button showVideosButton = (Button) rootView.findViewById(R.id.show_videos_button);
        showVideosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
                Uri.Builder uriBuilder = new Uri.Builder();

                String url = uriBuilder.scheme(IConstants.URL_SCHEME)
                        .authority(IConstants.BASE_URL)
                        .appendPath(IConstants.EXTRA_PATH_1)
                        .appendPath(IConstants.EXTRA_PATH_2)
                        .appendPath(mMovieDetails.getId())
                        .appendPath(IConstants.VIDEOS_PATH)
                        .appendQueryParameter(IConstants.API_KEY_PARAMETER, MOVIE_DB_API_KEY)
                        .build().toString();

                if(mVideoListFetchTask != null){
                    mVideoListFetchTask.cancel(true);
                }
                mVideoListFetchTask = new VideoListFetchTask();
                mVideoListFetchTask.execute(new String[]{url});
            }
        });

        Button showReviewsButton = (Button) rootView.findViewById(R.id.show_reviews_button);
        showReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
                Uri.Builder uriBuilder = new Uri.Builder();

                String url = uriBuilder.scheme(IConstants.URL_SCHEME)
                        .authority(IConstants.BASE_URL)
                        .appendPath(IConstants.EXTRA_PATH_1)
                        .appendPath(IConstants.EXTRA_PATH_2)
                        .appendPath(mMovieDetails.getId())
                        .appendPath(IConstants.REVIEWS_PATH)
                        .appendQueryParameter(IConstants.API_KEY_PARAMETER, MOVIE_DB_API_KEY)
                        .build().toString();

                if(mReviewListFetchTask != null){
                    mReviewListFetchTask.cancel(true);
                }
                mReviewListFetchTask = new ReviewListFetchTask();
                mReviewListFetchTask.execute(new String[]{url});
            }
        });

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
            mSynopsisTv.setVisibility(View.VISIBLE);
            mRatingTextView.setVisibility(View.VISIBLE);
            mReleaseDateTextView.setVisibility(View.VISIBLE);
            mPosterImage.setVisibility(View.VISIBLE);

            mSynopsisTv.setText(mMovieDetails.getOverview());
            mRatingTextView.setText(getResources().getString(R.string.details_user_rating)+ " : "+ mMovieDetails.getVoteAverage());
            mReleaseDateTextView.setText(getResources().getString(R.string.release_date)+ " : "+ mMovieDetails.getReleaseDate());

            Picasso.with(getActivity()).load(IMAGE_FETCH_BASE_URL+ mMovieDetails.getPosterPath()).into(mPosterImage);

            mButtonsLayout.setVisibility(View.VISIBLE);

            //Remove old transactions
            FragmentManager fm = getChildFragmentManager();
            int backstackCount = fm.getBackStackEntryCount();
            for(int i = 0; i < backstackCount; ++i) {
                fm.popBackStack();
            }
        }else{
            mSynopsisTv.setVisibility(View.INVISIBLE);
            mRatingTextView.setVisibility(View.INVISIBLE);
            mReleaseDateTextView.setVisibility(View.INVISIBLE);
            mPosterImage.setVisibility(View.INVISIBLE);

            mButtonsLayout.setVisibility(View.INVISIBLE);

            return;
        }

    }

    @Override
    public void onDestroy() {
        if(mVideoListFetchTask != null){
            mVideoListFetchTask.cancel(true);
        }
        super.onDestroy();
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

    public boolean handleBackPressed(){
        if(getChildFragmentManager().getBackStackEntryCount() > 0){
            getChildFragmentManager().popBackStack();
            return true;
        }else{
            return false;
        }
    }

    public interface OnFavoritesSelectedListener {
        // TODO: Update argument type and name
        void onFavoriteSelected(int movieId, boolean selected);
    }


    private class VideoListFetchTask extends AsyncTask<String, Void, List<MovieVideos.Results>> {
        private OkHttpClient client;
        private String responseJson;

        private List<MovieVideos.Results> mMovieVideos;

        @Override
        protected List<MovieVideos.Results> doInBackground(String... params) {
            if(params == null || params[0] == null || params[0].isEmpty()){
                return null;
            }

            client = new OkHttpClient();

            try {
                Request request = new Request.Builder()
                        .url(params[0])
                        .build();
                Response response = client.newCall(request).execute();

                responseJson = response.body().string();

                Gson gson = new Gson();
                mMovieVideos = gson.fromJson(responseJson, MovieVideos.class).getMovieVideoList();
                Log.d(TAG, "List Json = " + responseJson);

                mDialog.dismiss();
                return mMovieVideos;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<MovieVideos.Results> movieDataItems) {
            super.onPostExecute(movieDataItems);
            if(isCancelled() || mMovieVideos == null || mMovieVideos.isEmpty()){
                return;
            }

            TrailerListFragment trailerListFragment = TrailerListFragment.newInstance(responseJson);
            getChildFragmentManager().beginTransaction()
                    .add(R.id.details_fragment_placeholder, trailerListFragment)
                    .addToBackStack("trailers_fragment")
                    .commit();

        }
    }

    private class ReviewListFetchTask extends AsyncTask<String, Void, List<MovieReviews.Results>> {
        private OkHttpClient client;
        private String responseJson;

        private List<MovieReviews.Results> mMovieReviews;

        @Override
        protected List<MovieReviews.Results> doInBackground(String... params) {
            if (params == null || params[0] == null || params[0].isEmpty()) {
                return null;
            }

            client = new OkHttpClient();

            try {
                Request request = new Request.Builder()
                        .url(params[0])
                        .build();
                Response response = client.newCall(request).execute();

                responseJson = response.body().string();

                Gson gson = new Gson();
                mMovieReviews = gson.fromJson(responseJson, MovieReviews.class).getMovieReviewList();
                Log.d(TAG, "List Json = " + responseJson);

                mDialog.dismiss();
                return mMovieReviews;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<MovieReviews.Results> movieDataItems) {
            super.onPostExecute(movieDataItems);
            if (isCancelled() || mMovieReviews == null || mMovieReviews.isEmpty()) {
                return;
            }

            ReviewsListFragment reviewListFragment = ReviewsListFragment.newInstance(responseJson);
            getChildFragmentManager().beginTransaction()
                    .add(R.id.details_fragment_placeholder, reviewListFragment)
                    .addToBackStack("review_list_fragment")
                    .commit();
        }
    }
}
