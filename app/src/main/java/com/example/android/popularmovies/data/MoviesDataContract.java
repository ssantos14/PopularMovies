package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Sylvana on 1/15/2018.
 */

public class MoviesDataContract {
    public static final String AUTHORITY = "com.example.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITE_MOVIES = "favorite_movies";
    public static final String PATH_POPULAR_MOVIES = "popular_movies";
    public static final String PATH_TOP_RATED_MOVIES = "top_rated";
    public static final class MoviesDataEntry implements BaseColumns{
        public static final Uri FAVORITES_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();
        public static final Uri POPULAR_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_POPULAR_MOVIES).build();
        public static final Uri TOP_RATED_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TOP_RATED_MOVIES).build();
        public static final String FAVORITES_TABLE_NAME = "favorite_movies";
        public static final String POPULAR_TABLE_NAME = "popular_movies";
        public static final String TOP_RATED_TABLE_NAME = "top_rated_movies";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_POSTER_PATH = "path";
        public static final String COLUMN_DESCRIPTION = "description";
    }

    public static Uri buildMovieUriWithIdAndMoviesUri(Uri moviesUri, String id) {
        return moviesUri.buildUpon()
                .appendPath(id)
                .build();
    }

}