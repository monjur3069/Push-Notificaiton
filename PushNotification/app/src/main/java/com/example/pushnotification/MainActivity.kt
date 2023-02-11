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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var play = findViewById<Button>(R.id.button)

        val btn = findViewById<Button>(R.id.button1)


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

            MediaPlayer.create(applicationContext, R.raw.sound).start()

        }

        play.setOnClickListener {
            /*mp.setDataSource(this, Uri.parse("android.resource://"+this.packageName+"/"+R.raw.tone))
            mp.prepare()
            mp.start()*/
            val mp = MediaPlayer.create(this, R.raw.tone)
            mp.start()

        }

        btn.setOnClickListener {
            generateNotification(R.id.description.toString())
        }

    }
}