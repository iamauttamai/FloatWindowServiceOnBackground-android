package com.auttamai.floatingwindow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static int SYSTEM_ALERT_WINDOW_PERMISSION = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        Button showMenu = (Button) findViewById(R.id.showMenu);
        showMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(MainActivity.this, FloatWindowService.class));
            }
        });

        Button stopMenu = (Button) findViewById(R.id.stopMenu);
        stopMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatWindowService.stopChat();
                stopService(new Intent(MainActivity.this, FloatWindowService.class));
            }
        });

    }
}