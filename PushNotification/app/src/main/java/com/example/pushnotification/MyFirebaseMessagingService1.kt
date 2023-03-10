package com.example.pushnotification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel1"
const val channelName = "com.example.pushnotification"



class MyFirebaseMessagingService :FirebaseMessagingService() {


//    val appContext = applicationContext

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.getNotification() != null){
            generateNotification(remoteMessage.notification!!.body!!)

        }
    }

    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(message: String): RemoteViews {

        val remoteView = RemoteViews(
            this.packageName,
            R.layout.notification)


        //remoteView.setTextViewText(R.id.title,title)
        remoteView.setTextViewText(R.id.description,message)
        remoteView.setImageViewResource(R.id.app_logo,R.drawable.ic_tiger)

        return remoteView
    }

    fun generateNotification(message:String){
        val intent = Intent(this,MainActivity :: class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)

        //CHANNEL ID,NAME

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_tiger)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)


        builder = builder.setContent(getRemoteView(message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, this.packageName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0,builder.build())

        MediaPlayer.create(applicationContext, R.raw.tone).start()

    }
}