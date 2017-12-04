package edu.kaist.khw.overwatch;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

public class NotificationViewActivity extends WearableActivity {

    private TextView mAppNameView, mNotiContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);

        mAppNameView = (TextView) findViewById(R.id.appName);
        mNotiContentView = findViewById(R.id.notiContent);

        Intent intent = getIntent();

        String appName = intent.getStringExtra(MainActivity.TAG_APPNAME);
        String notiContent = intent.getStringExtra(MainActivity.TAG_NOTICONTENT);

        mAppNameView.setText(appName);
        mNotiContentView.setText(notiContent);

        Vibrator v = (Vibrator) this.getApplicationContext().getSystemService(VIBRATOR_SERVICE);
        v.vibrate(300);
        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished){
                return;
            }

            public void onFinish(){
                finish();
            }
        }.start();

        // Enables Always-on
        setAmbientEnabled();
    }
}
