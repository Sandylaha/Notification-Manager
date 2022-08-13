package com.example.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.notification.databinding.ActivityMainBinding
import java.util.*

const val CHANNEL_ID: String = "1"
const val CHANNEL_NAME: String = "Sandip"
const val CHANNEL_DESCRIPTION = "Notification Message"

class MainActivity : AppCompatActivity() {



    private lateinit var binding: ActivityMainBinding


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()
        binding.btn1.setOnClickListener { displaySimpleNotification() }
    }

    private fun createNotificationChannel() {
        //Create Notification channel for SDK above 25
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            channel.description = CHANNEL_DESCRIPTION
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun displaySimpleNotification() {

        val notificationIntent = Intent(this, Notification::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingNotificationIntent: PendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val title = binding.title.text.toString()
        val message = binding.message.text.toString()
        notificationIntent.putExtra(titleExtra, title)
        notificationIntent.putExtra(messageExtra, message)
//        val titleStatusChangeListener= binding.datePicker.year

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingNotificationIntent
        )
        showAlert(
            time,
            title,
            message
        )
    }

    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(this)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(this)

        AlertDialog.Builder(this)
            .setTitle("")
            .setMessage(
                "Title: " + title + "\nMessage " + message + "\nAt " + dateFormat.format(
                    date
                ) + " " + timeFormat.format(date)
            )
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getTime(): Long {
        val minute = binding.timePicker.minute
        val hour = binding.timePicker.hour
        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }

}