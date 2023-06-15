package com.auttamai.floatingwindow;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AuttaphonL. on 11,March,2022
 */
public class FloatWindowService extends Service {

    double x;
    double y;
    double pressedX;
    double pressedY;
    WindowManager.LayoutParams params;
    public static WindowManager windowManager;
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout linearLayout;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setBackgroundResource(R.drawable.chat_head);
        linearLayout.setLayoutParams(layoutParams);

        CircleImageView imageView = new CircleImageView(this);
        imageView.setImageResource(R.drawable.ic_cat);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(dpToPx(60), dpToPx(60)));
        linearLayout.addView(imageView);

        //here is all the science of params
        int LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

//        // Assuming you have a reference to the View element
//        int[] location = new int[2];
//        linearLayout.getLocationOnScreen(location);
//        int xx = location[0];
//        int yy = location[1];
//        int viewWidth = linearLayout.getWidth();
//        int viewHeight = linearLayout.getHeight();
//        int centerX = xx + (viewWidth);
//        int centerY = yy + (viewHeight);

        params.x = 0;
        params.y = 0;
        params.gravity = Gravity.CENTER;
        windowManager.addView(linearLayout, params);

        //noinspection AndroidLintClickableViewAccessibility
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            WindowManager.LayoutParams updatedParameters = params;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = updatedParameters.x;
                        y = updatedParameters.y;
                        pressedX = event.getRawX();
                        pressedY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        updatedParameters.x = (int) (x + (event.getRawX() - pressedX));
                        updatedParameters.y = (int) (y + (event.getRawY() - pressedY));
                        windowManager.updateViewLayout(linearLayout, updatedParameters);
                        break;
                }
                return true;
            }
        });

    }

    public static void stopChat() {
        windowManager.removeView(linearLayout);
    }

    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
