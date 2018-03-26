package com.example.android.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Sylvana on 11/27/2017.
 */

public class NetworkUtils {
    private static final String BASE_URL =  "http://api.themoviedb.org/3/movie/";
    private static final String ApiKey = "INSERT";
    private static final String YOUTUBE_URL = "http://youtube.com/watch";

    public static URL buildPopularMoviesUrl(){
        Log.d(NetworkUtils.class.getSimpleName(),"building url");
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath("popular")
                .appendQueryParameter("api_key", ApiKey)
                .appendQueryParameter("mode", "json")
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildTopRatedMoviesUrl(){
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath("top_rated")
                .appendQueryParameter("api_key", ApiKey)
                .appendQueryParameter("mode", "json")
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildTrailerUrl(String movieId){
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath("videos")
                .appendQueryParameter("api_key", ApiKey)
                .appendQueryParameter("mode", "json")
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildReviewUrl(String movieId){
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath("reviews")
                .appendQueryParameter("api_key", ApiKey)
                .appendQueryParameter("mode", "json")
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildYoutubeTrailerUrl(String movieKey){
        Uri builtUri = Uri.parse(YOUTUBE_URL).buildUpon()
                .appendQueryParameter("v", movieKey)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}