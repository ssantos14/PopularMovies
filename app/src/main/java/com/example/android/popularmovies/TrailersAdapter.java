package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Sylvana on 1/12/2018.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersAdapterViewHolder> {

    private String[] mTrailerKeys;
    private final TrailersAdapterOnClickHandler mClickHandler;

    public void setTrailerKeysData(String[] trailerKeys){
        mTrailerKeys = trailerKeys;
        notifyDataSetChanged();
    }
    public interface TrailersAdapterOnClickHandler{
        void onClick(String trailerKey);
    }
    public TrailersAdapter(TrailersAdapter.TrailersAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }
    public class TrailersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTrailerLabelTextView;
        public TrailersAdapterViewHolder(View itemView){
            super(itemView);
            mTrailerLabelTextView =  itemView.findViewById(R.id.trailer_label);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view){
            int adapterPosition = getAdapterPosition();
            String trailerKey = mTrailerKeys[adapterPosition];
            mClickHandler.onClick(trailerKey);
        }
    }
    @Override
    public TrailersAdapter.TrailersAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForItem = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForItem,parent,false);
        return new TrailersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersAdapter.TrailersAdapterViewHolder holder, int position) {
        String trailerNumber = String.valueOf(position + 1);
        holder.mTrailerLabelTextView.setText("Trailer " + trailerNumber);
    }

    @Override
    public int getItemCount() {
        if(mTrailerKeys == null) return 0;
        return mTrailerKeys.length;
    }

}
