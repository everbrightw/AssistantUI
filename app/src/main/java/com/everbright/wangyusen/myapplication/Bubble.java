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

    public FButton fButton;
    public ImageView imageIcon;
    public long bubbleID;
    public View imageView;
    public View buttonView;

    public View trashView;
    public ImageView trashImage;
    /**
     * 1 for clicked
     * 2 for manual cancel
     * 3 for auto cancel
     */
    public int status;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Bubble(Context context, long bubbleID) {
        super(context);
        this.bubbleID = bubbleID;
        // TODO: move this out, share one WindowManager across bubbles.

        imageView = initializeView(context, R.layout.image_icon);
        buttonView = initializeView(context, R.layout.fbbutton);

        // TODO: clarify naming
        fButton = buttonView.findViewById(R.id.default_bubbleButton);
        imageIcon = imageView.findViewById(R.id.imageView);

        setFButton(context,R.color.fbutton_color_orange, "firstline", "secondline");
        setImageIcon(context, R.drawable.contact_icon, R.color.fbutton_default_color, R.drawable.corner);


        trashView = initializeView(context, R.layout.trash_layout);
        trashImage = trashView.findViewById(R.id.imageView2);
        trashImage.setImageDrawable(context.getDrawable(R.drawable.trash));
        trashImage.setBackgroundColor(context.getColor(R.color.fbutton_color_orange));


    }

    /**
     * make sure the button has been initializeed
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setImageIcon(Context context, int drawble_icon, int color_background, int drawable_background){
        imageIcon.setImageDrawable(context.getDrawable(drawble_icon));
        imageIcon.setBackgroundColor(context.getColor(color_background));
        imageIcon.setBackground(context.getDrawable(drawable_background));

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setFButton(Context context, int color, String firstLine, String secondLine){
        fButton.setButtonColor(context.getColor(color));
        fButton.setCornerRadius(50);
        fButton.setHeight(1);
        setBubbleText(firstLine, secondLine);

    }

    public void setCostumizedBubblebutton(Context context, int mLayout, int button_id){
        View layout_view = initializeView(context, mLayout);
        fButton = layout_view.findViewById(button_id);
    }


    /**
     * initialize the view from your layout
     * @param context
     * @param mLayout the layout you created
     * @return
     */

    public View initializeView(Context context, int mLayout){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(mLayout, null);
    }

    /**
     * image of phone icon
     */
    public void initializePhoneIcon(){
        imageIcon.setImageResource(R.drawable.contact_icon);
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
        this.imageIcon.setImageResource(image);
    }

    public void setBubbleText(String first_line, String second_line){
        fButton.setText(first_line + "\n "+ second_line );
    }







}
