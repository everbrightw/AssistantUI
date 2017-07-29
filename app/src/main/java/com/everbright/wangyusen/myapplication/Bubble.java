package com.everbright.wangyusen.myapplication;

/**
 * Created by wangyusen on 7/27/17.
 */

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import info.hoang8f.widget.FButton;

/**
 * floating bubbles
 */
public class Bubble extends android.support.v7.widget.AppCompatButton{
    public FButton fBbutton;
    public ImageView mImageView;
    

    public static int X_OFFSET_POSITION = 125;

    public Bubble(Context context) {
        super(context);
    }

    /**
     * make sure the button has been initializeed
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setDefaultBubble(Context context){
        if(fBbutton!=null){
            fBbutton.setBackgroundColor(context.getColor(R.color.fbutton_color_silver));
            fBbutton.setCornerRadius(50);
            fBbutton.setHeight(1);
        }
        else{
            View layout_view = initializeView(context,R.layout.fbbutton);
            fBbutton = layout_view.findViewById(R.id.default_bubbleButton);
            fBbutton.setBackgroundColor(context.getColor(R.color.fbutton_color_silver));
            fBbutton.setCornerRadius(50);
            fBbutton.setHeight(1);
        }
    }

    public void setCostumizedBubblebutton(Context context, int mLayout, int button_id){
        View layout_view = initializeView(context, mLayout);
        fBbutton = layout_view.findViewById(button_id);
    }
    public void setButtonColor(int color){
        fBbutton.setButtonColor(color);
    }

    /**
     * initialize the view from your layout
     * @param context
     * @param mLayout the layout you created
     * @return
     */

    public View initializeView(Context context, int mLayout){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View c=inflater.inflate(mLayout, null);
        return c;
    }

    /**
     * image of phone icon
     */
    public void initializePhoneIcon(){
        mImageView.setImageResource(R.drawable.contact_icon);
    }

    /**
     * window overlay params, gravity on left
     * @param y
     * @return
     */
    public WindowManager.LayoutParams setParams(int y){
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT;
        params.y = y;
        return params;

    }
    /**
     * set image view
     */
    public void setBubbleIcon(int image){
        this.mImageView.setImageResource(image);
    }





}
