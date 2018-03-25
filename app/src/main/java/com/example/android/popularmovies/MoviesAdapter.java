package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Sylvana on 11/28/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {
    private final Context mContext;
    private Cursor mMoviesDataCursor;
    private final MoviesAdapterOnClickHandler mClickHandler;


    public void setMovieDataCursor(Cursor movieData){
        mMoviesDataCursor = movieData;
        notifyDataSetChanged();
    }
    public interface MoviesAdapterOnClickHandler{
        void onClick(int movieAdapterPosition);
    }
    public MoviesAdapter(@NonNull Context context, MoviesAdapterOnClickHandler clickHandler){
        mContext = context;
        mClickHandler = clickHandler;
    }
    public int getItemCount(){
        if(mMoviesDataCursor == null) return 0;
        return mMoviesDataCursor.getCount();
    }
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        int layoutIdForItem = R.layout.movie_poster_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForItem,parent,shouldAttachToParentImmediately);
        view.setFocusable(true);
        return new MoviesAdapterViewHolder(view);
    }
    @Override
    public void onBindViewHolder (MoviesAdapterViewHolder moviesAdapterViewHolder, int position){
        mMoviesDataCursor.moveToPosition(position);
        String moviePosterPath = mMoviesDataCursor.getString(3);
        Picasso.with(moviesAdapterViewHolder.mMovieImageView.getContext()).load("http://image.tmdb.org/t/p/w500" + moviePosterPath).into(moviesAdapterViewHolder.mMovieImageView);
    }
    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener{
        public final ImageView mMovieImageView;
        public MoviesAdapterViewHolder(View itemView){
            super(itemView);
            mMovieImageView = itemView.findViewById(R.id.poster_image_view);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view){
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }

}