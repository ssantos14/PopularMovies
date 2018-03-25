package com.example.android.popularmovies.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.popularmovies.data.MoviesDataContract;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by Sylvana on 1/18/2018.
 */

public class SyncMovieDataUtils {

    private static final int SYNC_INTERVAL_HOURS = 24;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 24;

    private static boolean sInitialized;

    private static final String SYNC_TAG = "movies-sync";

    private static final String TAG = SyncMovieDataUtils.class.getSimpleName();

    synchronized public static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context) {
        Log.d(TAG,"got to scheduleFirebaseJobDispatcher");
        if(sInitialized) return;
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Job syncMovieDataJob = dispatcher.newJobBuilder()
                .setService(SyncFirebaseJobService.class)
                .setTag(SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(syncMovieDataJob);
        sInitialized = true;
    }

    synchronized public static void initialize(@NonNull final Context context) {
        Log.d(TAG,"got to initialize");
        if (sInitialized) return;
        sInitialized = true;
        scheduleFirebaseJobDispatcherSync(context);

        //Need to check if database is empty
        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {
                Uri moviesQueryUri = MoviesDataContract.MoviesDataEntry.POPULAR_CONTENT_URI;
                Cursor cursor = context.getContentResolver().query(
                        moviesQueryUri,
                        null,
                        null,
                        null,
                        null);
                //If it is empty, sync data
                if (cursor == null || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }
                cursor.close();
            }
        });
        checkForEmpty.start();
    }

    public static void startImmediateSync(@NonNull final Context context) {
        Log.d(TAG,"got to immediate sync");
        Intent intentToSyncImmediately = new Intent(context, SyncMovieDataIntentService.class);
        context.startService(intentToSyncImmediately);
        Log.d(TAG,"INTENT SENT");
    }

}
