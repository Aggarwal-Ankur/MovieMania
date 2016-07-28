package com.ankuraggarwal.moviemania.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.ankuraggarwal.moviemania.data.MovieDataItem;
import com.ankuraggarwal.moviemania.data.MovieDetailsItem;
import com.ankuraggarwal.moviemania.data.MovieResults;
import com.ankuraggarwal.moviemania.provider.FavoritesContract;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Ankur on 6/17/2016.
 */

public class MovieFetchFragment extends Fragment {

    public interface FetchCallbacks{
        void onListFetchCompleted(List<MovieDataItem> movieDataItems);
        void onDetailsFetchCompleted(MovieDetailsItem movieDetails);
    }

    private static final String TAG = MovieFetchFragment.class.getSimpleName();

    private List<MovieDataItem> mMovieList = new ArrayList<>();

    private MovieListFetchTask mMovieListFetchTask;
    private MovieDetailsFetchTask mMovieDetailsFetchTask;
    private FavoritesListFetchTask mFavoritesListFetchTask;
    private FetchCallbacks mCallbackListener;

    private String listJson ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mCallbackListener = (FetchCallbacks) activity;
    }

    public void fetchListFromUrl(String url){
        if(mMovieListFetchTask != null){
            mMovieListFetchTask.cancel(true);
        }
        mMovieListFetchTask = new MovieListFetchTask();
        mMovieListFetchTask.execute(new String[]{url});
    }

    public void fetchMovieDetailsFromUrl(String url){
        if(mMovieDetailsFetchTask != null){
            mMovieDetailsFetchTask.cancel(true);
        }
        mMovieDetailsFetchTask = new MovieDetailsFetchTask();
        mMovieDetailsFetchTask.execute(new String[]{url});
    }

    public void fetchFavoritesList(){
        if(mFavoritesListFetchTask != null){
            mFavoritesListFetchTask.cancel(true);
        }
        mFavoritesListFetchTask = new FavoritesListFetchTask();
        mFavoritesListFetchTask.execute();
    }

    public void fetchMovieDetailFromDb(String movieId){
        MovieDetailsItem currentMovieDetails = null;
        try {
            //Get the data from content resolver
            Cursor cursor = getActivity().getContentResolver().query(FavoritesContract.FavoriteMovies.CONTENT_URI,
                    FavoritesContract.FavoriteMovies.ALL_COLUMNS,
                    FavoritesContract.FavoriteMovies.COLUMN_MOVIE_ID + " = " + movieId,
                    null, null, null);

            cursor.moveToFirst();
            if(!cursor.isAfterLast()){
                currentMovieDetails = new MovieDetailsItem();
                currentMovieDetails.setId(movieId);
                currentMovieDetails.setTitle(cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoriteMovies.COLUMN_TITLE)));
                currentMovieDetails.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoriteMovies.COLUMN_IMAGE)));
                currentMovieDetails.setOverview(cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoriteMovies.COLUMN_DESCRIPTION)));
                currentMovieDetails.setReleaseDate(cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoriteMovies.COLUMN_RELEASE_DATE)));
                currentMovieDetails.setVoteAverage(cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoriteMovies.COLUMN_RATING)));

            }

            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(mCallbackListener != null){
            mCallbackListener.onDetailsFetchCompleted(currentMovieDetails);
        }
    }

    public String getListJson(){
        return listJson;
    }

    public List<MovieDataItem> getMovieList(){
        return mMovieList;
    }

    private class MovieListFetchTask extends AsyncTask<String, Void, List<MovieDataItem>>{

        private OkHttpClient client;

        @Override
        protected List<MovieDataItem> doInBackground(String... params) {
            if(params == null || params[0] == null || params[0].isEmpty()){
                return null;
            }

            client = new OkHttpClient();

            try {
                Request request = new Request.Builder()
                        .url(params[0])
                        .build();
                Response response = client.newCall(request).execute();

                String responseJson = response.body().string();

                listJson = responseJson;

                Gson gson = new Gson();

                mMovieList = gson.fromJson(responseJson, MovieResults.class).getResults();

                Log.d(TAG, "List Json = " + responseJson);

                return mMovieList;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<MovieDataItem> movieDataItems) {
            super.onPostExecute(movieDataItems);

            if(isCancelled()){
                return;
            }

            if(mCallbackListener != null){
                mCallbackListener.onListFetchCompleted(movieDataItems);
            }
        }
    }

    private class FavoritesListFetchTask extends AsyncTask<Void, Void, List<MovieDataItem>>{


        @Override
        protected List<MovieDataItem> doInBackground(Void... params) {

            try {

                //Get the data from content resolver
                Cursor cursor = getActivity().getContentResolver().query(FavoritesContract.FavoriteMovies.CONTENT_URI,
                        FavoritesContract.FavoriteMovies.ALL_COLUMNS,
                        FavoritesContract.FavoriteMovies.COLUMN_IS_FAVORITE + " = " + 1,
                        null, null, null);

                mMovieList.clear();
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    MovieDataItem currentMovieItem = new MovieDataItem();

                    currentMovieItem.setMovieTitle(cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoriteMovies.COLUMN_TITLE)));

                    currentMovieItem.setId(cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoriteMovies.COLUMN_MOVIE_ID)));

                    currentMovieItem.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoriteMovies.COLUMN_IMAGE)));

                    mMovieList.add(currentMovieItem);

                    cursor.moveToNext();
                }

                cursor.close();

                return mMovieList;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<MovieDataItem> movieDataItems) {
            super.onPostExecute(movieDataItems);

            if(isCancelled()){
                return;
            }

            if(mCallbackListener != null){
                mCallbackListener.onListFetchCompleted(movieDataItems);
            }
        }
    }

    private class MovieDetailsFetchTask extends AsyncTask<String, Void, MovieDetailsItem>{

        private OkHttpClient client;

        @Override
        protected MovieDetailsItem doInBackground(String... params) {
            if(params == null || params[0] == null || params[0].isEmpty()){
                return null;
            }

            client = new OkHttpClient();

            try {
                Request request = new Request.Builder()
                        .url(params[0])
                        .build();
                Response response = client.newCall(request).execute();

                if(response.code() != 200){
                    return null;
                }

                String responseJson = response.body().string();

                Gson gson = new Gson();

                MovieDetailsItem mMovieDetails = gson.fromJson(responseJson, MovieDetailsItem.class);

                Log.d(TAG, "Details Json = " + responseJson);

                return mMovieDetails;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieDetailsItem movieDetailsItem) {
            super.onPostExecute(movieDetailsItem);

            if(isCancelled()){
                return;
            }

            if(mCallbackListener != null){
                mCallbackListener.onDetailsFetchCompleted(movieDetailsItem);
            }
        }
    }
}
