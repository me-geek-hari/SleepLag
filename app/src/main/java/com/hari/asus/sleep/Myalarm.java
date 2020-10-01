package com.hari.asus.sleep;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import static com.hari.asus.sleep.MainActivity.NOTIFICATION_CHANNEL_ID;

public class Myalarm extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id" ;
    public static String NOTIFICATION = "notification" ;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "alarm", Toast.LENGTH_SHORT).show();
        MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);


        Log.d("MyAlarmBelal", "Alarm just fired");
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
        Notification notification = intent.getParcelableExtra( NOTIFICATION ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel) ;
        }
        int id = intent.getIntExtra( NOTIFICATION_ID , 0 ) ;
        assert notificationManager != null;
        notificationManager.notify(id , notification) ;

        CountDownTimer ct = new CountDownTimer(6000,1000) {
            @Override
            public void onTick(long l) {
                mediaPlayer.start();
            }

            @Override
            public void onFinish() {
mediaPlayer.stop();
            }
        };ct.start();
    }
    }


