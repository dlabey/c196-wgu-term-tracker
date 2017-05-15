package org.wgu.termtracker.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.wgu.termtracker.Constants;

public class NotificationPublisher extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(Constants.NOTIFICATION);

        int notificationId = intent.getIntExtra(Constants.NOTIFICATION_ID, 0);

        notificationManager.notify(notificationId, notification);
    }
}
