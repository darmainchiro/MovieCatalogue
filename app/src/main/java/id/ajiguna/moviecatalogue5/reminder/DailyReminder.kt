package id.ajiguna.moviecatalogue5.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.PendingIntent
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.NotificationChannel
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import id.ajiguna.moviecatalogue5.R
import android.media.RingtoneManager
import id.ajiguna.moviecatalogue5.MainActivity
import java.util.*


class DailyReminder : BroadcastReceiver() {

    companion object {
        private val NOTIFICATION_CHANNEL_ID = "channel_01"
        private val NOTIFICATION_ID = 100
    }

    override fun onReceive(context: Context, intent: Intent) {
        sendNotification(
            context, context.getString(R.string.app_name),
            context.getString(R.string.daily_message_reminder)
        )
    }

    fun setDailyAlarm(context: Context) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyReminder::class.java)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 7)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, 0)
        alarmManager?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.getTimeInMillis(),
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun sendNotification(context: Context, title: String, desc: String) {
        val notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        val intent = Intent(context, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            context, DailyReminder.NOTIFICATION_ID, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val uriTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_movie_24dp)
            .setContentTitle(title)
            .setContentText(desc)
            .setContentIntent(pendingIntent)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSound(uriTone)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.YELLOW
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

            builder.setChannelId(NOTIFICATION_CHANNEL_ID)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(DailyReminder.NOTIFICATION_ID, builder.build())
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(getPendingIntent(context))
    }

    private fun getPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, ReleaseReminder::class.java)
        return PendingIntent.getBroadcast(
            context, NOTIFICATION_ID, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}
