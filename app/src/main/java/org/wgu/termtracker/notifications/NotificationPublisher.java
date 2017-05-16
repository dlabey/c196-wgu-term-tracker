package org.wgu.termtracker.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.wgu.termtracker.Constants;

public class NotificationPublisher extends BroadcastReceiver {
    private static final String TAG = "NotificationPublisher";

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(Constants.NOTIFICATION);

        Log.d(TAG, String.valueOf(notification));

        int notificationId = intent.getIntExtra(Constants.NOTIFICATION_ID, 0);

        Log.d(TAG, String.valueOf(notificationId));

        notificationManager.notify(notificationId, notification);
    }
}
