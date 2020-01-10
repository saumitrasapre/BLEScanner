package com.example.blescanner;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;



public class NotifService {

    MainActivity main;

    public NotifService(MainActivity main) {
        this.main = main;
    }

    //private static final int uniqueID=123456;

    void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel= new NotificationChannel("Default","MyNotificationChannel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("MyDescription");
            notificationChannel.setShowBadge(true);
            NotificationManager notificationManager=main.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


    void triggerNotification(int uniqueID, String text) {

        Intent intent=new Intent(main,AfterNotif.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent=PendingIntent.getActivity(main,0,intent,0);

        NotificationCompat.Builder notification;
        notification=new NotificationCompat.Builder(main, "Default");
        notification.setSmallIcon(R.drawable.ic_launcher_foreground);
        notification.setContentTitle("BLE Scanner");
        notification.setContentText("Beacon "+text+" in Range");
        notification.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notification.setContentIntent(pendingIntent);
        notification.setChannelId("Default");
        notification.setOngoing(false);//Notification cannot be swiped unless you click on it
        notification.setAutoCancel(true); //Notification cancels when you click it or it redirects you to the app
        notification.setWhen(System.currentTimeMillis());


        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(main);
        notificationManagerCompat.notify(uniqueID,notification.build());
    }
    void cancelNotification(int uniqueID)
    {
        NotificationManager notificationManager = (NotificationManager)main.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(uniqueID);
    }
}
