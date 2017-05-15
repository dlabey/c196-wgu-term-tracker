package org.wgu.termtracker.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import org.wgu.termtracker.Constants;
import org.wgu.termtracker.activities.HomeActivity;
import org.wgu.termtracker.models.AssessmentModel;
import org.wgu.termtracker.models.CourseModel;
import org.wgu.termtracker.models.TermModel;

public class NotificationScheduler {
    protected Context context;

    public NotificationScheduler(Context context) {
        this.context = context;
    }

    public void bootNotifications() {
        // go through courses and assessments based on notification preferences and schedule if not
        // already scheduled
    }

    public void rescheduleNotification(long delay, int notificationId) {

    }

    public void scheduleNotification(long delay, int notificationId, TermModel term,
                                     CourseModel course, String title, String content) {
        this.scheduleNotification(delay, notificationId, term, course, null, title, content);
    }

    public void scheduleNotification(long delay, int notificationId, TermModel term,
                                     CourseModel course, AssessmentModel assessment, String title,
                                     String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent intent = new Intent(context, HomeActivity.class);

        PendingIntent activity = PendingIntent.getActivity(context, notificationId, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        builder.setContentIntent(activity);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);

        notificationIntent.putExtra(Constants.NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(Constants.NOTIFICATION, notification);
        notificationIntent.putExtra(Constants.TERM, term);
        notificationIntent.putExtra(Constants.COURSE, course);
        notificationIntent.putExtra(Constants.ASSESSMENT, assessment);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId,
                notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }
}
