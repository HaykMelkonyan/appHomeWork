package com.david.interactionandengagement;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class NotificationActivity extends AppCompatActivity {

    private final int mNotificationId = 100;

    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotifyMgr;

    private boolean isOnProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void showNotification(View view) {

        if (!isOnProgress) {
            Intent resultIntent = new Intent(this, ShareActivity.class);

            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.side_nav_bar)
                    .setContentTitle("My notification")
                    .setContentText("0/100")
                    .setContentIntent(resultPendingIntent);

            new Downloader().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            Toast.makeText(getApplicationContext(), "Progress not completed", Toast.LENGTH_SHORT).show();
        }

    }


    class Downloader extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Displays the progress bar for the first time.
            mBuilder.setProgress(100, 0, false);
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
            isOnProgress = true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // Update progress
            mBuilder.setProgress(100, values[0], false);
            mBuilder.setContentText(values[0] + "/100");
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
            super.onProgressUpdate(values);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int i;
            if (isCancelled())
                return null;
            for (i = 0; i <= 100; i += 5) {
                if (isCancelled())
                    return null;
                // Sets the progress indicator completion percentage
                publishProgress(Math.min(i, 100));
                try {
                    // Sleep for 5 seconds
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mBuilder.setContentText("Download complete");
            // Removes the progress bar
            mBuilder.setProgress(0, 0, false);
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
            isOnProgress = false;
        }
    }

}
