package com.example.android.popularmovies.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.popularmovies.data.MoviesDataContract;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.utilities.OpenMovieJsonUtils;

import java.net.URL;

/**
 * Created by Sylvana on 1/18/2018.
 */

public class SyncMovieDataTask {

    synchronized public static void syncMovieData(Context context) {

        try {
            URL popularMoviesUrl = NetworkUtils.buildPopularMoviesUrl();
            URL topRatedMoviesURL = NetworkUtils.buildTopRatedMoviesUrl();
            String jsonPopularMoviesResponse = NetworkUtils.getResponseFromUrl(popularMoviesUrl);
            String jsonTopRatedMoviesResponse = NetworkUtils.getResponseFromUrl(topRatedMoviesURL);
            ContentValues[] PopularMoviesDataValues = OpenMovieJsonUtils.getContentValuesFromJson(context,jsonPopularMoviesResponse);
            ContentValues[] TopRatedMoviesDataValues = OpenMovieJsonUtils.getContentValuesFromJson(context,jsonTopRatedMoviesResponse);
            if (PopularMoviesDataValues != null && PopularMoviesDataValues.length != 0) {
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(
                        MoviesDataContract.MoviesDataEntry.POPULAR_CONTENT_URI,
                        null,
                        null);
                contentResolver.bulkInsert(
                        MoviesDataContract.MoviesDataEntry.POPULAR_CONTENT_URI,
                        PopularMoviesDataValues);
            }
            if (TopRatedMoviesDataValues != null && TopRatedMoviesDataValues.length != 0) {
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(
                        MoviesDataContract.MoviesDataEntry.TOP_RATED_CONTENT_URI,
                        null,
                        null);
                contentResolver.bulkInsert(
                        MoviesDataContract.MoviesDataEntry.TOP_RATED_CONTENT_URI,
                        TopRatedMoviesDataValues);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
