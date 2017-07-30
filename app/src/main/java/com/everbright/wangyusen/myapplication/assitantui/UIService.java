package com.everbright.wangyusen.myapplication.assitantui;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import com.everbright.wangyusen.myapplication.Bubble;
import com.everbright.wangyusen.myapplication.R;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;

/**
 * Created by wangyusen on 7/26/17.
 */

public class UIService extends Service {

    private WindowManager windowManager;
    private ImageView chatHead;
    private Button button;
    int windowwidth;
    int windowheight;
    Handler handler;



    @Override
    public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * initialize handler
         */
        handler = new Handler();
        /**
         * create overlay window
         */
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        button = new Button(this);
        button.setClickable(true);

        chatHead = new ImageView(this);
        chatHead.setImageResource(R.drawable.contact_icon);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.LEFT;
        params.x = 0;
        params.y = 50;
        final Bubble bubble = new Bubble(this);
        final View mlayout = bubble.initializeView(this, R.layout.fbbutton);
//        bubble.setDefaultBubble(this);//customizing the info_button(fbButton) to default value;
        bubble.fBbutton = mlayout.findViewById(R.id.default_bubbleButton);
        bubble.fBbutton.setBackground(getDrawable(R.drawable.buttonshape));
        bubble.fBbutton.setButtonColor(getColor(R.color.fbutton_default_color));


        bubble.fBbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("test", "clicked");
                /**
                 * animation when clicked
                 */
//                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
//                button.setAnimation(animation);

            }
        });

        /**
         * show the view
         */
        windowwidth = windowManager.getDefaultDisplay().getWidth();
        windowheight = windowManager.getDefaultDisplay().getHeight();

//        final ImageView balls = (ImageView) findViewById(R.id.ball);
//        bubble.fBbutton.setBackground(getDrawable(R.drawable.buttonshape));

        chatHead.setBackground(getDrawable(R.drawable.corner));
//        chatHead.setBackgroundColor(bubble.fBbutton.getButtonColor());
        final WindowManager.LayoutParams params2 = params;

        Log.i("params", String.valueOf(params));
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View c=inflater.inflate(R.layout.image_icon, null);
        final ImageView test = c.findViewById(R.id.imageView);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mlayout != null && bubble.fBbutton!=null&& mlayout.getWindowToken()!=null) {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation);

                    bubble.fBbutton.startAnimation(animation);
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    windowManager.removeViewImmediate(mlayout);
                                }
                            });
                        }
                    }, animation.getDuration());

                }
                else{

                    windowManager.addView(mlayout,params2);
                }
            }
        });

        test.setImageDrawable(getDrawable(R.drawable.contact_icon));
        test.setBackground(getDrawable(R.drawable.corner));

        /**
         * wait fot the layout to load
         */
        windowManager.addView(c, params);
        ViewTreeObserver vto = test.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){


            @Override
            public boolean onPreDraw() {
                params2.x = test.getMeasuredWidth();
                if(params2.x> 0){
                    Log.i("aaa", String.valueOf(params2));
                    return true;
                }
                Log.i("aaa", String.valueOf(params2));

                return false;
            }
        });
        bubble.setBubbleText("first line", "second line");

        /**
         * get the parameter
         */
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           @Override
                           public void run() {
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       windowManager.addView(mlayout,params2);
                                       Log.i("actual", String.valueOf(params2));
                                   }
                               });
                           }
                       },100);}

    /**
     * create UI thread
     * @param runnable
     */
    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /**
         * prevent it from killing it self;
         */
        Log.e("service","549668785" );
        Intent notificationIntent = new Intent(this, UIService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("uiservice")
                .setContentText("okkk")
                .setSmallIcon(R.drawable.contact_icon)
                .setContentIntent(pendingIntent)
                .setTicker("wow")
                .build();

        startForeground(7, notification);
        return Service.START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("ondestroy", "servicedestroyed");
//        if (chatHead != null) windowManager.removeView(chatHead);
    }

}





