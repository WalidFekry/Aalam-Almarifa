package com.walid.knowledgeworld.web_view;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.google.android.gms.ads.MobileAds;

public class AppRoot extends Application {

    public static final String CHANNEL_ID = "Remote_notification_fire_base_new";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        initializeMobileAds();
    }

    private void initializeMobileAds() {
        new Thread(() -> MobileAds.initialize(this, initializationStatus -> {})).start();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "إشعارات التطبيق";
            String description = "قناة إشعارات تطبيق عالم المعرفة";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
