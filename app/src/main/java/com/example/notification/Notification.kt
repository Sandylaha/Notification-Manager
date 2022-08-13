package com.example.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput


const val notificationId = 28
const val KEY_TEXT_REPLY = "key_text_reply"
const val titleExtra = "title"
const val messageExtra = "message"

class Notification : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        val intent = Intent(context, AlertStatus::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val icon = BitmapFactory.decodeResource(context?.resources, R.drawable.image)


        //Add a reply
        var replyLabel: String = "Reply"
        var remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
            setLabel(replyLabel)
            build()
        }
        //Add an action to open a new Activity
        var intent2 = Intent(context, Action1::class.java)
        var pendingActionIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_MUTABLE)

        var action1: NotificationCompat.Action =
            NotificationCompat.Action.Builder(0, "Reply", pendingActionIntent)
                .addRemoteInput(remoteInput)
                .build()


        val builder = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_message_24)
            .setContentTitle(titleExtra)
            .setContentText(messageExtra)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setLargeIcon(icon)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(icon)
                    .bigLargeIcon(null)
            )
            .addAction(action1)
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }
    }
}