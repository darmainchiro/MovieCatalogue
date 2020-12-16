package id.ajiguna.moviecatalogue5.ui.setting

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import id.ajiguna.moviecatalogue5.R
import kotlinx.android.synthetic.main.activity_setting.*
import id.ajiguna.moviecatalogue5.reminder.ReleaseReminder
import id.ajiguna.moviecatalogue5.reminder.DailyReminder


class SettingActivity : AppCompatActivity(){

    private var sharedPreferences: SharedPreferences? = null
    private var sharedPreferenceEdit: SharedPreferences.Editor? = null
    private var mMovieDailyReceiver: DailyReminder? = null
    private var mMovieReleaseReceiver: ReleaseReminder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportActionBar?.title = "Setting"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPreferences =
            getSharedPreferences("reminder", Context.MODE_PRIVATE)
        mMovieDailyReceiver = DailyReminder()
        mMovieReleaseReceiver = ReleaseReminder()

        switchConfiguration()
        setPreferences()
    }

    private fun setPreferences() {
        val dailyReminder = sharedPreferences!!.getBoolean("daily_reminder", false)
        val releaseReminder = sharedPreferences!!.getBoolean("release_reminder", false)

        switch_daily.setChecked(dailyReminder)
        switch_upcoming.setChecked(releaseReminder)
    }

    private fun switchConfiguration() {

        switch_daily.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            sharedPreferenceEdit = sharedPreferences?.edit()
            sharedPreferenceEdit?.putBoolean("daily_reminder", isChecked)
            sharedPreferenceEdit?.apply()
            if (isChecked) {
                mMovieDailyReceiver?.setDailyAlarm(applicationContext)
            } else {
                mMovieDailyReceiver?.cancelAlarm(applicationContext)
            }
        })
        switch_upcoming.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            sharedPreferenceEdit = sharedPreferences?.edit()
            sharedPreferenceEdit?.putBoolean("release_reminder", isChecked)
            sharedPreferenceEdit?.apply()
            if (isChecked) {
                mMovieReleaseReceiver?.setReleaseAlarm(applicationContext)
            } else {
                mMovieReleaseReceiver?.cancelAlarm(applicationContext)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)  {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}