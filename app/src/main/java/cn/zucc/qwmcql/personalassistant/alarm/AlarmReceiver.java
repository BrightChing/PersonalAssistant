package cn.zucc.qwmcql.personalassistant.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;


import cn.zucc.qwmcql.personalassistant.MainActivity;
import cn.zucc.qwmcql.personalassistant.R;


/**
 * Created by My PC on 2017/7/2.
 */

public class AlarmReceiver extends BroadcastReceiver {
    String noteTitle="";
    int noteId=0;
    int requestCode = -1;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        requestCode = intent.getIntExtra("requestCode", 0);

        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        noteTitle=intent.getStringExtra("planTitle");
        noteId=intent.getIntExtra("planId",0);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.best_buy);
        builder.setContentTitle(noteTitle);
        builder.setContentText("时间到了，快去做事吧！");
        builder.setWhen(System.currentTimeMillis());
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setAutoCancel(true);//打开程序后图标消失



        intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent =PendingIntent.getActivity(context, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        nm.notify(noteId, notification);

        Toast.makeText(context, "到时间了哦, 开始做事情吧~~", Toast.LENGTH_LONG).show();
    }

}
