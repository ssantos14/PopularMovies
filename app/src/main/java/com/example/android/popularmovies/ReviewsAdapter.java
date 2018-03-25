package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Sylvana on 1/14/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder> {
    private String[] mReviews;

    public void setReviewsData(String[] reviews){
        mReviews = reviews;
        notifyDataSetChanged();
    }

    public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mReviewTextView;
        public ReviewsAdapterViewHolder(View itemView){
            super(itemView);
            mReviewTextView =  itemView.findViewById(R.id.review_text_view);
        }
    }

    @Override
    public ReviewsAdapter.ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForItem,parent,false);
        return new ReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ReviewsAdapterViewHolder holder, int position) {
        holder.mReviewTextView.setText(mReviews[position]);
    }

    @Override
    public int getItemCount() {
        if(mReviews == null) return 0;
        return mReviews.length;
    }

}

