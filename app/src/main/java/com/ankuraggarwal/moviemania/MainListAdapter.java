package com.ankuraggarwal.moviemania;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ankuraggarwal.moviemania.data.MovieDataItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ankur on 6/16/2016.
 */
public class MainListAdapter extends RecyclerView.Adapter<MovieViewHolder>{

    private Context context;
    private List<MovieDataItem> mItemList;

    private static final String IMAGE_FETCH_BASE_URL = "http://image.tmdb.org/t/p/w500";

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
        final MovieDataItem currentItem = mItemList.get(position);

        holder.mMovieTitle.setText(currentItem.getMovieTitle());
        Picasso.with(context).load(IMAGE_FETCH_BASE_URL+ currentItem.getPosterPath()).into(holder.mMovieImg);

        holder.mMovieImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String movieId = currentItem.getId();

                Intent detailsIntent = new Intent(context, DetailsActivity.class);

                context.startActivity(detailsIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mItemList == null || mItemList.isEmpty()){
            return 0;
        }
        return mItemList.size();
    }


}
