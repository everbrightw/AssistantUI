package com.everbright.wangyusen.myapplication.assitantui;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;

import android.support.annotation.RequiresApi;

import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;


import com.everbright.wangyusen.myapplication.MainActivity;
import com.everbright.wangyusen.myapplication.R;


/**
 * Created by wangyusen on 7/26/17.
 */

public class UIService extends Service {

    private WindowManager windowManager;
    private ImageView chatHead;
    public static boolean isServiceRunning = false;


    @Override
    public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        chatHead = new ImageView(this);
        chatHead.setImageResource(R.drawable.ic_home_white_24px);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        windowManager.addView(chatHead, params);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /**
         * prevent it from killing it self;
         */
        Intent intent_ = new Intent(this, MainActivity.class);
        intent_.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent_, 0);

        Notification noti = new Notification.Builder(getApplicationContext())
                .setContentTitle("Pratikk")
                .setContentText("Subject")
                .setSmallIcon(R.drawable.ic_home_white_24px)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1234, noti);

        return Service.START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        isServiceRunning = false;
        if (chatHead != null) windowManager.removeView(chatHead);
    }
}





