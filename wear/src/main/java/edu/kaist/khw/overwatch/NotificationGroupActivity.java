package edu.kaist.khw.overwatch;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

public class NotificationGroupActivity extends WearableActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_group_activity);
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
