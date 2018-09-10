package com.example.amrit.notificationapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mNotifyButton;
    private Button mUpdateButton;
    private Button mCancelButton;
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String NOTIFICATION_CHANNEL = "NotificationChannel";
    private static final String NOTIFICATION_GUIDE_URL = "https://developer.android.com/design/patterns/notifications.html";
    private static final String ACTION_UPDATE_NOTIFICATION = "com.example.amrit.notificationapp.ACTION_UPDATE_NOTIFICATION";
    private static final String ACTION_CANCEL_NOTIFICATION = "com.example.amrit.notificationapp.ACTION_CANCEL_NOTIFICATION";
    private NotificationReceiver mReceiver = new NotificationReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_UPDATE_NOTIFICATION);
        intentFilter.addAction(ACTION_CANCEL_NOTIFICATION);
        registerReceiver(mReceiver, intentFilter);

        mNotifyButton = findViewById(R.id.notify);
        mUpdateButton = findViewById(R.id.update);
        mCancelButton = findViewById(R.id.cancel);

        mNotifyButton.setEnabled(true);
        mCancelButton.setEnabled(false);
        mUpdateButton.setEnabled(false);

        mNotifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNotification();
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelNotification();
            }
        });
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public void sendNotification() {

        mNotifyButton.setEnabled(false);
        mUpdateButton.setEnabled(true);
        mCancelButton.setEnabled(true);

        Intent learnMoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NOTIFICATION_GUIDE_URL));
        PendingIntent learnMorePendingIntent = PendingIntent.getActivity
                (this,NOTIFICATION_ID,learnMoreIntent,PendingIntent.FLAG_ONE_SHOT);

        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity
                (this,NOTIFICATION_ID,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent cancelIntent = new Intent(ACTION_CANCEL_NOTIFICATION);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, cancelIntent, PendingIntent.FLAG_ONE_SHOT);

        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL)
                .setContentTitle("You are notified!")
                .setContentText("This is your notificaiton text")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .addAction(R.drawable.ic_learn_more,"Learn More",learnMorePendingIntent)
                .addAction(R.drawable.ic_learn_more, getString(R.string.learn_more), updatePendingIntent)
                .setDeleteIntent(cancelPendingIntent);

        Notification myNotification = notifyBuilder.build();
        mNotificationManager.notify(NOTIFICATION_ID,myNotification);

    }

    public void updateNotification() {

        mNotifyButton.setEnabled(false);
        mUpdateButton.setEnabled(false);
        mCancelButton.setEnabled(true);

        Bitmap androidImage = BitmapFactory.decodeResource(getResources(),R.drawable.mascot_1);

        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity
                (this,NOTIFICATION_ID,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent cancelIntent = new Intent(ACTION_CANCEL_NOTIFICATION);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, cancelIntent, PendingIntent.FLAG_ONE_SHOT);

        Intent learnMoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NOTIFICATION_GUIDE_URL));
        PendingIntent learnMorePendingIntent = PendingIntent.getActivity
                (this, NOTIFICATION_ID, learnMoreIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL)
                .setContentTitle("You are notified!")
                .setContentText("This is your notificaiton text")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(androidImage).setBigContentTitle("Notification Updated!"))
                .setDeleteIntent(cancelPendingIntent)
                .addAction(R.drawable.ic_learn_more, getString(R.string.learn_more),learnMorePendingIntent);

        Notification myNotification = notifyBuilder.build();
        mNotificationManager.notify(NOTIFICATION_ID,myNotification);
    }

    public void cancelNotification() {
        mCancelButton.setEnabled(false);
        mNotifyButton.setEnabled(true);
        mUpdateButton.setEnabled(false);

        mNotificationManager.cancel(NOTIFICATION_ID);
    }
    public class NotificationReceiver extends BroadcastReceiver {

        public NotificationReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            switch (action) {
                case ACTION_CANCEL_NOTIFICATION:
                    cancelNotification();
                    break;
                case ACTION_UPDATE_NOTIFICATION:
                    updateNotification();
                    break;
            }
        }
    }
}
