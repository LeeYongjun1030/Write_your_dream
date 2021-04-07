package org.techtown.writeyourdreambyleeyongjun;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

public class BroadcastD extends BroadcastReceiver {

    String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder;

        int keyId = intent.getIntExtra("keyId",1000);
        String title = intent.getStringExtra("title");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = R.string.notification_channel_id+"";
            CharSequence channelName = R.string.notification_channel_name+"";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;///IMPORTANCE_HIGH일시 팝업
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(channel);
            builder = new Notification.Builder(context,channelId);
        }
        else{
            builder = new Notification.Builder(context);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context,MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setSmallIcon(R.drawable.smallicon)
                .setTicker("Alarm")
                .setShowWhen(true)
                .setNumber(1)
                .setContentTitle(title)
                .setContentText("오늘의 피드백을 작성해주세요.")
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(keyId,builder.build());


        //notificationManager.cancel(keyId);
    }


}
