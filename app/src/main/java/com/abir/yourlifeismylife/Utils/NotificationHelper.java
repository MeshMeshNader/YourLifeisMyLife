package com.abir.yourlifeismylife.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

import com.abir.yourlifeismylife.R;

import java.util.ArrayList;
import java.util.Random;

class NotificationHelper {

    private Context mContext;
    private static final String NOTIFICATION_CHANNEL_ID = "10001";
    private ArrayList<String> messages = new ArrayList<>();

    NotificationHelper(Context context) {
        mContext = context;
        initMessagesArray();
    }

    private void initMessagesArray() {
        messages.add("You deserve to love all the love in the world.");
        messages.add("There is wisdom and a purpose for all these events to fall on your shoulders. There is mercy, a lot of good, an overwhelming victory.");
        messages.add("Be kind to yourself.");
        messages.add("DON’T frown. You never know who is falling in love with your smile");
        messages.add("It’s never too late.");
        messages.add("No pain No gain.");
        messages.add("Try to find happiness in every moment of your life.");
        messages.add("Always remember that you are not alone in life.");
        messages.add("Leave the past where it belongs");
        messages.add("Your smile is really beautiful");
        messages.add("Look at a mirror and you will find someone who deserves all the beautiful things in life.");
        messages.add("Do not be sad for what has passed, be ambitious and start your life again.");
        messages.add("To have one true friend is better than to have a hundred fake friends.");
        messages.add("You must believe in your abilities, always trust yourself");
        messages.add("If you fail today you will succeed tomorrow.");
        messages.add("Don't cry over anyone who won't cry over you");
        messages.add("Do not regret that you chose the wrong person who will teach you the value of the right person.");
        messages.add("Don`t be afraid of waiting , True love will always find you.");
        messages.add("One bad chapter doesn’t mean your story is over.");
        messages.add("Be the reason someone believes in the goodness of people.");
        messages.add("Small daily improvements over time will lead you to stunning results");

    }

    private String generateMessage() {
        Random r = new Random();
        int n = r.nextInt(messages.size() - 1);
        return messages.get(n);
    }

    void createNotification() {

        String message = generateMessage();

        Intent intent = new Intent(mContext, NotificationActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                100 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.hands_tran);
        mBuilder.setContentTitle("Be Happy")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "BE_HAPPY", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0 /* Request Code */, mBuilder.build());
    }
}