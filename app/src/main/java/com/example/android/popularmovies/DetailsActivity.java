package com.example.android.popularmovies;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.data.MoviesDataContract;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.utilities.OpenMovieJsonUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

import static com.example.android.popularmovies.MainActivity.MOVIES_PROJECTION;

public class DetailsActivity extends AppCompatActivity implements TrailersAdapter.TrailersAdapterOnClickHandler,LoaderManager.LoaderCallbacks<Cursor> {

    private TextView mMovieInfoTextView;
    private ImageView mMoviePoster;
    private TextView mMovieTitle;
    private Button mFavoritesButton;
    private String movieId;
    private String moviePosterPath;
    private String movieDescription;
    private String movieTitle;
    private RecyclerView mTrailersRecyclerView;
    private TrailersAdapter mTrailersAdapter;
    private RecyclerView mReviewsRecyclerView;
    private ReviewsAdapter mReviewsAdapter;
    private TextView mTrailersLabelTextView;
    private TextView mReviewsLabelTextView;
    private static final int MOVIE_DETAILS_LOADER_ID = 88;
    private Uri movieUri;
    private LinearLayoutManager trailersLayoutManager;
    private LinearLayoutManager reviewsLayoutManager;
    private String TRAILER_LIST_STATE_KEY = "trailers_state";
    private String REVIEW_LIST_STATE_KEY = "reviews_state";
    private Parcelable trailerSavedState;
    private Parcelable reviewsSavedState;
    private NestedScrollView detailActivityScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        detailActivityScrollView = findViewById(R.id.details_activity_scrollview);
        mMovieInfoTextView = findViewById(R.id.movie_info);
        mMoviePoster = findViewById(R.id.movie_poster);
        mMovieTitle = findViewById(R.id.movie_title);
        mFavoritesButton = findViewById(R.id.add_to_favorites_button);
        mTrailersLabelTextView = findViewById(R.id.trailers_label);
        mReviewsLabelTextView = findViewById(R.id.reviews_label);
        mTrailersRecyclerView = findViewById(R.id.trailers_recycler_view);
        trailersLayoutManager = new LinearLayoutManager(this);
        mTrailersRecyclerView.setLayoutManager(trailersLayoutManager);
        mTrailersRecyclerView.setHasFixedSize(true);
        mTrailersAdapter = new TrailersAdapter(this);
        mTrailersRecyclerView.setAdapter(mTrailersAdapter);
        mReviewsRecyclerView = findViewById(R.id.reviews_recycler_view);
        reviewsLayoutManager = new LinearLayoutManager(this);
        mReviewsRecyclerView.setLayoutManager(reviewsLayoutManager);
        mReviewsRecyclerView.setHasFixedSize(true);
        mReviewsAdapter = new ReviewsAdapter();
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);
        Intent startThisActivityIntent = getIntent();
        movieUri = startThisActivityIntent.getData();
        if (movieUri == null) throw new NullPointerException("URI for DetailsActivity cannot be null");
        getLoaderManager().initLoader(MOVIE_DETAILS_LOADER_ID,null,this);
    }

    @Override
    public void onClick(String trailerKey){
        URL movieYoutubeUrl = NetworkUtils.buildYoutubeTrailerUrl(trailerKey);
        Uri trailerUri = Uri.parse(movieYoutubeUrl.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW,trailerUri);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    public class FetchTrailersTask extends AsyncTask<String,Void,String[]> {
        @Override
        protected String[] doInBackground(String...params){
            String id = params[0];
            URL trailersUrl = NetworkUtils.buildTrailerUrl(id);
            try{
                String jsonResponse = NetworkUtils.getResponseFromUrl(trailersUrl);
                return OpenMovieJsonUtils.getYoutubeKeysFromJson(DetailsActivity.this, jsonResponse);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String[] trailerKeys){
            if(trailerKeys != null && trailerKeys.length != 0){
                mTrailersAdapter.setTrailerKeysData(trailerKeys);
                trailersLayoutManager.onRestoreInstanceState(trailerSavedState);
            }else{
                mTrailersAdapter.setTrailerKeysData(null);
                mTrailersRecyclerView.setVisibility(View.GONE);
                mTrailersLabelTextView.setVisibility(View.GONE);
            }
        }
    }

    public class FetchReviewsTask extends AsyncTask<String,Void,String[]> {
        @Override
        protected String[] doInBackground(String...params){
            String id = params[0];
            URL reviewsUrl = NetworkUtils.buildReviewUrl(id);
            try{
                String jsonResponse = NetworkUtils.getResponseFromUrl(reviewsUrl);
                return OpenMovieJsonUtils.getReviewsFromJson(DetailsActivity.this, jsonResponse);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String[] movieReviews){
            if(movieReviews != null && movieReviews.length != 0){
                mReviewsAdapter.setReviewsData(movieReviews);
                reviewsLayoutManager.onRestoreInstanceState(reviewsSavedState);
            }else{
                mReviewsAdapter.setReviewsData(null);
                mReviewsRecyclerView.setVisibility(View.GONE);
                mReviewsLabelTextView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TRAILER_LIST_STATE_KEY,trailersLayoutManager.onSaveInstanceState());
        outState.putParcelable(REVIEW_LIST_STATE_KEY,reviewsLayoutManager.onSaveInstanceState());
        outState.putIntArray("SCROLL_POSITION", new int[]{ detailActivityScrollView.getScrollX(), detailActivityScrollView.getScrollY()});
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            trailerSavedState = savedInstanceState.getParcelable(TRAILER_LIST_STATE_KEY);
            reviewsSavedState = savedInstanceState.getParcelable(REVIEW_LIST_STATE_KEY);
            final int[] position = savedInstanceState.getIntArray("SCROLL_POSITION");
            Log.d(DetailsActivity.class.getSimpleName(),"Saved X Position: " + position[0]);
            if(position != null)
                detailActivityScrollView.post(new Runnable() {
                    public void run() {
                        detailActivityScrollView.scrollTo(position[0], position[1]);
                    }
                });
        }
    }

    public void addMovieToFavoritesData(View view){
        Button favoriteButton = (Button)view;
        String currentLabel = favoriteButton.getText().toString();
        if(currentLabel == getString(R.string.addToFavoritesButtonLabel)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MoviesDataContract.MoviesDataEntry.COLUMN_TITLE, movieTitle);
            contentValues.put(MoviesDataContract.MoviesDataEntry.COLUMN_MOVIE_ID, movieId);
            contentValues.put(MoviesDataContract.MoviesDataEntry.COLUMN_POSTER_PATH, moviePosterPath);
            contentValues.put(MoviesDataContract.MoviesDataEntry.COLUMN_DESCRIPTION, movieDescription);
            Uri uri = getContentResolver().insert(MoviesDataContract.MoviesDataEntry.FAVORITES_CONTENT_URI, contentValues);
            if (uri != null) {
                Toast.makeText(getBaseContext(), "Movie Added to Favorites", Toast.LENGTH_LONG).show();
            }
            favoriteButton.setText(getString(R.string.removeFromFavoritesButtonLabel));
        }else if(currentLabel == getString(R.string.removeFromFavoritesButtonLabel)){
            String movieEntryNumber = String.valueOf(favoriteButton.getTag());
            Uri movieInFavoritesUri = MoviesDataContract.buildMovieUriWithIdAndMoviesUri(MoviesDataContract.MoviesDataEntry.FAVORITES_CONTENT_URI,movieEntryNumber);
            int movieDeleted = getContentResolver().delete(movieInFavoritesUri,null,null);
            if(movieDeleted > 0){
                Toast.makeText(getBaseContext(), "Movie Deleted From Favorites", Toast.LENGTH_LONG).show();
            }
            favoriteButton.setText(R.string.addToFavoritesButtonLabel);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, final Bundle loaderArgs) {
        switch(loaderId){
            case MOVIE_DETAILS_LOADER_ID:
                return new CursorLoader(this,movieUri,MOVIES_PROJECTION,null,null,null);
            default:
                throw new RuntimeException("Loader Not Implemented" + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }
        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            return;
        }
        movieId = data.getString(2);
        moviePosterPath = data.getString(3);
        movieDescription = data.getString(4);
        movieTitle = data.getString(1);
        mMovieInfoTextView.setText(movieDescription);
        Picasso.with(this).load("http://image.tmdb.org/t/p/w342" + moviePosterPath).into(mMoviePoster);
        mMovieTitle.setText(movieTitle);
        Cursor movieInFavoritesCursor = getContentResolver().query(MoviesDataContract.MoviesDataEntry.FAVORITES_CONTENT_URI,null, MoviesDataContract.MoviesDataEntry.COLUMN_MOVIE_ID + " = ?",new String[]{movieId} ,null);
        if(movieInFavoritesCursor != null && movieInFavoritesCursor.getCount() > 0){
            mFavoritesButton.setText(getString(R.string.removeFromFavoritesButtonLabel));
            movieInFavoritesCursor.moveToFirst();
            mFavoritesButton.setTag(movieInFavoritesCursor.getInt(0));
            movieInFavoritesCursor.close();
        }
        new FetchTrailersTask().execute(movieId);
        new FetchReviewsTask().execute(movieId);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

}