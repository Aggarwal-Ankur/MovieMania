package com.ankuraggarwal.moviemania;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ankuraggarwal.moviemania.data.MovieDataItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ankur on 6/16/2016.
 */
public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MovieViewHolder>{
    public interface ListItemClickCallback{
        void onListItemClicked(String movieId);
    }

    private Context context;
    private List<MovieDataItem> mItemList;
    private ListItemClickCallback mListItemClickCallback;

    private static final String IMAGE_FETCH_BASE_URL = "http://image.tmdb.org/t/p/w500";

    Picasso picasso;

    public MainListAdapter(Context context, List<MovieDataItem> itemList, ListItemClickCallback listItemClickCallback, Picasso picasso) {
        this.mItemList = itemList;
        this.context = context;
        this.mListItemClickCallback = listItemClickCallback;
        this.picasso = picasso;
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
        picasso.load(IMAGE_FETCH_BASE_URL+ currentItem.getPosterPath()).into(holder.mMovieImg);

        holder.mMovieImg.setTag(currentItem.getId());

        holder.mMovieContainer.setTag(currentItem.getId());
    }

    @Override
    public int getItemCount() {
        if(mItemList == null || mItemList.isEmpty()){
            return 0;
        }
        return mItemList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.movie_title) public TextView mMovieTitle;
        @BindView(R.id.movie_img) public ImageView mMovieImg;
        @BindView(R.id.movie_container) public View mMovieContainer;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.movie_container)
        public void clickMovie(View v){
            mListItemClickCallback.onListItemClicked((String)v.getTag());
        }
    }


}
