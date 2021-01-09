package com.example.mysecondapplication.UI.NavigationDrawer;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.mysecondapplication.R;
import com.example.mysecondapplication.UI.Login_Activity.LoginActivity;

public class MyBroadcastReceiver extends BroadcastReceiver {
Context context;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
this.context=context;
        // Extract data included in the Intent
        CharSequence intentData = intent.getCharSequenceExtra("message");
       // Toast.makeText(context, "Javacodegeeks received the Intent's message: "+intentData, Toast.LENGTH_LONG).show();
         if(intent.getAction().matches("com.javacodegeeks.android.A_CUSTOM_INTENT"))
         {
        //    Toast.makeText(context, "A_CUSTOM_INTENT addNotification", Toast.LENGTH_LONG).show();
             addNotification();
         }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addNotification() {

        String id = "channel_1";//id of channel
        String description = "ezra";//Description information of channel
        int importance = NotificationManager.IMPORTANCE_HIGH;//The Importance of channel
        NotificationChannel channel = new NotificationChannel(id, description, importance);//Generating channel

        // prepare intent which is triggered if the
        // notification is selected
        Intent notificationIntent = new Intent(context, LoginActivity.class);



        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        Notification.Builder mBuilder = new Notification.Builder(context, id);
        mBuilder.setSmallIcon(R.drawable.road)
                .setContentTitle("Find me a ride")
                .setContentText("new travel added")
                .setSound(Uri.parse("android.resource://" +
                        getPackageName(context) + "/" + R.raw.notification))
                .setContentIntent(pendingIntent);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .build();

        Uri soundUri = Uri.parse("android.resource://" +
                getPackageName(context) + "/" + R.raw.notification);

        channel.setSound(soundUri, audioAttributes);


        // Gets an instance of the NotificationManager service

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        /*
        What Are Notification Channels?
        Notification channels enable us app developers to group our notifications into groups—channels—with
        the user having the ability to modify notification settings for the entire channel at once.
        For example, for each channel, users can completely block all notifications,
        override the importance level, or allow a notification badge to be shown.
        This new feature helps in greatly improving the user experience of an app.
        */

        mNotificationManager.createNotificationChannel(channel);
        mBuilder.setChannelId(id);

        /*
        When you issue multiple notifications about the same type of event,
        it’s best practice for your app to try to update an existing notification
        with this new information, rather than immediately creating a new notification.
        If you want to update this notification at a later date, you need to assign it an ID.
        You can then use this ID whenever you issue a subsequent notification.
        If the previous notification is still visible, the system will update this existing notification,
        rather than create a new one. In this example, the notification’s ID is 001
        */

        mNotificationManager.notify(001, mBuilder.build());
    }

    public String getPackageName(Context context) { return context.getPackageName(); }
}



