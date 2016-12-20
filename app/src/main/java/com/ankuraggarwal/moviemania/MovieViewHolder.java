package com.ankuraggarwal.moviemania;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ankur on 6/16/2016.
 *
 * //This is no longer used
 */
public class MovieViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.movie_title) public TextView mMovieTitle;
    @BindView(R.id.movie_img) public ImageView mMovieImg;
    @BindView(R.id.movie_container) public View mMovieContainer;

    public MovieViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


}
