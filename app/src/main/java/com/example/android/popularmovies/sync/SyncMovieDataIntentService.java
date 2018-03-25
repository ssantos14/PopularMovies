package com.example.android.popularmovies.sync;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Sylvana on 1/18/2018.
 */

public class SyncMovieDataIntentService extends IntentService {
    public SyncMovieDataIntentService() {
        super("SyncMovieDataIntentService");
        Log.d(SyncMovieDataIntentService.class.getSimpleName(),"got to intent service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(SyncMovieDataIntentService.class.getSimpleName(),"got to intent service");
        SyncMovieDataTask.syncMovieData(this);
    }
}
