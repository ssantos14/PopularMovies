package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies.data.MoviesDataContract;

/**
 * Created by Sylvana on 1/15/2018.
 */

public class MoviesDataDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 10;
    public MoviesDataDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate (SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_FAVORITE_MOVIES_TABLE =
                "CREATE TABLE " + MoviesDataContract.MoviesDataEntry.FAVORITES_TABLE_NAME + " (" +
                MoviesDataContract.MoviesDataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesDataContract.MoviesDataEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesDataContract.MoviesDataEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                MoviesDataContract.MoviesDataEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MoviesDataContract.MoviesDataEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL);";
        final String SQL_CREATE_POPULAR_MOVIES_TABLE =
                "CREATE TABLE " + MoviesDataContract.MoviesDataEntry.POPULAR_TABLE_NAME + " (" +
                        MoviesDataContract.MoviesDataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MoviesDataContract.MoviesDataEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        MoviesDataContract.MoviesDataEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                        MoviesDataContract.MoviesDataEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                        MoviesDataContract.MoviesDataEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL);";
        final String SQL_CREATE_TOP_RATED_MOVIES_TABLE =
                "CREATE TABLE " + MoviesDataContract.MoviesDataEntry.TOP_RATED_TABLE_NAME + " (" +
                        MoviesDataContract.MoviesDataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MoviesDataContract.MoviesDataEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        MoviesDataContract.MoviesDataEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                        MoviesDataContract.MoviesDataEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                        MoviesDataContract.MoviesDataEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_POPULAR_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TOP_RATED_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesDataContract.MoviesDataEntry.FAVORITES_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesDataContract.MoviesDataEntry.POPULAR_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesDataContract.MoviesDataEntry.TOP_RATED_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
