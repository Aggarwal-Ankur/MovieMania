package com.ankuraggarwal.moviemania.fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ankuraggarwal.moviemania.DetailsActivity;
import com.ankuraggarwal.moviemania.IConstants;
import com.ankuraggarwal.moviemania.R;
import com.ankuraggarwal.moviemania.data.MovieDetailsItem;
import com.ankuraggarwal.moviemania.data.MovieReviews;
import com.ankuraggarwal.moviemania.data.MovieVideos;
import com.ankuraggarwal.moviemania.provider.FavoritesContract;
import com.ankuraggarwal.moviemania.provider.FavoritesProvider;
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

    private TextView mTitleTv, mSynopsisTv, mRatingTextView, mReleaseDateTextView;

    private static final String IMAGE_FETCH_BASE_URL = "http://image.tmdb.org/t/p/w500";

    private MovieDetailsItem mMovieDetails;

    private OnFavoritesSelectedListener mListener;

    private ProgressDialog mDialog;

    private VideoListFetchTask mVideoListFetchTask;
    private ReviewListFetchTask mReviewListFetchTask;

    private LinearLayout mButtonsLayout;

    private static final String REVIEW_LIST_FRAGMENT_TAG = "review_list_fragment";
    private static final String TRAILER_LIST_FRAGMENT_TAG = "trailers_fragment";

    private Button favoriteButton;

    private boolean isEntryInDb = false;
    private boolean isFavorite = false;

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
        mTitleTv = (TextView) rootView.findViewById(R.id.movie_title);
        mPosterImage = (ImageView) rootView.findViewById(R.id.imageView);
        mSynopsisTv = (TextView) rootView.findViewById(R.id.synopsis);
        mRatingTextView = (TextView) rootView.findViewById(R.id.rating);
        mReleaseDateTextView = (TextView) rootView.findViewById(R.id.release_date);


        mButtonsLayout = (LinearLayout) rootView.findViewById(R.id.details_button_layout);

        //Get the three buttons and attach their respective click listeners
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

        favoriteButton = (Button) rootView.findViewById(R.id.add_to_favorites_button);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavoritesButtonPressed();
            }
        });


        refreshUI();

        return rootView;
    }

    /**
     * Helper function for favoriting and unfavoriting
     */
    public void onFavoritesButtonPressed() {
        favoriteButton.setEnabled(false);

        checkDB();

        if(!isEntryInDb){
            // Add to favorites
            ContentValues values = new ContentValues();
            values.put(FavoritesContract.FavoriteMovies.COLUMN_MOVIE_ID, mMovieDetails.getId());
            values.put(FavoritesContract.FavoriteMovies.COLUMN_TITLE, mMovieDetails.getTitle());
            values.put(FavoritesContract.FavoriteMovies.COLUMN_DESCRIPTION, mMovieDetails.getOverview());
            values.put(FavoritesContract.FavoriteMovies.COLUMN_IMAGE, mMovieDetails.getPosterPath());
            values.put(FavoritesContract.FavoriteMovies.COLUMN_RATING, mMovieDetails.getVoteAverage());
            values.put(FavoritesContract.FavoriteMovies.COLUMN_RELEASE_DATE, mMovieDetails.getReleaseDate());
            values.put(FavoritesContract.FavoriteMovies.COLUMN_IS_FAVORITE, 1);
            Uri uri = getActivity().getContentResolver().insert(FavoritesContract.FavoriteMovies.CONTENT_URI, values);

            isFavorite = true;

            Log.d(TAG, "Insert Uri = "+ uri.toString());
        }else{
            isFavorite = !isFavorite;

            int favoriteValue = isFavorite ? 1: 0;

            //Update the value
            ContentValues values = new ContentValues();
            values.put(FavoritesContract.FavoriteMovies.COLUMN_IS_FAVORITE, favoriteValue);

            int updatedRows = getActivity().getContentResolver().update(FavoritesContract.FavoriteMovies.CONTENT_URI,
                    values,
                    FavoritesContract.FavoriteMovies.COLUMN_MOVIE_ID + " = " + mMovieDetails.getId(),
                    null);

            Log.d(TAG, "Update rows = "+ updatedRows);
        }

        refreshFavoritesButton();
        favoriteButton.setEnabled(true);
    }

    /**
     * Called from the main fragment, to update the selected movie details
     * @param movieDetails
     */
    public void updateMovieDetails(MovieDetailsItem movieDetails){
        this.mMovieDetails = movieDetails;
        refreshUI();
    }

    /**
     * Utility function, useful when the trailers or reviews are displayed
     */
    public void clearBackstack(){
        //Remove old transactions
        FragmentManager fm = getChildFragmentManager();
        int backstackCount = fm.getBackStackEntryCount();
        for(int i = 0; i < backstackCount; ++i) {
            fm.popBackStack();
        }
    }

    /**
     * Function to check if movie is in db or not
     */
    private void checkDB(){
        Cursor cursor = getActivity().getContentResolver().query(FavoritesContract.FavoriteMovies.CONTENT_URI,
                FavoritesContract.FavoriteMovies.ALL_COLUMNS,
                FavoritesContract.FavoriteMovies.COLUMN_MOVIE_ID + " = " + mMovieDetails.getId(),
                null, null, null);

        cursor.moveToFirst();
        if(!cursor.isAfterLast()){
            isEntryInDb = true;

            isFavorite = (cursor.getInt(cursor.getColumnIndex(FavoritesContract.FavoriteMovies.COLUMN_IS_FAVORITE)) == 1)? true: false;
        }else{
            isEntryInDb = false;
            isFavorite = false;
        }

        cursor.close();
    }

    /**
     * Utility function to update the UI
     */
    private void refreshUI(){
        if(mMovieDetails != null){
            checkDB();
            mTitleTv.setVisibility(View.VISIBLE);
            mSynopsisTv.setVisibility(View.VISIBLE);
            mRatingTextView.setVisibility(View.VISIBLE);
            mReleaseDateTextView.setVisibility(View.VISIBLE);
            mPosterImage.setVisibility(View.VISIBLE);

            mTitleTv.setText(mMovieDetails.getTitle());
            mSynopsisTv.setText(mMovieDetails.getOverview());
            mRatingTextView.setText(getResources().getString(R.string.details_user_rating)+ " : "+ mMovieDetails.getVoteAverage());
            mReleaseDateTextView.setText(getResources().getString(R.string.release_date)+ " : "+ mMovieDetails.getReleaseDate());

            Picasso.with(getActivity()).load(IMAGE_FETCH_BASE_URL+ mMovieDetails.getPosterPath()).into(mPosterImage);

            mButtonsLayout.setVisibility(View.VISIBLE);

            refreshFavoritesButton();
        }else{
            mTitleTv.setVisibility(View.INVISIBLE);
            mSynopsisTv.setVisibility(View.INVISIBLE);
            mRatingTextView.setVisibility(View.INVISIBLE);
            mReleaseDateTextView.setVisibility(View.INVISIBLE);
            mPosterImage.setVisibility(View.INVISIBLE);

            mButtonsLayout.setVisibility(View.INVISIBLE);

            return;
        }

    }

    /**
     * This function toggles the favorite button text (Favorite/ Un-Favorite)
     */
    private void refreshFavoritesButton(){
        if(isFavorite){
            favoriteButton.setText(getResources().getString(R.string.remove_from_favs));
        }else{
            favoriteButton.setText(getResources().getString(R.string.add_to_favs));
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

        android.support.v4.app.FragmentManager childFragmentManager = getChildFragmentManager();
        if(childFragmentManager.getBackStackEntryCount() > 0){
            Fragment reviewListFragment = childFragmentManager.findFragmentByTag(REVIEW_LIST_FRAGMENT_TAG);
            if(!(reviewListFragment != null && ((ReviewsListFragment)reviewListFragment).handleBackPressed())){
                childFragmentManager.popBackStack();
            }
            return true;
        }else{
            return false;
        }
    }

    public interface OnFavoritesSelectedListener {
        void onFavoriteSelected(int movieId, boolean selected);
    }


    /**
     * Utility Asynctask to fetch the Trailers
     */
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
            if(isCancelled()){
                return;
            }

            if(mMovieVideos == null || mMovieVideos.size()< 1){
                Toast.makeText(getActivity(), getResources().getString(R.string.error_no_trailers), Toast.LENGTH_SHORT).show();
            }else{
                TrailerListFragment trailerListFragment = TrailerListFragment.newInstance(responseJson);
                getChildFragmentManager().beginTransaction()
                        .add(R.id.details_fragment_placeholder, trailerListFragment, TRAILER_LIST_FRAGMENT_TAG)
                        .addToBackStack(TRAILER_LIST_FRAGMENT_TAG)
                        .commit();
            }
        }
    }

    /**
     * Utility Asynctask to fetch the reviews
     */
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
            if (isCancelled()) {
                return;
            }

            if(mMovieReviews == null || mMovieReviews.size()< 1){
                Toast.makeText(getActivity(), getResources().getString(R.string.error_no_reviews), Toast.LENGTH_SHORT).show();
            }else{
                ReviewsListFragment reviewListFragment = ReviewsListFragment.newInstance(responseJson);
                getChildFragmentManager().beginTransaction()
                        .add(R.id.details_fragment_placeholder, reviewListFragment, REVIEW_LIST_FRAGMENT_TAG)
                        .addToBackStack(REVIEW_LIST_FRAGMENT_TAG)
                        .commit();
            }
        }
    }
}
