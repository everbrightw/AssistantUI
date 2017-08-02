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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import com.everbright.wangyusen.myapplication.Bubble;
import com.everbright.wangyusen.myapplication.BubbleEvent;
import com.everbright.wangyusen.myapplication.R;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by wangyusen on 7/26/17.
 */

public class UIService extends Service {


    private EventBus myEventBus = EventBus.getDefault();
    //TODO: naming windowWidth
    private int windowWidth;
    private int windowHeight;
    private Handler handler;
    private WindowManager mWindowManager;
    private Map<String, Bubble>mBubbleMap = new HashMap<>();
    // TODO: mXX
    private Bubble testBubble;

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
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        myEventBus.register(this);
        myEventBus.post(new BubbleEvent("1"));

    }

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
        Intent notificationIntent = new Intent(this, UIService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Messsage")
                .setContentText("UIService is running")
                .setSmallIcon(R.drawable.contact_icon)
                .setContentIntent(pendingIntent)
                .setTicker("EBG")
                .build();

        startForeground(7, notification);
        return Service.START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        myEventBus.unregister(this);
//        if (chatHead != null) windowManager.removeView(chatHead);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Subscribe
    // This method will be called when a HelloWorldEvent is posted
    public void onEvent(BubbleEvent event){
        //// TODO: 8/2/17 use the event to get the query id and create new task 
        newBubbleTask(7);
        runTheFuckingCoolThing(50, 7);
    }

    /**
     * add bubble to hashmap
     * @param query_id
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void newBubbleTask(long query_id){
        mBubbleMap.put(String.valueOf(query_id), new Bubble(this,query_id));
    }

    /**
     * delete this bubble
     * @param query_id
     */
    public void deleteBubbleTask(long query_id){
        mBubbleMap.remove(mBubbleMap.get(query_id));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void runTheFuckingCoolThing(int yPosition, long query_id){

        mBubbleMap.put(String.valueOf(query_id), new Bubble(this, query_id));
        final Bubble currentBubble = mBubbleMap.get(String.valueOf(query_id));
        handler = new Handler();
        windowWidth = mWindowManager.getDefaultDisplay().getWidth();
        windowHeight = mWindowManager.getDefaultDisplay().getHeight();
        final WindowManager.LayoutParams params2 = getParams(0,yPosition);
        /**
         * display the image icon, and wait it loading to get second params for button
         */
        displayImageIcon(query_id, 0,yPosition);
        ViewTreeObserver vto = currentBubble.imageIcon.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){
            @Override
            public boolean onPreDraw() {
                params2.x = currentBubble.imageIcon.getMeasuredWidth();
                if(params2.x> 0){

                    return true;
                }
                return false;
            }
        });

        /**
         * wait the image icon parameter to display button
         */
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run() {
//                                       windowManager.addView(mlayout,params2);
                        displayNewBubble(7, params2.x, params2.y);
                    }
                });
            }
        },100);
        dismissButtonWithAnimation(query_id);
    }


    public WindowManager.LayoutParams getParams(int xPosition, int yPosition){
         WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT;
        params.x = xPosition;
        params.y = yPosition;
        return params;

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void displayNewBubble(final long query_id, int xPosition, int yPosition){
        final Bubble currentBubble = mBubbleMap.get(String.valueOf(query_id));

        //TODO
        mWindowManager.addView(currentBubble.buttonView, getParams(xPosition, yPosition));

        /**
         * listen for clicking
         */
        currentBubble.fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentBubble.status = 1;//clicked
//                dismissWholeBubbleImediate(query_id);
            }
        });
    }
    //TODO: merge
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void displayImageIcon(long query_id, int xPosition, int yPosition){
        Bubble currentBubble = mBubbleMap.get(String.valueOf(query_id));

        mWindowManager.addView(currentBubble.imageView, getParams(xPosition, yPosition));


        /**
         * motion dected;
         */
    }

    /**
     * with image icon listener
     */

    public void dismissButtonWithAnimation(long query_id){
        final Bubble currentBubble = mBubbleMap.get(String.valueOf(query_id));
        currentBubble.status = 2;// manual dismiss

        currentBubble.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentBubble.buttonView != null && currentBubble.fButton!=null&& currentBubble.buttonView.getWindowToken()!=null) {

                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation);

                    currentBubble.fButton.startAnimation(animation);
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(currentBubble.buttonView.getWindowToken()!=null){
                                        /**
                                         * check if the window manager contains the layout
                                         */
                                        mWindowManager.removeViewImmediate(currentBubble.buttonView);
                                    }
                                }
                            });
                        }
                    }, animation.getDuration());

                }
                else{

                    mWindowManager.addView(currentBubble.buttonView,getParams(132,50));
                }
            }
        });
        setImageViewLongClick(currentBubble);
    }

    public void setImageViewLongClick(Bubble bubble){
        /**
         * longClcik Listener
         */
        bubble.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(view!=null){
                    dismissWholeBubbleImediate(7);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * load ml web view
     * @param html
     */
    public void loadWebView(String html){
        html = "<html><body>Hello, World!</body></html>";//use for test
        String mime = "text/html";
        String encoding = "utf-8";
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View xml_container = inflater.inflate(R.layout.xml_container, null);
        WebView myWebView = xml_container.findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadDataWithBaseURL(null, html, mime, encoding, null);

    }

    public void setImageViewDrag(final Bubble bubble){
        bubble.imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) bubble.imageView.getLayoutParams();

                switch(motionEvent.getAction())
                {

                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(bubble.trashView.getWindowToken()==null){
                            mWindowManager.addView(bubble.trashView, getParams(500, 700));
                        }
                        int x_cord = (int)motionEvent.getRawX();
                        int y_cord = (int)motionEvent.getRawY();

                        if(x_cord>windowWidth){x_cord=windowWidth;}
                        if(y_cord>windowHeight){y_cord=windowHeight;}
                        layoutParams.x = x_cord;
                        layoutParams.y = y_cord - windowHeight/2;

                        mWindowManager.updateViewLayout(bubble.imageView, getParams(layoutParams.x, layoutParams.y));
                        mWindowManager.updateViewLayout(bubble.buttonView, getParams(layoutParams.x + 132, layoutParams.y));

                        if(  (layoutParams.x <= 600&&layoutParams.x>=400)&&( layoutParams.y >=400&&layoutParams.y<=570)){
                            dismissWholeBubbleImediate(2);
                            if(bubble.trashView.getWindowToken()!=null){
                                mWindowManager.removeViewImmediate(bubble.trashView);
                            }
                        }

                        break;
                    default:
                        if(bubble.trashView.getWindowToken()!=null){
                            mWindowManager.removeViewImmediate(bubble.trashView);
                        }
                        break;
                }
                return true;
            }
        });
    }


    /**
     *
     * @param duration the time for the imageview to be dismissed
     */

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void dismissWholeBubble(int duration, long bubble_id){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Bubble current_bubble = new Bubble(this, bubble_id);
        final View mLayout = inflater.inflate(R.layout.fbbutton,null);
        final ImageView imageIcon = (ImageView) inflater.inflate(R.layout.image_icon,null);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         * delete icon and button
                         */
                        if(mLayout.getWindowToken()!=null && imageIcon.getWindowToken()!=null){
                            mWindowManager.removeViewImmediate(mLayout);
                            mWindowManager.removeViewImmediate(imageIcon);
                        }
                    }
                });
            }
        },duration);
        current_bubble.status = 3;// auto dismiss
    }

    public void dismissWholeBubbleImediate(long query_id){
        Bubble currentBubble = mBubbleMap.get(String.valueOf(query_id));
        if(currentBubble.imageView.getWindowToken() !=null){
            mWindowManager.removeViewImmediate(currentBubble.imageView);
            if(currentBubble.buttonView.getWindowToken()!=null){
            mWindowManager.removeViewImmediate(currentBubble.buttonView);
        }
        }
    }
}





