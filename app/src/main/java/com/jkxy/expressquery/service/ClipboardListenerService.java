package com.jkxy.expressquery.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.jkxy.expressquery.R;
import com.jkxy.expressquery.activity.AddInfoActivity;
import com.jkxy.expressquery.utils.RegularUtils;

public class ClipboardListenerService extends Service implements ClipboardManager.OnPrimaryClipChangedListener {

    private ClipboardManager manager;

    public ClipboardListenerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        manager.addPrimaryClipChangedListener(this);
        super.onCreate();
    }

    //剪贴板监听
    @Override
    public void onPrimaryClipChanged() {
        String code = manager.getPrimaryClip().getItemAt(0).getText().toString();
        if (RegularUtils.isExpressCode(code.trim())) {
            showNotification(code);
        }
    }

    private void showNotification(String str) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher); //小图标
        builder.setContentTitle("note");//标题
        builder.setContentText("检测到" + str + "有可能是快递单号，是否添加到快递列表");//详细文本
        Intent notifyIntent = new Intent(this, AddInfoActivity.class);
        notifyIntent.putExtra("code", str.trim());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(AddInfoActivity.class);
        stackBuilder.addNextIntent(notifyIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        builder.setAutoCancel(true);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    @Override
    public void onDestroy() {
        manager.removePrimaryClipChangedListener(this);
        super.onDestroy();
    }
}
