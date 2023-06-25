package com.example.to_do

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch




class MyReceiver: BroadcastReceiver() {


    override fun onReceive(contex: Context?, intent: Intent?) {
        val task_name = intent?.getStringExtra("message name")
        val message_id=intent?.getStringExtra("message_id")
        var id:Int=0
        if (message_id != null) {
            id=message_id.toInt()
        }

        //remove date and time after alarm is run
        val datetimedata= Room.databaseBuilder(contex!!,Date_Time_Database::class.java,"datetimetablee")
            .allowMainThreadQueries()
            .build()
        GlobalScope.launch {
            if (message_id != null) {
                datetimedata.DateTime_Dao().deleteByUiddatetime(message_id.toLong())
            }
        }

        val i = Intent(contex, MainActivity::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val flags = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            else -> PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getActivity(contex, 0, i, flags)
            val builder = NotificationCompat.Builder(contex!!, "divaker")
                .setContentTitle(task_name.toString())
                .setContentText("Tap to go application")
                .setSmallIcon(R.drawable.small_app_logo)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)

            val notificationManager = NotificationManagerCompat.from(contex)
            // if id is same then notification is update and id is diffrent diffrent then notification is also create diffrent daffrent
        if (ActivityCompat.checkSelfPermission(contex, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        notificationManager.notify(id, builder.build())
        }
    }


