package com.example.android.popularmovies.utilities;

import android.content.ContentValues;
import android.content.Context;

import com.example.android.popularmovies.data.MoviesDataContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sylvana on 11/27/2017.
 */

public class OpenMovieJsonUtils {
    public static ContentValues[] getContentValuesFromJson(Context context, String moviesJson)
            throws JSONException {
        String[] moviesInfo;
        String title;
        String description;
        String rating;
        String releaseDate;
        String posterPath;
        String movieId;

        JSONObject response = new JSONObject(moviesJson);
        JSONArray results = response.getJSONArray("results");
        ContentValues[] movieDataContentValuesArray = new ContentValues[results.length()];
        for (int i = 0; i < results.length(); i++) {
            JSONObject movie = results.getJSONObject(i);
            title = movie.getString("original_title");
            description = movie.getString("overview");
            rating = movie.getString("vote_average");
            releaseDate = movie.getString("release_date");
            posterPath = movie.getString("poster_path");
            movieId = movie.getString("id");

            ContentValues movieDataContentValues = new ContentValues();
            movieDataContentValues.put(MoviesDataContract.MoviesDataEntry.COLUMN_TITLE, title);
            movieDataContentValues.put(MoviesDataContract.MoviesDataEntry.COLUMN_MOVIE_ID, movieId);
            movieDataContentValues.put(MoviesDataContract.MoviesDataEntry.COLUMN_POSTER_PATH, posterPath);
            movieDataContentValues.put(MoviesDataContract.MoviesDataEntry.COLUMN_DESCRIPTION, "Release Date: " + releaseDate + '\n' + "Rating: " + rating + '\n' + "Synopsis: " + description);
            movieDataContentValuesArray[i] = movieDataContentValues;
        }

        return movieDataContentValuesArray;
    }

    public static String[] getYoutubeKeysFromJson(Context context, String movieTrailerJson)
            throws JSONException{
        JSONObject response = new JSONObject(movieTrailerJson);
        JSONArray results = response.getJSONArray("results");
        String[] youtubeKeys = new String[results.length()];
        for(int i = 0; i < results.length(); i++) {
            JSONObject trailer = results.getJSONObject(i);
            youtubeKeys[i] = trailer.getString("key");
        }
        return youtubeKeys;
    }

    public static String[] getReviewsFromJson(Context context, String movieTrailerJson)
            throws JSONException{
        JSONObject response = new JSONObject(movieTrailerJson);
        JSONArray results = response.getJSONArray("results");
        if(results.length() != 0) {
            String[] youtubeKeys = new String[results.length()];
            for (int i = 0; i < results.length(); i++) {
                JSONObject review = results.getJSONObject(i);
                String author = review.getString("author");
                String content = review.getString("content");
                youtubeKeys[i] = "Author: " + author + '\n' + "Review: " + content;
            }
            return youtubeKeys;
        }else{
            return null;
        }
    }
}