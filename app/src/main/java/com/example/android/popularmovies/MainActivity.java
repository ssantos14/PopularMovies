package com.example.android.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.popularmovies.data.MoviesDataContract;
import com.example.android.popularmovies.sync.SyncMovieDataUtils;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler,SharedPreferences.OnSharedPreferenceChangeListener,LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView mMoviesRecyclerView;
    private MoviesAdapter mMoviesAdapter;
    private ProgressBar mLoadingIndicator;
    private int CurrentLoaderId;
    private static final int FAVORITE_MOVIES_LOADER_ID = 19;
    private static final int POPULAR_MOVIES_LOADER_ID = 20;
    private static final int TOP_RATED_MOVIES_LOADER_ID = 21;
    public static final String[] MOVIES_PROJECTION = {
            MoviesDataContract.MoviesDataEntry._ID,
            MoviesDataContract.MoviesDataEntry.COLUMN_TITLE,
            MoviesDataContract.MoviesDataEntry.COLUMN_MOVIE_ID,
            MoviesDataContract.MoviesDataEntry.COLUMN_POSTER_PATH,
            MoviesDataContract.MoviesDataEntry.COLUMN_DESCRIPTION};
    private Uri MoviesQueryUri;
    private static final String URI_KEY = "uri_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMoviesRecyclerView = findViewById(R.id.movies_recyclerview);
        mLoadingIndicator = findViewById(R.id.loading_indicator);
        GridLayoutManager layoutManager;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            layoutManager = new GridLayoutManager(this, 2);
        }
        else{
            layoutManager = new GridLayoutManager(this, 3);
        }
        mMoviesRecyclerView.setLayoutManager(layoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);
        mMoviesAdapter = new MoviesAdapter(this,this);
        mMoviesRecyclerView.setAdapter(mMoviesAdapter);
        setUpSharedPreferences();
        SyncMovieDataUtils.initialize(this);
        getLoaderManager().initLoader(CurrentLoaderId,null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId,final Bundle loaderArgs) {
        switch(loaderId){
            case FAVORITE_MOVIES_LOADER_ID:
                MoviesQueryUri = MoviesDataContract.MoviesDataEntry.FAVORITES_CONTENT_URI;
                return new CursorLoader(this,MoviesQueryUri,MOVIES_PROJECTION,null,null,null);
            case POPULAR_MOVIES_LOADER_ID:
                MoviesQueryUri = MoviesDataContract.MoviesDataEntry.POPULAR_CONTENT_URI;
                return new CursorLoader(this,MoviesQueryUri,MOVIES_PROJECTION,null,null,null);
            case TOP_RATED_MOVIES_LOADER_ID:
                MoviesQueryUri = MoviesDataContract.MoviesDataEntry.TOP_RATED_CONTENT_URI;
                return new CursorLoader(this,MoviesQueryUri,MOVIES_PROJECTION,null,null,null);
            default:
                throw new RuntimeException("Loader Not Implemented" + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        showMoviesDisplay();
        Log.d(MainActivity.class.getSimpleName(), DatabaseUtils.dumpCursorToString(data));
        mMoviesAdapter.setMovieDataCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapter.setMovieDataCursor(null);
    }

    public void setUpSharedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortMoviesBy = sharedPreferences.getString(getString(R.string.sortOrderPreferenceKey),getResources().getString(R.string.sortPopularValue));
        if(sortMoviesBy.equals(getString(R.string.sortFavoriteValue))){
            CurrentLoaderId = FAVORITE_MOVIES_LOADER_ID;
        }else if(sortMoviesBy.equals(getString(R.string.sortPopularValue))){
            CurrentLoaderId = POPULAR_MOVIES_LOADER_ID;
        }else if(sortMoviesBy.equals(getString(R.string.sortRatingValue))){
            CurrentLoaderId = TOP_RATED_MOVIES_LOADER_ID;
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onClick (int movieAdapterPosition) {
        Context context = this;
        Class destinationActivity = DetailsActivity.class;
        Intent MoveToMovieDescriptionIntent = new Intent(context, destinationActivity);
        String id = String.valueOf(movieAdapterPosition + 1);
        Log.d(MainActivity.class.getSimpleName(), "Uri for movie: " + MoviesQueryUri);
        Uri uriForMovie = MoviesDataContract.buildMovieUriWithIdAndMoviesUri(MoviesQueryUri,id);
        MoveToMovieDescriptionIntent.setData(uriForMovie);
        startActivity(MoveToMovieDescriptionIntent);
    }

    private void showMoviesDisplay(){
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if(id == R.id.settings){
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.sortOrderPreferenceKey))){
            String defaultValue = getResources().getString(R.string.sortPopularValue);
            String currentValue = sharedPreferences.getString(key,defaultValue);
            if(currentValue == getString(R.string.sortFavoriteValue)){
                CurrentLoaderId = FAVORITE_MOVIES_LOADER_ID;
                getLoaderManager().restartLoader(CurrentLoaderId,null,this);
            }else if(currentValue == getString(R.string.sortPopularValue)){
                CurrentLoaderId = POPULAR_MOVIES_LOADER_ID;
                getLoaderManager().restartLoader(CurrentLoaderId,null,this);
            }else if(currentValue == getString(R.string.sortRatingValue)){
                CurrentLoaderId = TOP_RATED_MOVIES_LOADER_ID;
                getLoaderManager().restartLoader(CurrentLoaderId,null,this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(URI_KEY,MoviesQueryUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(URI_KEY)){
                MoviesQueryUri = savedInstanceState.getParcelable(URI_KEY);
            }
        }
    }
}