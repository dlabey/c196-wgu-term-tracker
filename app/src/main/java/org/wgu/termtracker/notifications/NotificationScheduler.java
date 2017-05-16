package org.wgu.termtracker.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.wgu.termtracker.Constants;
import org.wgu.termtracker.R;
import org.wgu.termtracker.activities.HomeActivity;
import org.wgu.termtracker.models.AssessmentModel;
import org.wgu.termtracker.models.CourseModel;
import org.wgu.termtracker.models.TermModel;

public class NotificationScheduler {
    private static final String TAG = "NotificationScheduler";

    protected Context context;

    public NotificationScheduler(Context context) {
        this.context = context;
    }

    public void scheduleNotification(Class forClass, long delay, int notificationId, TermModel term,
                                     CourseModel course, String title, String content) {
        this.scheduleNotification(forClass, delay, notificationId, term, course, null, title,
                content);
    }

    public void scheduleNotification(Class forClass, long delay, int notificationId, TermModel term,
                                     CourseModel course, AssessmentModel assessment, String title,
                                     String content) {
        Log.d(TAG, term.toString());
        Log.d(TAG, course.toString());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent intent = new Intent(context, forClass);

        intent.putExtra(Constants.TERM, term);
        intent.putExtra(Constants.COURSE, course);
        intent.putExtra(Constants.ASSESSMENT, assessment);

        PendingIntent activity = PendingIntent.getActivity(context, notificationId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(activity);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);

        notificationIntent.putExtra(Constants.NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(Constants.NOTIFICATION, notification);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId,
                notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Log.d(TAG, String.valueOf(delay));

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, delay, pendingIntent);
    }
}
