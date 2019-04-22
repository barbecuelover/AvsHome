package com.zw.avshome.alexa.impl.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.widget.RemoteViews;


import com.zw.avshome.R;

import static android.content.Context.NOTIFICATION_SERVICE;


public class AlexaNotification {
    private static NotificationManager notificationManager;
    private String CHANNEL_ID = "Alexa_Notification_ID";
    private int NOTIFICATNUM = 101;
    private RemoteViews alexaNotificationHeadsUpRemoteView;
    private RemoteViews alexaNotificationBigRemoteView;
    private Notification customNotification;
    private Context mContext;

    public AlexaNotification(Context context) {
        this.mContext = context;
        notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
    }

    public void createNotification() {
        setNotificationCustomView();
        createNotificationChannel();
        initJsonView();
    }

    public void clearNotification(){
        if (notificationManager != null){
            notificationManager.cancel(CHANNEL_ID,NOTIFICATNUM);
        }
    }

    private void setNotificationCustomView() {
        alexaNotificationHeadsUpRemoteView = new RemoteViews(mContext.getPackageName(), R.layout.alexa_notification_upremote_view);
//        alexaNotificationBigRemoteView = new RemoteViews(mContext.getPackageName(), R.layout.alexa_notification_view);
        alexaNotificationBigRemoteView = new RemoteViews(mContext.getPackageName(), R.layout.alexa_notification_upremote_view);

        //设置自定义的style
        customNotification = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.icon_blue_alexa)
                //设置自定义的style
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomHeadsUpContentView(alexaNotificationHeadsUpRemoteView) // 浮动通知视图
                .setCustomBigContentView(alexaNotificationBigRemoteView)
                .build();
        customNotification.flags = Notification.FLAG_ONLY_ALERT_ONCE;//发起Notification后，铃声和震动均只执行一次
//        initNotificationEvent();
        notifyNotification();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = mContext.getString(R.string.alexa_notification_channel_name);//
            String description = "stop";
            int importance = NotificationManager.IMPORTANCE_HIGH;//开启通知，会弹出，发出提示音，状态栏中显示
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void initJsonView() {
        String messageTitle = "New message";
        String messageContent = "Message From Alexa";

        alexaNotificationHeadsUpRemoteView.setTextViewText(R.id.tv_alexa_titile,messageTitle);
        alexaNotificationHeadsUpRemoteView.setTextViewText(R.id.tv_alexa_centent,messageContent);

        alexaNotificationBigRemoteView.setTextViewText(R.id.tv_alexa_titile,messageTitle);
        alexaNotificationBigRemoteView.setTextViewText(R.id.tv_alexa_centent,messageContent);
        notifyNotification();
    }

    private void notifyNotification() {
        notificationManager.notify(CHANNEL_ID,NOTIFICATNUM,customNotification);
    }

}
