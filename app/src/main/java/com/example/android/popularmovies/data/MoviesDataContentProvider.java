package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Sylvana on 1/15/2018.
 */

public class MoviesDataContentProvider extends ContentProvider {
    private MoviesDataDbHelper mMovieDbHelper;
    public static final int FAVORITE_MOVIES = 333;
    public static final int FAVORITE_MOVIE_WITH_ID = 334;
    public static final int POPULAR_MOVIES = 335;
    public static final int POPULAR_MOVIE_WITH_ID = 336;
    public static final int TOP_RATED_MOVIES = 337;
    public static final int TOP_RATED_MOVIE_WITH_ID = 338;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MoviesDataContract.AUTHORITY, MoviesDataContract.PATH_FAVORITE_MOVIES , FAVORITE_MOVIES);
        uriMatcher.addURI(MoviesDataContract.AUTHORITY, MoviesDataContract.PATH_FAVORITE_MOVIES + "/#", FAVORITE_MOVIE_WITH_ID);
        uriMatcher.addURI(MoviesDataContract.AUTHORITY, MoviesDataContract.PATH_POPULAR_MOVIES , POPULAR_MOVIES);
        uriMatcher.addURI(MoviesDataContract.AUTHORITY, MoviesDataContract.PATH_POPULAR_MOVIES + "/#", POPULAR_MOVIE_WITH_ID);
        uriMatcher.addURI(MoviesDataContract.AUTHORITY, MoviesDataContract.PATH_TOP_RATED_MOVIES , TOP_RATED_MOVIES);
        uriMatcher.addURI(MoviesDataContract.AUTHORITY, MoviesDataContract.PATH_TOP_RATED_MOVIES + "/#", TOP_RATED_MOVIE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDbHelper = new MoviesDataDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor returnCursor;
        switch(match){
            case FAVORITE_MOVIES:
                returnCursor = db.query(MoviesDataContract.MoviesDataEntry.FAVORITES_TABLE_NAME, projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case POPULAR_MOVIES:
                returnCursor = db.query(MoviesDataContract.MoviesDataEntry.POPULAR_TABLE_NAME, projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case TOP_RATED_MOVIES:
                returnCursor = db.query(MoviesDataContract.MoviesDataEntry.TOP_RATED_TABLE_NAME, projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case FAVORITE_MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};
                returnCursor = db.query(MoviesDataContract.MoviesDataEntry.FAVORITES_TABLE_NAME,projection,mSelection,mSelectionArgs,null,null,sortOrder);
                break;
            case POPULAR_MOVIE_WITH_ID:
                String id2 = uri.getPathSegments().get(1);
                String mSelection2 = "_id=?";
                String[] mSelectionArgs2 = new String[]{id2};
                returnCursor = db.query(MoviesDataContract.MoviesDataEntry.POPULAR_TABLE_NAME,projection,mSelection2,mSelectionArgs2,null,null,sortOrder);
                break;
            case TOP_RATED_MOVIE_WITH_ID:
                String id3 = uri.getPathSegments().get(1);
                String mSelection3 = "_id=?";
                String[] mSelectionArgs3 = new String[]{id3};
                returnCursor = db.query(MoviesDataContract.MoviesDataEntry.TOP_RATED_TABLE_NAME,projection,mSelection3,mSelectionArgs3,null,null,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case FAVORITE_MOVIES:
                long id1 = db.insert(MoviesDataContract.MoviesDataEntry.FAVORITES_TABLE_NAME,null,contentValues);
                Log.d(MoviesDataContentProvider.class.getSimpleName(),"id for added movie: " + String.valueOf(id1));
                if (id1>0){
                    returnUri = ContentUris.withAppendedId(MoviesDataContract.MoviesDataEntry.FAVORITES_CONTENT_URI,id1);
                }else{
                    throw new android.database.SQLException("Failed to insert entry to " + uri);
                }
                break;
            case POPULAR_MOVIES:
                long id2 = db.insert(MoviesDataContract.MoviesDataEntry.POPULAR_TABLE_NAME,null,contentValues);
                if (id2>0){
                    returnUri = ContentUris.withAppendedId(MoviesDataContract.MoviesDataEntry.POPULAR_CONTENT_URI,id2);
                }else{
                    throw new android.database.SQLException("Failed to insert entry to " + uri);
                }
                break;
            case TOP_RATED_MOVIES:
                long id3 = db.insert(MoviesDataContract.MoviesDataEntry.TOP_RATED_TABLE_NAME,null,contentValues);
                if (id3>0){
                    returnUri = ContentUris.withAppendedId(MoviesDataContract.MoviesDataEntry.TOP_RATED_CONTENT_URI,id3);
                }else{
                    throw new android.database.SQLException("Failed to insert entry to " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int moviesDeleted;
        switch (match){
            case FAVORITE_MOVIES:
                moviesDeleted = db.delete(MoviesDataContract.MoviesDataEntry.FAVORITES_TABLE_NAME,null,null);
                break;
            case POPULAR_MOVIES:
                moviesDeleted = db.delete(MoviesDataContract.MoviesDataEntry.POPULAR_TABLE_NAME,null,null);
                break;
            case TOP_RATED_MOVIES:
                moviesDeleted = db.delete(MoviesDataContract.MoviesDataEntry.TOP_RATED_TABLE_NAME,null,null);
                break;
            case FAVORITE_MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                moviesDeleted = db.delete(MoviesDataContract.MoviesDataEntry.FAVORITES_TABLE_NAME,"_id=?",new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(moviesDeleted != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch(match){
            case POPULAR_MOVIES:
                db.beginTransaction();
                int rowsInserted1 = 0;
                try{
                    for(ContentValues value:values){
                        long id = db.insert(MoviesDataContract.MoviesDataEntry.POPULAR_TABLE_NAME,null,value);
                        if(id != -1){rowsInserted1++;}
                    }
                db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                if(rowsInserted1 > 0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return rowsInserted1;
            case TOP_RATED_MOVIES:
                db.beginTransaction();
                int rowsInserted2 = 0;
                try{
                    for(ContentValues value:values){
                        long id = db.insert(MoviesDataContract.MoviesDataEntry.TOP_RATED_TABLE_NAME,null,value);
                        if(id != -1){rowsInserted2++;}
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                if(rowsInserted2 > 0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return rowsInserted2;
            default:
                return super.bulkInsert(uri,values);
        }
    }

}

