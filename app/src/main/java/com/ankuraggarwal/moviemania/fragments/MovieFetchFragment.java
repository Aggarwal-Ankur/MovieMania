package com.ankuraggarwal.moviemania.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.ankuraggarwal.moviemania.data.MovieDataItem;
import com.ankuraggarwal.moviemania.data.MovieResults;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Ankur on 6/17/2016.
 */

public class MovieFetchFragment extends Fragment {

    private static final String TAG = MovieFetchFragment.class.getSimpleName();

    private MovieFetchTask mMovieFetchTask;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setRetainInstance(true);
    }

    public void fetchDataFromUrl ( String url){
        if(mMovieFetchTask != null){
            mMovieFetchTask.cancel(true);
        }
        mMovieFetchTask = new MovieFetchTask();
        mMovieFetchTask.execute(new String[]{url});
    }

    private class MovieFetchTask extends AsyncTask<String, Void, List<MovieDataItem>>{
        private List<MovieDataItem> mMovieList;
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

                Gson gson = new Gson();

                mMovieList = gson.fromJson(responseJson, MovieResults.class).getResults();

                Log.d(TAG, "response Json = " + responseJson);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<MovieDataItem> movieDataItems) {
            super.onPostExecute(movieDataItems);
        }
    }
}
