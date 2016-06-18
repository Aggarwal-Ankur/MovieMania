package com.ankuraggarwal.moviemania;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ankuraggarwal.moviemania.data.MovieDataItem;

import java.util.List;

/**
 * Created by Ankur on 6/16/2016.
 */
public class MainListAdapter extends RecyclerView.Adapter<MovieViewHolder>{

    private Context context;
    private List<MovieDataItem> mItemList;

    public MainListAdapter(Context context, List<MovieDataItem> itemList) {
        this.mItemList = itemList;
        this.context = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card_view_list, null);
        MovieViewHolder viewHolder = new MovieViewHolder(layoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.mMovieTitle.setText(mItemList.get(position).getMovieTitle());
        //holder.mMovieImg.setImageResource(mItemList.get(position).getImgResource());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }


}
