package id.ajiguna.moviecatalogue5.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.PendingIntent
import android.app.AlarmManager
import id.ajiguna.moviecatalogue5.api.ApiClient
import android.app.NotificationManager
import android.app.NotificationChannel
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import android.media.RingtoneManager
import id.ajiguna.moviecatalogue5.MainActivity
import java.util.*
import id.ajiguna.moviecatalogue5.R
import id.ajiguna.moviecatalogue5.model.Movie
import id.ajiguna.moviecatalogue5.model.MovieResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class ReleaseReminder : BroadcastReceiver() {

    private var list = ArrayList<Movie>()
    var mContext: Context? = null
    companion object {
        private val NOTIFICATION_CHANNEL_ID = "channel_02"
        private val NOTIFICATION_ID = 101
    }

    override fun onReceive(context: Context, intent: Intent) {
        mContext = context
        getReleaseMovie()
    }

    private fun sendNotification(context: Context, title: String) {
        val notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        val intent = Intent(context, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            context, ReleaseReminder.NOTIFICATION_ID, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val uriTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_movie_24dp)
            .setContentTitle(title)
            .setContentText(context.resources.getString(R.string.release_reminder_message))
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
        notificationManager.notify(ReleaseReminder.NOTIFICATION_ID, builder.build())

    }

    private fun getReleaseMovie() {
        val apiKey = mContext?.getString(R.string.api_key)
        try {
            val dateToday = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            ApiClient().services.getReleasedMovies(apiKey,dateToday, dateToday).enqueue(object :
                Callback<MovieResponse> {
                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    if(response.code() == 200) {
                        list = response.body()?.movies as ArrayList<Movie>
                        var id = 2
                        for (movie in list) {
                            val title = movie.title
                            val desc =
                                title + " " + mContext?.getString(R.string.release_reminder_message)
                            mContext?.let { movie.title?.let { it1 -> sendNotification(it, it1) } }
                            id++
                        }

                    }
                }
                override fun onFailure(call: Call<MovieResponse>, t: Throwable){
                }
            })

        } catch (e: Exception) {
            println("error$e")
        }

    }

    fun setReleaseAlarm(context: Context) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReleaseReminder::class.java)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 8)
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
