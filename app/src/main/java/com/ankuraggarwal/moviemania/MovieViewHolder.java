package com.ankuraggarwal.moviemania;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Ankur on 6/16/2016.
 */
public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView mMovieTitle;
    public ImageView mMovieImg;

    public MovieViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mMovieTitle = (TextView)itemView.findViewById(R.id.movie_title);
        mMovieImg = (ImageView)itemView.findViewById(R.id.movie_img);
    }

    @Override
    public void onClick(View v) {

    }
}
