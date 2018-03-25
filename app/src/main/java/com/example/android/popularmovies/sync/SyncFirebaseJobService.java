package com.example.android.popularmovies.sync;

import android.content.Context;
import android.os.AsyncTask;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.JobParameters;

/**
 * Created by Sylvana on 1/18/2018.
 */

public class SyncFirebaseJobService extends JobService{
    private AsyncTask<Void, Void, Void> mBackgroundSyncTask;
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        mBackgroundSyncTask = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                SyncMovieDataTask.syncMovieData(context);
                return null;
            }

            @Override
            protected void onPostExecute(Void Void1) {
                jobFinished(jobParameters, false);
            }
        };

        mBackgroundSyncTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mBackgroundSyncTask != null) {
            mBackgroundSyncTask.cancel(true);
        }
        return true;
    }
}
