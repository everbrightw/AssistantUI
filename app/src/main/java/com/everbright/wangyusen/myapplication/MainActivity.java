package com.everbright.wangyusen.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.everbright.wangyusen.myapplication.assitantui.UIService;

import static android.R.attr.permission;


public class MainActivity extends AppCompatActivity {
    public final static int REQUEST_CODE = 23456;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)); use this to check user permission
        checkDrawOverlayPermission();




    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkDrawOverlayPermission() {
        /** check if we already  have permissionto draw over other apps */
        if (!Settings.canDrawOverlays(this)) {
            /** if not construct intent to request permission */
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            /** request permission via start activity for result */
            startActivityForResult(intent, REQUEST_CODE);
            Log.i("test", "2");
        }
        else{
            Log.i("test", "passed");

            startService(new Intent(getBaseContext(), UIService.class));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        /** check if received result code
         is equal our requested code for draw permission  */

        if (requestCode == REQUEST_CODE) {

            if (Settings.canDrawOverlays(this)) {
                // continue here - permission was granted
                Log.i("entered", "permission=");
                startService(new Intent(this, UIService.class));

            }
        }
    }
}
